import java.util.ArrayList;
import java.util.List;

import implementapi.ComputeComponentAPI;
import shared.ComputationResult;
import shared.InputInts;

public class TestComputeComponentAPI {
	 
	// smoke tests
	public void testCompute() {
		List<Integer> inputInts = new ArrayList<Integer>();
		inputInts.add(5);
		ComputeComponentAPI testApi = new ComputeComponentAPI();
		ComputationResult result = testApi.compute(new InputInts(inputInts));
		List<Integer> correctOutput = new ArrayList<Integer>();
		correctOutput.add(1);
		correctOutput.add(2);
		correctOutput.add(3);
		correctOutput.add(5);
		assert result.getPrimeList().equals(correctOutput);
	}
	
	public void testSaveResult() {
		ComputeComponentAPI testApi = new ComputeComponentAPI();
		ComputationResult testResult = new ComputationResult();
		// test passes if saveResult doesn't throw error
		testApi.saveResult(testResult);
	}
}
