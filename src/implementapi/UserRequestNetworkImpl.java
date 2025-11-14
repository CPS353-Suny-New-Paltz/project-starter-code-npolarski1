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
			throw new IllegalArgumentException("Storage cannot be null");
		}
		if (engineInstance == null) {
			throw new IllegalArgumentException("Compute engine cannot be null");
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
		if (inputSource == null) {
			throw new IllegalArgumentException("Input source cannot be null");
		}
		try {
			if (storage.get().setInputSource(inputSource).isSuccess()) {
				return ProcessResponse.SUCCESS;
			}
			return ProcessResponse.FAIL;
		} catch (Exception e) {
			// Prevent unexpected exceptions from escaping the network boundary
			System.err.println("Error processing input source: " + e.getMessage());
			return ProcessResponse.FAIL;
		}
	}
	
	@Override
	public ProcessResponse processOutputSource(OutputSource outputSource) {
		if (outputSource == null) {
			throw new IllegalArgumentException("Output source cannot be null");
		}
		try {
			if (storage.get().setOutputSource(outputSource).isSuccess()) {
				return ProcessResponse.SUCCESS;
			}
			return ProcessResponse.FAIL;
		} catch (Exception e) {
			// Prevent unexpected exceptions from escaping the network boundary
			System.err.println("Error processing output source: " + e.getMessage());
			return ProcessResponse.FAIL;
		}
	}
	
	public void requestReadInput() {
		try {
			InputInts read = storage.get().readInput();
			// Ensure we never leave input null
			if (read == null) {
				input.set(new InputInts(java.util.Collections.emptyList()));
			} else {
				input.set(read);
			}
		} catch (Exception e) {
			System.err.println("Error reading input: " + e.getMessage());
			input.set(new InputInts(java.util.Collections.emptyList()));
		}
	}
	
	public void passInput() {
		try {
			engine.get().setInput(input.get());
		} catch (Exception e) {
			System.err.println("Error passing input to compute engine: " + e.getMessage());
		}
	}
	
	public ProcessResponse requestStartComputation() {
		try {
			List<ComputationResult> computed = engine.get().compute();
			// store into thread-local results
			if (computed == null) {
				results.set(null);
				return ProcessResponse.FAIL;
			}
			results.set(computed);
			return ProcessResponse.SUCCESS;
		} catch (Exception e) {
			System.err.println("Error starting computation: " + e.getMessage());
			return ProcessResponse.FAIL;
		}
	}
	
	public void requestWriteResults() {
		try {
			List<ComputationResult> res = results.get();
			if (res == null) {
				return;
			}
			boolean lastResult = false;
			for (ComputationResult r : res) {
				if (r == res.get(res.size() - 1)) {
					lastResult = true;
				}
				try {
					storage.get().writeOutput(r, lastResult);
				} catch (Exception e) {
					// Log and continue attempting to write other results
					System.err.println("Error writing result: " + e.getMessage());
				}
			}
		} catch (Exception e) {
			System.err.println("Error during requestWriteResults: " + e.getMessage());
		}
	}
	
	public void processDelimiter(shared.Delimiter delim) {
		try {
			storage.get().setDelimiter(delim);
		} catch (Exception e) {
			System.err.println("Error processing delimiter: " + e.getMessage());
		}
	}
}