package networkapi;

import project.annotations.NetworkAPI;
import shared.Delimiter;
import shared.InputSource;
import shared.OutputSource;
import shared.ProcessResponse;
import shared.Status;

@NetworkAPI
public interface UserComputeAPI {

	ProcessResponse processInputSource(InputSource inputSource, Delimiter delimiter);
	
	ProcessResponse processInputSource(InputSource inputSource);

	ProcessResponse processOutputSource(OutputSource outputSource);

	Status sendError(String error);
	
}
