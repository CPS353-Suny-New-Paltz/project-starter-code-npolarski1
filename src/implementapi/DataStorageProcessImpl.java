package implementapi;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
			throw new IllegalStateException("Input source has not been set");
		}
		String filePath = inputSource.getFilePath();
		
		List<Integer> inputList = new ArrayList<Integer>();
		
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				if (line.trim().isEmpty()) {
					throw new NumberFormatException("Empty line encountered in input file");
				}
				inputList.add(Integer.parseInt(line));
			}
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
		
		return new InputInts(inputList);
	}

	@Override
	public ProcessResponse writeOutput(ComputationResult compResult, boolean lastResult) {
		if (compResult == null) {
			throw new IllegalArgumentException("Computation result cannot be null");
		}
		// lastResult doesn't require checking as it is a boolean
		
		if (outputSource == null) {
			throw new IllegalStateException("Output source has not been set");
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
