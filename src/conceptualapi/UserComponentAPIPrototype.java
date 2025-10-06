package conceptualapi;

import shared.InputSource;
import shared.OutputSource;

public class UserComponentAPIPrototype {

	public void prototype(UserComponentAPI conceptualApi) {
		// create local placeholders for any dependencies
		ComputeComponentAPI computeApi = null;
		
		// process input and output sources
		conceptualApi.processInputSource(new InputSource(null));
		conceptualApi.processOutputSource(new OutputSource());

		// simulate processapi completion: read, pass, compute, and write
		conceptualApi.requestReadInput();
		conceptualApi.passInputToCompute(computeApi);
		conceptualApi.requestStartComputation(computeApi);
		conceptualApi.requestWriteResults();
	}
}


