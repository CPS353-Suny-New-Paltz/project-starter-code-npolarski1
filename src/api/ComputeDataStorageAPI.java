package api;

import project.annotations.ProcessAPI;

@ProcessAPI
public interface ComputeDataStorageAPI {

	InputInts readInput();

	ComputationResult processInputInts(InputInts inputInts);

	void writeOutput(ComputationResult compResult);

	InputSource getInputSource();

}
