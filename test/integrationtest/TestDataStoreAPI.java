package integrationtest;

import shared.ComputationResult;
import shared.InputInts;
import shared.InputSource;
import shared.Status;

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
	
}
