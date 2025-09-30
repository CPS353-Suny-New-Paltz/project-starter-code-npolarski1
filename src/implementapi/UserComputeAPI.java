package implementapi;
import java.util.List;

import shared.ComputationResult;
import shared.Delimiter;
import shared.InputInts;
import shared.InputSource;
import shared.OutputSource;
import shared.ProcessResponse;
import shared.Status;

public class UserComputeAPI implements networkapi.UserComputeAPI {
	
	private ComputeDataStorageAPI storage;
	private ComputeComponentAPI engine;
	
	private InputInts input;
	private List<ComputationResult> results;
	
	public UserComputeAPI() {
		storage = new ComputeDataStorageAPI();
	}

	@Override
	public ProcessResponse processInputSource(InputSource inputSource) {
		if (storage.setInputSource(inputSource).isSuccess()) {
			return ProcessResponse.SUCCESS;
		}
		return ProcessResponse.FAIL;
	}
	
	@Override
	public ProcessResponse processOutputSource(OutputSource outputSource) {
		if (storage.setOutputSource(outputSource).isSuccess()) {
			return ProcessResponse.SUCCESS;
		}
		return ProcessResponse.FAIL;
	}
	
	public void requestReadInput() {
		input = storage.readInput();
	}
	
	public void passInput() {
		engine.setInput(input);
	}
	
	public ProcessResponse requestStartComputation() {
		results = engine.compute();
		return ProcessResponse.SUCCESS;
	}
	
	public void requestWriteResults() {
		for (ComputationResult r : results) {
			storage.writeOutput(r);
		}
	}
}
