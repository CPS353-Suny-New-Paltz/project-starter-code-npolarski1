package networkapi;

import project.annotations.NetworkAPIPrototype;
import shared.InputSource;
import shared.OutputSource;
import shared.ProcessResponse;

public class UserComputeAPIPrototype {
	
	@NetworkAPIPrototype
	public void prototype(UserRequestNetworkAPI networkApi) {
		// process input source
		ProcessResponse inputResponse = networkApi.processInputSource(new InputSource());
		// process output source
		ProcessResponse outputResponse = networkApi.processOutputSource(new OutputSource());
	}
}