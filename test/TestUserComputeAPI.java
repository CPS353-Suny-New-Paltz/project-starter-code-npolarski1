import implementapi.UserComputeAPI;
import shared.Delimiter;
import shared.InputSource;
import shared.OutputSource;
import shared.ProcessResponse;
import shared.Status;

public class TestUserComputeAPI {
	
	// smoke tests
	public void testProcessInputSourceWithDelimiter() {
		UserComputeAPI testAPI = new UserComputeAPI();
		InputSource testInputSource = new InputSource(System.in);
		Delimiter testDelimiter = new Delimiter();
		ProcessResponse testResponse = testAPI.processInputSource(testInputSource, testDelimiter);
		assert testResponse.success();
	}
	
	public void testProcessInputSourceNoDelimiter() {
		UserComputeAPI testAPI = new UserComputeAPI();
		InputSource testInputSource = new InputSource(System.in);
		ProcessResponse testResponse = testAPI.processInputSource(testInputSource);
		assert testResponse.success();
	}
	
	public void testProcessOutputSource() {
		UserComputeAPI testAPI = new UserComputeAPI();
		OutputSource testOutputSource = new OutputSource();
		ProcessResponse testResponse = testAPI.processOutputSource(testOutputSource);
		assert testResponse.success();
	}
	
	public void testSendError() {
		UserComputeAPI testAPI = new UserComputeAPI();
		Status testStatus = testAPI.sendError("test error");
		// check error was successfully displayed
		assert testStatus.getStatus().equals(Status.StatusCode.SUCCESS);
	}
}
