package smoketest;
import org.junit.jupiter.api.Test;

import implementapi.UserComputeAPI;
import shared.InputSource;
import shared.OutputSource;
import shared.ProcessResponse;

public class TestUserComputeAPI {
	
	// smoke tests
	@Test
	public void testProcessInputSourceNoDelimiter() {
		UserComputeAPI testAPI = new UserComputeAPI();
		InputSource testInputSource = new InputSource(System.in);
		ProcessResponse testResponse = testAPI.processInputSource(testInputSource);
		assert testResponse.isSuccess();
	}
	
	@Test
	public void testProcessOutputSource() {
		UserComputeAPI testAPI = new UserComputeAPI();
		OutputSource testOutputSource = new OutputSource();
		ProcessResponse testResponse = testAPI.processOutputSource(testOutputSource);
		assert testResponse.isSuccess();
	}
}
