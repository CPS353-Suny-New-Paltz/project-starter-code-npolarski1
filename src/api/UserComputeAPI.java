package api;

import project.annotations.NetworkAPI;

@NetworkAPI
public interface UserComputeAPI {

	ProcessInputResponse processInput(InputSource inputSource, Delimiter delimiter);

	void displayResult(ComputationResult result);
	
}
