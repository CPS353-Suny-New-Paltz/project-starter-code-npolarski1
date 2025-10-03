package smoketest;

import java.util.ArrayList;

import shared.ComputationResult;
import shared.ProcessResponse;

import org.junit.jupiter.api.Test;

import implementapi.DataStorageProcessAPI;

public class TestComputeDataStorageAPI {
	
	// smoke tests
	@Test
	public void testReadInput() {
		DataStorageProcessAPI testAPI = new DataStorageProcessAPI();
		assert testAPI.readInput() != null;
	}
	
	@Test
	public void testWriteOutput() {
		ComputationResult testResult = new ComputationResult();
		testResult.setPrimeList(new ArrayList<Integer>());
		DataStorageProcessAPI testAPI = new DataStorageProcessAPI();
		ProcessResponse status = testAPI.writeOutput(testResult);
		assert status.isSuccess();
	}
 }
