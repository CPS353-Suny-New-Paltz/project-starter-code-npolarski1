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

	@Test
	public void testComputeWithValuesLessThanTwo() {
		List<Integer> inputInts = new ArrayList<Integer>();
		inputInts.add(0);
		inputInts.add(1);
		ComputeComponentAPI testApi = new ComputeComponentAPI();
		InputInts wrappedInput = new InputInts(inputInts);
		testApi.setInput(wrappedInput);
		List<ComputationResult> results = testApi.compute();
		assert results.size() == 2;
		assert results.get(0).getPrimeList().isEmpty();
		assert results.get(1).getPrimeList().isEmpty();
	}
}
