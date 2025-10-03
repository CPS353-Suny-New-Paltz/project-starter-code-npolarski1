package conceptualapi;

import project.annotations.ConceptualAPIPrototype;
import shared.InputSource;
import shared.OutputSource;

public class UserComponentAPIPrototype {

	@ConceptualAPIPrototype
	public void prototype(UserComponentAPI userApi, ComputeComponentAPI computeApi) {
		// process input and output sources
		userApi.processInputSource(new InputSource(null));
		userApi.processOutputSource(new OutputSource());

		// simulate processapi completion: read, pass, compute, and write
		userApi.requestReadInput();
		userApi.passInputToCompute(computeApi);
		userApi.requestStartComputation(computeApi);
		userApi.requestWriteResults();
	}
}


