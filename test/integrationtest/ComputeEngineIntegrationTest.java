package integrationtest;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class ComputeEngineIntegrationTest {
	
	@Test
	public void computeEngineTest() {
		TestDataStoreAPI testApi = new TestDataStoreAPI();
		
		TestInputSource inputSource = new TestInputSource();
		List<Integer> input = new ArrayList<Integer>();
		input.add(1);
		input.add(10);
		input.add(25);
		inputSource.setInput(input);
		
		testApi.setInputSource(inputSource);
		
		TestOutputSource outputSource = new TestOutputSource();
		
		List<String> correctOutput = new ArrayList<String>();
		correctOutput.add("1");
		correctOutput.add("1, 2, 3, 5, 7");
		correctOutput.add("1, 2, 3, 5, 7, 11, 13, 17, 19, 23");
		assert outputSource.getOutput().equals(correctOutput);
	}
}
