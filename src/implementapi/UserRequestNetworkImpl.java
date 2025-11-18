package implementapi;
import java.util.List;

import shared.ComputationResult;
import shared.InputInts;
import shared.InputSource;
import shared.OutputSource;
import shared.ProcessResponse;

public class UserRequestNetworkImpl implements networkapi.UserRequestNetworkAPI {
    
    // make the storage and engine per-thread so a single coordinator can be shared by
    // multiple concurrent users
    private ThreadLocal<DataStorageProcessImpl> storage;
    private ThreadLocal<ComputeComponentImpl> engine;
    
    // per-thread request state
    private ThreadLocal<InputInts> input;
    private ThreadLocal<List<ComputationResult>> results;
    
    public UserRequestNetworkImpl() {
        this(new DataStorageProcessImpl(), new ComputeComponentImpl());
    }

    public UserRequestNetworkImpl(DataStorageProcessImpl storageInstance, ComputeComponentImpl engineInstance) {
        if (storageInstance == null) {
            System.err.println("Storage instance was null; using default DataStorageProcessImpl");
            storageInstance = new DataStorageProcessImpl();
        }
        if (engineInstance == null) {
            System.err.println("Engine instance was null; using default ComputeComponentImpl");
            engineInstance = new ComputeComponentImpl();
        }
        
        // create a fresh instance of storage and engine per thread
        this.storage = ThreadLocal.withInitial(() -> new DataStorageProcessImpl());
        this.engine = ThreadLocal.withInitial(() -> new ComputeComponentImpl());
        
        // initialize per-thread containers
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
    
    public void requestReadInput() {
        InputInts read = NetworkApiHelper.readInput(storage.get());
        input.set(read);
    }
    
    public void passInput() {
        NetworkApiHelper.passInputToEngine(engine.get(), input.get());
    }
    
    public ProcessResponse requestStartComputation() {
        List<ComputationResult> computed = NetworkApiHelper.compute(engine.get());
        if (computed == null) {
            results.set(null);
            return ProcessResponse.FAIL;
        }
        results.set(computed);
        return ProcessResponse.SUCCESS;
    }
    
    public void requestWriteResults() {
        NetworkApiHelper.writeResults(storage.get(), results.get());
    }
    
    public void processDelimiter(shared.Delimiter delim) {
        NetworkApiHelper.processDelimiter(storage.get(), delim);
    }
}