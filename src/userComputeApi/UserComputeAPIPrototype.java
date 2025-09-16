package userComputeApi;

import project.annotations.NetworkAPIPrototype;
import shared.*;

public class UserComputeAPIPrototype {
	
	@NetworkAPIPrototype
	public void prototype(UserComputeAPI networkApi) {
		// process input source
		ProcessResponse inputResponse = networkApi.processInputSource(new InputSource(null), new Delimiter(null));
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
