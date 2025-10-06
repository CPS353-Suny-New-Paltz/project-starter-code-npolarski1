package conceptualapi;

import java.util.List;

import project.annotations.ConceptualAPIPrototype;
import shared.ComputationResult;

public class ComputeComponentAPIPrototype {
	
	@ConceptualAPIPrototype
	public void prototype(ComputeComponentAPI conceptualApi) {
		
		List<ComputationResult> results = conceptualApi.compute();
		
	}
}
