package implementapi;
import shared.ComputationResult;
import shared.InputInts;
import shared.InputSource;
import shared.OutputSource;
import shared.ProcessResponse;
import shared.Status;

public class ComputeDataStorageAPI implements processapi.ComputeDataStorageAPI {
	
	private InputSource inputSource;
	private OutputSource outputSource;

	@Override
	public InputInts readInput() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ComputationResult processInputInts(InputInts inputInts) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Status writeOutput(ComputationResult compResult) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputSource getInputSource() {
		// TODO Auto-generated method stub
		return null;
	}

	public ProcessResponse setInputSource(InputSource inputSource) {
		this.inputSource = inputSource;
		return ProcessResponse.SUCCESS;
	}

	public ProcessResponse setOutputSource(OutputSource outputSource) {
		this.outputSource = outputSource;
		return ProcessResponse.SUCCESS;
	}
}
