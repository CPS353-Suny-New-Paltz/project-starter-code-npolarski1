package api;

import project.annotations.NetworkAPIPrototype;

public class UserComputeAPIPrototype {
	
	@NetworkAPIPrototype
	public void prototype(UserComputeAPI networkApi) {
		// process input source
		ProcessResponse inputResponse = networkApi.processInputSource(new InputSource(), new Delimiter());
		// process output source
		ProcessResponse outputResponse = networkApi.processOutputSource(new OutputSource());
		if (!inputResponse.success()) {
			// send error message to user
			networkApi.sendError(inputResponse.getError());
		}
		if (!outputResponse.success()) {
			// send error message to user
			networkApi.sendError(outputResponse.getError());
		}
	}
}
