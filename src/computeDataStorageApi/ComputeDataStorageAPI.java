package computeDataStorageApi;

import project.annotations.ProcessAPI;
import shared.ComputationResult;
import shared.InputInts;
import shared.InputSource;

@ProcessAPI
public interface ComputeDataStorageAPI {

	InputInts readInput();

	ComputationResult processInputInts(InputInts inputInts);

	void writeOutput(ComputationResult compResult);

	InputSource getInputSource();

}
