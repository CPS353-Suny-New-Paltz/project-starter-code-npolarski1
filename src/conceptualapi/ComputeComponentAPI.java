package conceptualapi;

import java.util.List;

import project.annotations.ConceptualAPI;
import shared.ComputationResult;
import shared.InputInts;

@ConceptualAPI
public interface ComputeComponentAPI {

	List<ComputationResult> compute(InputInts inputInts);

	void saveResult(ComputationResult result);

}
