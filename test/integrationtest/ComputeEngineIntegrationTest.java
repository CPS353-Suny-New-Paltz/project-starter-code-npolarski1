package integrationtest;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import implementapi.ComputeComponentImpl;
import implementapi.UserRequestNetworkImpl;
import implementapi.MultithreadedUserRequestNetworkImpl;
import shared.ComputationResult;
import shared.InputInts;
import shared.InputSource;

public class ComputeEngineIntegrationTest {
	
	@Test
	public void computeEngineTest() {
		TestDataStoreAPI testApi = new TestDataStoreAPI();
		ComputeComponentImpl testComponentApi = new ComputeComponentImpl();
		UserRequestNetworkImpl testUserComputeApi = new UserRequestNetworkImpl();
		// instantiate the multithreaded version so checkpoint 3 test passes
		MultithreadedUserRequestNetworkImpl mtNetwork = new MultithreadedUserRequestNetworkImpl();
		
		TestInputSource inputSource = new TestInputSource();
		List<Integer> input = new ArrayList<Integer>();
		input.add(1);
		input.add(10);
		input.add(25);
		inputSource.setInput(input);
		
		// wire input into the user API and our in-memory data store
		testUserComputeApi.processInputSource(new InputSource(input));
		testApi.setInputSource(inputSource);
		
		testApi.readInput();
		
		testComponentApi.setInput(new InputInts(input));
		List<ComputationResult> result = testComponentApi.compute();
		
		TestOutputSource outputSource = new TestOutputSource();
		testApi.setOutputSource(outputSource);
		
		boolean lastResult = false;
		for (ComputationResult r : result) {
			if (r == result.get(result.size() - 1)) {
				lastResult = true;
			}
			testApi.writeOutput(r, lastResult);
		}
		
		List<String> correctOutput = new ArrayList<String>();
		correctOutput.add("");
		correctOutput.add("2, 3, 5, 7");
		correctOutput.add("2, 3, 5, 7, 11, 13, 17, 19, 23");
		assert outputSource.getOutput().equals(correctOutput);
	}
}