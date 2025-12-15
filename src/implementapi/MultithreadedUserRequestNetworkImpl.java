package implementapi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import shared.ComputationResult;
import shared.InputInts;
import shared.InputSource;
import shared.OutputSource;
import shared.ProcessResponse;
import shared.Delimiter;

import processapi.DataStorageProcessAPI;

/**
 * Multithreaded implementation of the UserRequestNetworkAPI.
 *
 * This implementation parallelizes the compute step by splitting the input integers into chunks and
 * submitting compute tasks to an internal fixed thread pool.
 */
public class MultithreadedUserRequestNetworkImpl implements networkapi.UserRequestNetworkAPI {

    // reasonable upper bound for internal compute workers
    public static final int DEFAULT_MAX_THREADS = 4;

    // internal compute thread pool
    private final ExecutorService computePool;

    // per-request/thread state (keep these thread-local so multiple users can share
    // one coordinator instance safely)
    private ThreadLocal<DataStorageProcessImpl> storage;
    private ThreadLocal<InputInts> input;
    private ThreadLocal<List<ComputationResult>> results;

    // optional externally-provided storage API (kept as reference for thread-local initializers)
    private final DataStorageProcessAPI externalStorageApi;

    /*
     * Creates a MultithreadedUserRequestNetworkImpl with a default maximum number of internal compute threads.
     */
    public MultithreadedUserRequestNetworkImpl() {
        this(DEFAULT_MAX_THREADS);
    }

    /*
     * Creates a MultithreadedUserRequestNetworkImpl with the specified maximum number of internal compute threads.
     */
    public MultithreadedUserRequestNetworkImpl(int maxThreads) {
        if (maxThreads <= 0) {
            System.err.println("Invalid maxThreads value: " + maxThreads + "; falling back to DEFAULT_MAX_THREADS=" + DEFAULT_MAX_THREADS);
            maxThreads = DEFAULT_MAX_THREADS;
        }
        this.computePool = Executors.newFixedThreadPool(maxThreads);

        this.externalStorageApi = null;
        // default: use in-process DataStorageProcessImpl for each thread
        this.storage = ThreadLocal.withInitial(() -> new DataStorageProcessImpl());
        this.input = new ThreadLocal<>();
        this.results = new ThreadLocal<>();
    }

    /**
     * Create a multithreaded network impl that delegates storage operations to an externally
     * provided DataStorageProcessAPI (for example a gRPC-backed adapter). This allows the
     * coordinator to use remote storage while still parallelizing compute.
     */
    public MultithreadedUserRequestNetworkImpl(DataStorageProcessAPI storageApi) {
        this(storageApi, DEFAULT_MAX_THREADS);
    }

    /**
     * Create a multithreaded network impl with external storage and a specified max thread count.
     */
    public MultithreadedUserRequestNetworkImpl(DataStorageProcessAPI storageApi, int maxThreads) {
        if (maxThreads <= 0) {
            System.err.println("Invalid maxThreads value: " + maxThreads + "; falling back to DEFAULT_MAX_THREADS=" + DEFAULT_MAX_THREADS);
            maxThreads = DEFAULT_MAX_THREADS;
        }
        this.computePool = Executors.newFixedThreadPool(maxThreads);
        this.externalStorageApi = storageApi;
        final DataStorageProcessAPI finalStorage = (storageApi == null) ? null : storageApi;
        // thread-local storage should return a delegate wrapper that forwards to the provided API
        if (finalStorage == null) {
            this.storage = ThreadLocal.withInitial(() -> new DataStorageProcessImpl());
        } else {
            // use a small anonymous adapter to satisfy the existing code which expects DataStorageProcessImpl methods.
            // wrap the provided DataStorageProcessAPI by returning a lightweight shim that delegates calls.
            this.storage = ThreadLocal.withInitial(() -> new DataStorageProcessImpl() {
                // override methods to forward to the external API where appropriate
                @Override
                public InputInts readInput() {
                    return finalStorage.readInput();
                }

                @Override
                public ProcessResponse setInputSource(InputSource inputSource) {
                    return finalStorage.setInputSource(inputSource);
                }

                @Override
                public ProcessResponse setOutputSource(OutputSource outputSource) {
                    return finalStorage.setOutputSource(outputSource);
                }

                @Override
                public ProcessResponse writeOutput(ComputationResult compResult, boolean lastResult) {
                    return finalStorage.writeOutput(compResult, lastResult);
                }

                @Override
                public void setDelimiter(Delimiter delim) {
                    finalStorage.setDelimiter(delim);
                }
            });
        }
        this.input = new ThreadLocal<>();
        this.results = new ThreadLocal<>();
    }

    /*
     * Processes the given input source by configuring the per-request storage.
     */
    @Override
    public ProcessResponse processInputSource(InputSource inputSource) {
        return NetworkApiHelper.processInputSource(storage.get(), inputSource);
    }
    
	/*
	 * Processes the given output source by configuring the per-request storage.
	 */
    @Override
    public ProcessResponse processOutputSource(OutputSource outputSource) {
        return NetworkApiHelper.processOutputSource(storage.get(), outputSource);
    }

    /*
     * Reads input integers from the configured storage into the per-request input container.
     */
    @Override
    public void requestReadInput() {
        InputInts read = NetworkApiHelper.readInput(storage.get());
        input.set(read);
    }

    /*
     * Unused in this implementation since input chunks are passed directly to compute engines in requestStartComputation.
     */
    @Override
    public void passInput() {}

    /*
	 * Starts the computation by partitioning the input integers into chunks and submitting
	 * compute tasks to the internal thread pool.
	 */
    @Override
    public ProcessResponse requestStartComputation() {
    	// ensure input is loaded
        InputInts in = input.get();
        if (in == null) {
            in = NetworkApiHelper.readInput(storage.get());
            input.set(in);
        }

        // get input integers
        List<Integer> ints = Collections.emptyList();
        try {
            ints = in.getInts();
        } catch (Exception e) {
            // print error message to show where the failure occurred
            System.err.println("Error retrieving input integers for computation: " + e.getMessage());
            e.printStackTrace();
            return ProcessResponse.FAIL;
        }

        // handle empty input case
        if (ints == null || ints.isEmpty()) {
            results.set(Collections.emptyList());
            return ProcessResponse.SUCCESS;
        }
        
        // determine number of tasks to create
        int total = ints.size(); // total input integers
        int poolSize = Math.max(1, ((java.util.concurrent.ThreadPoolExecutor) computePool).getCorePoolSize()); // current pool size
        int numTasks = Math.min(poolSize, total); // don't create more tasks than input items

        // partition the input into numTasks chunks (preserve order)
        List<Callable<List<ComputationResult>>> tasks = new ArrayList<>();
        // divide input into nearly equal chunks (ceiling division)
        int chunkSize = (total + numTasks - 1) / numTasks;
        for (int t = 0; t < numTasks; t++) { // for each task
            final int start = t * chunkSize; // inclusive start index
            final int end = Math.min(start + chunkSize, total); // exclusive end index
            if (start >= end) { // no more input to process
            	break;
            }
            final List<Integer> subList = ints.subList(start, end); // get the sublist for this task
            tasks.add(() -> { // task callable
                // each task gets its own compute engine instance
                ComputeComponentImpl engine = new ComputeComponentImpl();
                InputInts chunkInput = new InputInts(new ArrayList<>(subList));
                NetworkApiHelper.passInputToEngine(engine, chunkInput);
                List<ComputationResult> res = NetworkApiHelper.compute(engine);
                return res == null ? Collections.emptyList() : res; // return empty list on failure
            });
        }

        try {
            List<Future<List<ComputationResult>>> futures = computePool.invokeAll(tasks); // submit all tasks
            List<ComputationResult> allResults = new ArrayList<>(total); // preallocate result list
            for (Future<List<ComputationResult>> f : futures) { // gather results
                List<ComputationResult> part = f.get();
                if (part == null) {
                    results.set(null);
                    return ProcessResponse.FAIL;
                }
                allResults.addAll(part);
            }
            results.set(allResults); // set final results
            return ProcessResponse.SUCCESS;
        } catch (Exception e) {
            results.set(null);
            System.err.println("Error during parallel computation: " + e.getMessage());
            return ProcessResponse.FAIL;
        }
    }

    /*
     * Writes the computed results from the per-request results container to the configured storage.
     */
    @Override
    public void requestWriteResults() {
        NetworkApiHelper.writeResults(storage.get(), results.get());
        storage.remove();
        input.remove();
        results.remove();
    }
    
	/*
	 * Processes a delimiter in the configured storage.
	 */
    @Override
    public void processDelimiter(Delimiter delim) {
        NetworkApiHelper.processDelimiter(storage.get(), delim);
    }

    /**
     * Shutdown the internal compute pool. Called when the coordinator will no longer be used.
     */
    public void shutdown() {
        computePool.shutdown();
        try {
            if (!computePool.awaitTermination(5, TimeUnit.SECONDS)) {
                computePool.shutdownNow();
            }
        } catch (InterruptedException e) {
            computePool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
