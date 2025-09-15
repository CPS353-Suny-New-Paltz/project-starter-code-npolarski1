package api;

import project.annotations.NetworkAPI;

@NetworkAPI
public interface UserComputeAPI {

	ProcessInputResponse processInput(InputSource inputSource, Delimiter delimiter);
	
	// for default delimiter
	ProcessInputResponse processInput(InputSource inputSource);

	void displayResult(ComputationResult result);
	
}
