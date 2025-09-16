package networkapi;

import project.annotations.NetworkAPI;
import shared.*;

@NetworkAPI
public interface UserComputeAPI {

	ProcessResponse processInputSource(InputSource inputSource, Delimiter delimiter);
	
	ProcessResponse processInputSource(InputSource inputSource);

	ProcessResponse processOutputSource(OutputSource outputSource);

	void sendError(String error);
	
}
