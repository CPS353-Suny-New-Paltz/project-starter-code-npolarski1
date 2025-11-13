package implementapi;
import java.util.List;

import shared.ComputationResult;
import shared.InputInts;
import shared.InputSource;
import shared.OutputSource;
import shared.ProcessResponse;

public class UserRequestNetworkImpl implements networkapi.UserRequestNetworkAPI {
	
	private DataStorageProcessImpl storage;
	private ComputeComponentImpl engine;
	
	private InputInts input;
	private List<ComputationResult> results;
	
	public UserRequestNetworkImpl() {
		this(new DataStorageProcessImpl(), new ComputeComponentImpl());
	}

	public UserRequestNetworkImpl(DataStorageProcessImpl storage, ComputeComponentImpl engine) {
		if (storage == null) {
			throw new IllegalArgumentException("Storage cannot be null");
		}
		if (engine == null) {
			throw new IllegalArgumentException("Compute engine cannot be null");
		}
		
		this.storage = storage;
		this.engine = engine;
	}

	@Override
	public ProcessResponse processInputSource(InputSource inputSource) {
		if (inputSource == null) {
			throw new IllegalArgumentException("Input source cannot be null");
		}
		try {
			if (storage.setInputSource(inputSource).isSuccess()) {
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
			if (storage.setOutputSource(outputSource).isSuccess()) {
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
			InputInts read = storage.readInput();
			// Ensure we never leave input null
			if (read == null) {
				input = new InputInts(java.util.Collections.emptyList());
			} else {
				input = read;
			}
		} catch (Exception e) {
			System.err.println("Error reading input: " + e.getMessage());
			input = new InputInts(java.util.Collections.emptyList());
		}
	}
	
	public void passInput() {
		try {
			engine.setInput(input);
		} catch (Exception e) {
			System.err.println("Error passing input to compute engine: " + e.getMessage());
		}
	}
	
	public ProcessResponse requestStartComputation() {
		try {
			results = engine.compute();
			if (results == null) {
				return ProcessResponse.FAIL;
			}
			return ProcessResponse.SUCCESS;
		} catch (Exception e) {
			System.err.println("Error starting computation: " + e.getMessage());
			return ProcessResponse.FAIL;
		}
	}
	
	public void requestWriteResults() {
		try {
			if (results == null) {
				return;
			}
			boolean lastResult = false;
			for (ComputationResult r : results) {
				if (r == results.get(results.size() - 1)) {
					lastResult = true;
				}
				try {
					storage.writeOutput(r, lastResult);
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
			storage.setDelimiter(delim);
		} catch (Exception e) {
			System.err.println("Error processing delimiter: " + e.getMessage());
		}
	}
}
