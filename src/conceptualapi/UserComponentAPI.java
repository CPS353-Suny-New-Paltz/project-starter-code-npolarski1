package conceptualapi;

import shared.InputSource;
import shared.OutputSource;
import shared.ProcessResponse;

public interface UserComponentAPI {

	ProcessResponse processInputSource(InputSource inputSource);

	ProcessResponse processOutputSource(OutputSource outputSource);

	void requestReadInput();

	void passInputToCompute(ComputeComponentAPI computeEngine);

	ProcessResponse requestStartComputation(ComputeComponentAPI computeEngine);

	void requestWriteResults();
}


