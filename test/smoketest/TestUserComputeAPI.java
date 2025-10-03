package smoketest;
import org.junit.jupiter.api.Test;

import implementapi.UserRequestNetworkAPI;
import shared.InputSource;
import shared.OutputSource;
import shared.ProcessResponse;

public class TestUserComputeAPI {
	
	// smoke tests
	@Test
	public void testProcessInputSourceNoDelimiter() {
		UserRequestNetworkAPI testAPI = new UserRequestNetworkAPI();
		InputSource testInputSource = new InputSource(System.in);
		ProcessResponse testResponse = testAPI.processInputSource(testInputSource);
		assert testResponse.isSuccess();
	}
	
	@Test
	public void testProcessOutputSource() {
		UserRequestNetworkAPI testAPI = new UserRequestNetworkAPI();
		OutputSource testOutputSource = new OutputSource();
		ProcessResponse testResponse = testAPI.processOutputSource(testOutputSource);
		assert testResponse.isSuccess();
	}
}
