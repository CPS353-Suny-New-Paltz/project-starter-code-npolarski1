package conceptualapi;

import java.util.ArrayList;

import project.annotations.ConceptualAPIPrototype;
import shared.ComputationResult;
import shared.InputInts;

public class ComputeComponentAPIPrototype {
	
	@ConceptualAPIPrototype
	public void prototype(ComputeComponentAPI conceptualApi) {
		
		ComputationResult result = conceptualApi.compute(new InputInts(new ArrayList<Integer>()));
		
		// save result in compute engine to be retrieved by data storage
		conceptualApi.saveResult(result);
		
	}
}
