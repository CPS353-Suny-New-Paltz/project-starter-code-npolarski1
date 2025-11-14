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

/**
 * Multithreaded implementation of the UserRequestNetworkAPI.
 *
 * This implementation parallelizes the compute step by splitting the input integers into chunks and
 * submitting compute tasks to an internal fixed thread pool.
 */
public class MultithreadedUserRequestNetworkImpl implements networkapi.UserRequestNetworkAPI {

    // reasonable upper bound for internal compute workers
    public static final int DEFAULT_MAX_THREADS = 4;

    private final ExecutorService computePool;

    // per-request/thread state (keep these thread-local so multiple users can share
    // one coordinator instance safely)
    private ThreadLocal<DataStorageProcessImpl> storage;
    private ThreadLocal<InputInts> input;
    private ThreadLocal<List<ComputationResult>> results;

    public MultithreadedUserRequestNetworkImpl() {
        this(DEFAULT_MAX_THREADS);
    }

    public MultithreadedUserRequestNetworkImpl(int maxThreads) {
        if (maxThreads <= 0) {
            throw new IllegalArgumentException("maxThreads must be > 0");
        }
        this.computePool = Executors.newFixedThreadPool(maxThreads);

        this.storage = ThreadLocal.withInitial(() -> new DataStorageProcessImpl());
        this.input = new ThreadLocal<>();
        this.results = new ThreadLocal<>();
    }

    @Override
    public ProcessResponse processInputSource(InputSource inputSource) {
        return NetworkApiHelper.processInputSource(storage.get(), inputSource);
    }

    @Override
    public ProcessResponse processOutputSource(OutputSource outputSource) {
        return NetworkApiHelper.processOutputSource(storage.get(), outputSource);
    }

    @Override
    public void requestReadInput() {
        InputInts read = NetworkApiHelper.readInput(storage.get());
        input.set(read);
    }

    @Override
    public void passInput() {
        // pass input chunks to task-local compute engines during requestStartComputation
    }

    @Override
    public ProcessResponse requestStartComputation() {
        InputInts in = input.get();
        if (in == null) {
            in = NetworkApiHelper.readInput(storage.get());
            input.set(in);
        }

        List<Integer> ints = Collections.emptyList();
        try {
            ints = in.getInts();
        } catch (Exception e) {
            return ProcessResponse.FAIL;
        }

        if (ints == null || ints.isEmpty()) {
            results.set(Collections.emptyList());
            return ProcessResponse.SUCCESS;
        }
        
        int total = ints.size();
        int poolSize = Math.max(1, ((java.util.concurrent.ThreadPoolExecutor) computePool).getCorePoolSize()); // current pool size
        int numTasks = Math.min(poolSize, total); // don't create more tasks than input items

        // partition the input into numTasks chunks (preserve order)
        List<Callable<List<ComputationResult>>> tasks = new ArrayList<>();
        int chunkSize = (total + numTasks - 1) / numTasks;
        for (int t = 0; t < numTasks; t++) {
            final int start = t * chunkSize;
            final int end = Math.min(start + chunkSize, total);
            if (start >= end) {
            	break;
            }
            final List<Integer> subList = ints.subList(start, end);
            tasks.add(() -> {
                // each task gets its own compute engine instance
                ComputeComponentImpl engine = new ComputeComponentImpl();
                InputInts chunkInput = new InputInts(new ArrayList<>(subList));
                NetworkApiHelper.passInputToEngine(engine, chunkInput);
                List<ComputationResult> res = NetworkApiHelper.compute(engine);
                return res == null ? Collections.emptyList() : res;
            });
        }

        try {
            List<Future<List<ComputationResult>>> futures = computePool.invokeAll(tasks);
            List<ComputationResult> allResults = new ArrayList<>(total);
            for (Future<List<ComputationResult>> f : futures) {
                List<ComputationResult> part = f.get();
                if (part == null) {
                    results.set(null);
                    return ProcessResponse.FAIL;
                }
                allResults.addAll(part);
            }
            results.set(allResults);
            return ProcessResponse.SUCCESS;
        } catch (Exception e) {
            results.set(null);
            System.err.println("Error during parallel computation: " + e.getMessage());
            return ProcessResponse.FAIL;
        }
    }

    @Override
    public void requestWriteResults() {
        NetworkApiHelper.writeResults(storage.get(), results.get());
    }

    @Override
    public void processDelimiter(Delimiter delim) {
        NetworkApiHelper.processDelimiter(storage.get(), delim);
    }

    /**
     * Shutdown the internal compute pool. Call this when the coordinator will no longer be used.
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
