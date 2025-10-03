package implementapi;
import java.util.List;

import shared.ComputationResult;
import shared.InputInts;
import shared.InputSource;
import shared.OutputSource;
import shared.ProcessResponse;

public class UserRequestNetworkAPI implements networkapi.UserRequestNetworkAPI {
	
	private DataStorageProcessAPI storage;
	private ComputeComponentAPI engine;
	
	private InputInts input;
	private List<ComputationResult> results;
	
	public UserRequestNetworkAPI() {
		storage = new DataStorageProcessAPI();
		engine = new ComputeComponentAPI();
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
