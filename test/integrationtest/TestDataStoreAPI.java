package integrationtest;

import java.util.ArrayList;
import java.util.List;

import shared.ComputationResult;
import shared.InputInts;
import shared.ProcessResponse;

public class TestDataStoreAPI {
	
	private TestInputSource inputSource;

	private TestOutputSource outputSource;


	public InputInts readInput() {
		if (inputSource == null) {
			return new InputInts(new ArrayList<Integer>());
		}
		return new InputInts(inputSource.getInput());
	}

	public ProcessResponse writeOutput(ComputationResult compResult) {
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

	public ProcessResponse setInputSource(TestInputSource inputSource) {
		if (inputSource == null) return ProcessResponse.FAIL;
		this.inputSource = inputSource;
		return ProcessResponse.SUCCESS;
	}

	public ProcessResponse setOutputSource(TestOutputSource outputSource) {
		if (outputSource == null) return ProcessResponse.FAIL;
		this.outputSource = outputSource;
		return ProcessResponse.SUCCESS;
	}
	
}
