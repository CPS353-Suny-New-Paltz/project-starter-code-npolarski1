package api;

import project.annotations.ConceptualAPIPrototype;

public class ComputeComponentAPIPrototype {
	
	@ConceptualAPIPrototype
	public void prototype(ComputeComponentAPI conceptualApi) {
		
		ComputationResult result = conceptualApi.compute(new InputInts());
		
		// save result in compute engine to be retrieved by data storage
		conceptualApi.saveResult(result);
		
	}
}
