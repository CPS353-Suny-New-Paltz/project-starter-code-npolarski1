package integrationtest;

import shared.ComputationResult;
import shared.InputInts;
import shared.InputSource;
import shared.OutputSource;
import shared.ProcessResponse;

public class TestDataStoreAPI implements processapi.ComputeDataStorageAPI {
	
	private TestInputSource inputSource;
	
	private TestOutputSource outputSource;
	
	public void setInputSource(TestInputSource inputSource) {
		this.inputSource = inputSource;
	}
	
	public void setOutputSource(TestOutputSource outputSource) {
		this.outputSource = outputSource;
	}

	@Override
	public InputInts readInput() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProcessResponse writeOutput(ComputationResult compResult) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProcessResponse setInputSource(InputSource inputSource) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProcessResponse setOutputSource(OutputSource outputSource) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
