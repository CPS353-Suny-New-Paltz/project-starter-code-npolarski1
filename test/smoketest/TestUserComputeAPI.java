package smoketest;
import org.junit.jupiter.api.Test;

import implementapi.UserRequestNetworkImpl;
import shared.InputSource;
import shared.OutputSource;
import shared.ProcessResponse;

public class TestUserComputeAPI {
	
	// smoke tests
	@Test
	public void testProcessInputSourceNoDelimiter() {
		UserRequestNetworkImpl testAPI = new UserRequestNetworkImpl();
		InputSource testInputSource = new InputSource();
		ProcessResponse testResponse = testAPI.processInputSource(testInputSource);
		assert testResponse.isSuccess();
	}
	
	@Test
	public void testProcessOutputSource() {
		UserRequestNetworkImpl testAPI = new UserRequestNetworkImpl();
		OutputSource testOutputSource = new OutputSource();
		ProcessResponse testResponse = testAPI.processOutputSource(testOutputSource);
		assert testResponse.isSuccess();
	}
}