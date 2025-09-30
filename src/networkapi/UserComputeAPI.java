package networkapi;

import project.annotations.NetworkAPI;
import shared.Delimiter;
import shared.InputSource;
import shared.OutputSource;
import shared.ProcessResponse;

@NetworkAPI
public interface UserComputeAPI {

	ProcessResponse processInputSource(InputSource inputSource);
	
	ProcessResponse processOutputSource(OutputSource outputSource);

	public void requestReadInput();
	
	public void passInput();
	
	public ProcessResponse requestStartComputation();
	
	public void requestWriteResults();
}
