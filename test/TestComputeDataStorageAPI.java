
import shared.ComputationResult;
import shared.InputInts;
import shared.InputSource;
import shared.Status;

import java.util.ArrayList;
import java.util.List;

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
	public void testProcessInputInts() {
		List<Integer> inputInts = new ArrayList<Integer>();
		inputInts.add(5);
		ComputeDataStorageAPI testAPI = new ComputeDataStorageAPI();
		ComputationResult result = testAPI.processInputInts(new InputInts(inputInts));
		List<Integer> correctOutput = new ArrayList<Integer>();
		correctOutput.add(1);
		correctOutput.add(2);
		correctOutput.add(3);
		correctOutput.add(5);
		assert result.getPrimeList().equals(correctOutput);
	}
	
	@Test
	public void testWriteOutput() {
		ComputationResult testResult = new ComputationResult();
		ComputeDataStorageAPI testAPI = new ComputeDataStorageAPI();
		Status status = testAPI.writeOutput(testResult);
		assert status.getStatus() == Status.StatusCode.SUCCESS;
	}
	
	@Test
	public void testGetInputSource() {
		ComputeDataStorageAPI testAPI = new ComputeDataStorageAPI();
		InputSource testSource = testAPI.getInputSource();
		assert testSource != null;
	}
 }
