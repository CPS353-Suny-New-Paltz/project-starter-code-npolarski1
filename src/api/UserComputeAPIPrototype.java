package api;

import project.annotations.NetworkAPIPrototype;

public class UserComputeAPIPrototype {
	
	@NetworkAPIPrototype
	public void prototype(UserComputeAPI networkApi) {
		// process input
		ProcessInputResponse response = networkApi.processInput(new InputSource(), new Delimiter());
		if (response.success()) {
			// run computation
			ComputationResult result = response.getCompResults();
			// display results
			networkApi.displayResult(result);
		}
	}
}
