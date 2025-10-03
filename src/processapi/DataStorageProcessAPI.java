package processapi;

import project.annotations.ProcessAPI;
import shared.ComputationResult;
import shared.InputInts;
import shared.InputSource;
import shared.OutputSource;
import shared.ProcessResponse;

@ProcessAPI
public interface DataStorageProcessAPI {

	InputInts readInput();

	ProcessResponse writeOutput(ComputationResult compResult);

	public ProcessResponse setInputSource(InputSource inputSource);
	
	public ProcessResponse setOutputSource(OutputSource outputSource);
}
