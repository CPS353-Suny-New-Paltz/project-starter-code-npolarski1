package api;

import project.annotations.NetworkAPI;

@NetworkAPI
public interface UserComputeAPI {

	ProcessResponse processInputSource(InputSource inputSource, Delimiter delimiter);
	
	ProcessResponse processInputSource(InputSource inputSource);

	ProcessResponse processOutputSource(OutputSource outputSource);

	void sendError(String error);
	
}
