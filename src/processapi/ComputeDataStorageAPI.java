package processapi;

import project.annotations.ProcessAPI;
import shared.ComputationResult;
import shared.InputInts;
import shared.InputSource;
import shared.Status;

@ProcessAPI
public interface ComputeDataStorageAPI {

	InputInts readInput();

	ComputationResult processInputInts(InputInts inputInts);

	Status writeOutput(ComputationResult compResult);

	InputSource getInputSource();

}
