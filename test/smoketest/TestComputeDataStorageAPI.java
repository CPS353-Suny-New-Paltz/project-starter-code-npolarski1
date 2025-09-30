package smoketest;

import shared.ComputationResult;
import shared.ProcessResponse;

import org.junit.jupiter.api.Test;

import implementapi.ComputeDataStorageAPI;

public class TestComputeDataStorageAPI {
	
	// smoke tests
	@Test
	public void testReadInput() {
		ComputeDataStorageAPI testAPI = new ComputeDataStorageAPI();
		assert testAPI.readInput() != null;
	}
	
	@Test
	public void testWriteOutput() {
		ComputationResult testResult = new ComputationResult();
		ComputeDataStorageAPI testAPI = new ComputeDataStorageAPI();
		ProcessResponse status = testAPI.writeOutput(testResult);
		assert status.isSuccess();
	}
 }
