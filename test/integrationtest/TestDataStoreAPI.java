package integrationtest;

import java.util.ArrayList;
import java.util.List;

import processapi.DataStorageProcessAPI;
import shared.ComputationResult;
import shared.InputInts;
import shared.InputSource;
import shared.OutputSource;
import shared.ProcessResponse;

public class TestDataStoreAPI implements DataStorageProcessAPI {
	
	private TestInputSource inputSource;

	private TestOutputSource outputSource;


	public InputInts readInput() {
		if (inputSource == null) {
			return new InputInts(new ArrayList<Integer>());
		}
		return new InputInts(inputSource.getInput());
	}

	public ProcessResponse writeOutput(ComputationResult compResult, boolean lastResult) {
		if (outputSource == null) {
			return ProcessResponse.FAIL;
		}
		List<String> current = outputSource.getOutput();
		if (current == null) {
			current = new ArrayList<String>();
		}
		List<Integer> primes = compResult.getPrimeList();
		StringBuilder line = new StringBuilder();
		if (primes != null && !primes.isEmpty()) {
			boolean first = true;
			for (Integer p : primes) {
				if (!first) {
					line.append(", ");
				}
				line.append(p);
				first = false;
			}
		}
		current.add(line.toString());
		outputSource.setOutput(current);
		return ProcessResponse.SUCCESS;
	}

	// Interface-compatible setters that adapt shared wrappers to test doubles
	@Override
	public ProcessResponse setInputSource(InputSource inputSource) {
		if (inputSource == null) {
			return ProcessResponse.FAIL;
		}
		// If manual ints were provided, adapt them into TestInputSource
		if (inputSource.getManualInts() != null) {
			TestInputSource tis = new TestInputSource();
			List<Integer> ints = inputSource.getManualInts();
			if (ints == null) {
				ints = new ArrayList<Integer>();
			}
			tis.setInput(new ArrayList<Integer>(ints));
			this.inputSource = tis;
			return ProcessResponse.SUCCESS;
		}
		// If a file path was provided, we cannot adapt it to the in-memory TestInputSource
		if (inputSource.getFilePath() != null) {
			return ProcessResponse.FAIL;
		}
		return ProcessResponse.FAIL;
	}

	@Override
	public ProcessResponse setOutputSource(OutputSource outputSource) {
		if (outputSource == null) {
			return ProcessResponse.FAIL;
		}
		// If a file path was provided, we cannot adapt it to the in-memory TestOutputSource
		if (outputSource.getFilePath() != null) {
			return ProcessResponse.FAIL;
		}
		return ProcessResponse.FAIL;
	}

	public ProcessResponse setInputSource(TestInputSource inputSource) {
		if (inputSource == null) {
			return ProcessResponse.FAIL;
		}
		this.inputSource = inputSource;
		return ProcessResponse.SUCCESS;
	}

	public ProcessResponse setOutputSource(TestOutputSource outputSource) {
		if (outputSource == null) {
			return ProcessResponse.FAIL;
		}
		this.outputSource = outputSource;
		return ProcessResponse.SUCCESS;
	}
	
}