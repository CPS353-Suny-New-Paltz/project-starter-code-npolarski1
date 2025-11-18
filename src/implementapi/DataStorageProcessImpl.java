package implementapi;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import shared.ComputationResult;
import shared.Delimiter;
import shared.InputInts;
import shared.InputSource;
import shared.OutputSource;
import shared.ProcessResponse;

public class DataStorageProcessImpl implements processapi.DataStorageProcessAPI {
	
	private InputSource inputSource;
	private OutputSource outputSource;
	private Delimiter delim;

	@Override
	public InputInts readInput() {
		if (inputSource == null) {
			// return empty input container instead of throwing exception
			System.err.println("readInput called but input source has not been set");
			return new InputInts(Collections.emptyList());
		}
		String filePath = inputSource.getFilePath();
		
		List<Integer> inputList = new ArrayList<Integer>();
		
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				if (line.trim().isEmpty()) {
					// skip empty lines instead of throwing exception
					System.err.println("Skipping empty line in input file: " + filePath);
					continue;
				}
				try {
					inputList.add(Integer.parseInt(line));
				} catch (NumberFormatException nfe) {
					// log and return empty list instead of throwing exception
					System.err.println("Invalid integer in input file: '" + line + "' - skipping");
				}
			}
		} catch (IOException e) {
			System.err.println("Error reading file: " + e.getMessage());
			return new InputInts(Collections.emptyList());
		}
		
		return new InputInts(inputList);
	}

	@Override
	public ProcessResponse writeOutput(ComputationResult compResult, boolean lastResult) {
		if (compResult == null) {
			System.err.println("writeOutput called with null computation result");
			return ProcessResponse.FAIL;
		}
		
		if (outputSource == null) {
			System.err.println("writeOutput called but output source has not been set");
			return ProcessResponse.FAIL;
		}
		String filePath = outputSource.getFilePath();
		
		if (delim == null) {
			delim = new Delimiter(";");
		}
		
		// pass true as second argument to FileWriter to append to file
		try (FileWriter writer = new FileWriter(filePath, true)) {
			List<Integer> primes = compResult.getPrimeList();
			for (Integer i : primes) {
				writer.write(i.toString());
				if (i != primes.get(primes.size() - 1)) {
					writer.write(delim.getDelim());
				}
			}
			if (!lastResult) {
				writer.write(',');
			}
		} catch (IOException e) {
			System.err.println("Error writing output file: " + e.getMessage());
			return ProcessResponse.FAIL;
		}
		
		return ProcessResponse.SUCCESS;
	}

	public ProcessResponse setInputSource(InputSource inputSource) {
		if (inputSource == null) {
			return ProcessResponse.FAIL;
		}
		this.inputSource = inputSource;
		return ProcessResponse.SUCCESS;
	}

	public ProcessResponse setOutputSource(OutputSource outputSource) {
		if (outputSource == null) {
			return ProcessResponse.FAIL;
		}
		this.outputSource = outputSource;
		return ProcessResponse.SUCCESS;
	}

	public void setDelimiter(Delimiter delim) {
		this.delim = delim;
	}
}