package computeComponentApi;

import project.annotations.ConceptualAPI;
import shared.ComputationResult;
import shared.InputInts;

@ConceptualAPI
public interface ComputeComponentAPI {

	ComputationResult compute(InputInts inputInts);

	void saveResult(ComputationResult result);

}
