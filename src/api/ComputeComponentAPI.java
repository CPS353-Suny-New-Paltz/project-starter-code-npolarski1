package api;

import project.annotations.ConceptualAPI;

@ConceptualAPI
public interface ComputeComponentAPI {

	ComputationResult compute(InputInts inputInts);

	void saveResult(ComputationResult result);

}
