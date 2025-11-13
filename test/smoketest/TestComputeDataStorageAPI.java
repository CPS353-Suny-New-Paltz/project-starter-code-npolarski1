package smoketest;

import java.util.ArrayList;

import shared.ComputationResult;
import shared.InputSource;
import shared.OutputSource;
import shared.ProcessResponse;

import org.junit.jupiter.api.Test;

import implementapi.DataStorageProcessImpl;

public class TestComputeDataStorageAPI {
	
	// smoke tests
	@Test
	public void testReadInput() {
		DataStorageProcessImpl testAPI = new DataStorageProcessImpl();
		testAPI.setInputSource(new InputSource("test/smoketest/smokeTestInput.txt"));
		assert testAPI.readInput() != null;
	}

	@Test
	public void testSetInputSourceValidation() {
		try {
		DataStorageProcessImpl testAPI = new DataStorageProcessImpl();
		testAPI.setInputSource(null);
		} catch (IllegalArgumentException e) {
			assert e.getMessage().equals("Input source cannot be null");
		}
	}

	@Test
	public void testWriteOutput() {
		ComputationResult testResult = new ComputationResult();
		testResult.setPrimeList(new ArrayList<Integer>());
		DataStorageProcessImpl testAPI = new DataStorageProcessImpl();
		testAPI.setOutputSource(new OutputSource("test/smoketest/smokeTestOutput.txt"));
		boolean lastResult = true;
		ProcessResponse status = testAPI.writeOutput(testResult, lastResult);
		assert status.isSuccess();
	}
 }
