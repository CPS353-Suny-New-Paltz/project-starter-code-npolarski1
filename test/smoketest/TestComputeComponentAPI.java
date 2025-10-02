package smoketest;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import implementapi.ComputeComponentAPI;
import shared.ComputationResult;
import shared.InputInts;

public class TestComputeComponentAPI {
	 
	// smoke tests
	@Test
	public void testCompute() {
		List<Integer> inputInts = new ArrayList<Integer>();
		inputInts.add(5);
		ComputeComponentAPI testApi = new ComputeComponentAPI();
		InputInts wrappedInput = new InputInts(inputInts);
		testApi.setInput(wrappedInput);
		List<ComputationResult> results = testApi.compute();
		List<Integer> correctOutput = new ArrayList<Integer>();
		correctOutput.add(2);
		correctOutput.add(3);
		correctOutput.add(5);
		assert results.get(0).getPrimeList().equals(correctOutput);
	}
}
