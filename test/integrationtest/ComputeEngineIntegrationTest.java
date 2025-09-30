package integrationtest;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import implementapi.ComputeComponentAPI;
import implementapi.UserComputeAPI;
import shared.ComputationResult;
import shared.InputSource;

public class ComputeEngineIntegrationTest {
	
	@Test
	public void computeEngineTest() {
		TestDataStoreAPI testApi = new TestDataStoreAPI();
		ComputeComponentAPI testComponentApi = new ComputeComponentAPI();
		UserComputeAPI testUserComputeApi = new UserComputeAPI();
		
		TestInputSource inputSource = new TestInputSource();
		List<Integer> input = new ArrayList<Integer>();
		input.add(1);
		input.add(10);
		input.add(25);
		inputSource.setInput(input);
		
		testUserComputeApi.processInputSource(new InputSource(inputSource));
		
		testApi.readInput();
		
		List<ComputationResult> result = testComponentApi.compute();
		
		TestOutputSource outputSource = new TestOutputSource();
		
		for (ComputationResult r : result) {
			testApi.writeOutput(r);
		}
		
		List<String> correctOutput = new ArrayList<String>();
		correctOutput.add("1");
		correctOutput.add("1, 2, 3, 5, 7");
		correctOutput.add("1, 2, 3, 5, 7, 11, 13, 17, 19, 23");
		assert outputSource.getOutput().equals(correctOutput);
	}
}
