package conceptualapi;

import java.util.ArrayList;
import java.util.List;

import project.annotations.ConceptualAPIPrototype;
import shared.ComputationResult;
import shared.InputInts;

public class ComputeComponentAPIPrototype {
	
	@ConceptualAPIPrototype
	public void prototype(ComputeComponentAPI conceptualApi) {
		
		List<ComputationResult> results = conceptualApi.compute(new InputInts(new ArrayList<Integer>()));
		
		// save results in compute engine to be retrieved by data storage
		conceptualApi.saveResults(results);
		
	}
}
