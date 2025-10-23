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

public class DataStorageProcessAPI implements processapi.DataStorageProcessAPI {
	
	private InputSource inputSource;
	private OutputSource outputSource;
	private Delimiter delim;

	@Override
	public InputInts readInput() {
		String filePath = inputSource.getFilePath();
		
		List<Integer> inputList = new ArrayList<Integer>();
		
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			delim = new Delimiter(reader.readLine());
			for (String line = reader.readLine(); line != null; line = reader.readLine()) {
				inputList.add(Integer.parseInt(line));
			}
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
		
		return new InputInts(inputList);
	}

	@Override
	public ProcessResponse writeOutput(ComputationResult compResult, boolean lastResult)
	{
		if (compResult == null) {
			throw new IllegalArgumentException("Computation result cannot be null");
		}
		// lastResult doesn't require checking as it is a boolean
		
		String filePath = outputSource.getFilePath();
		
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
			throw new IllegalArgumentException("Input source cannot be null");
		}
		this.inputSource = inputSource;
		return ProcessResponse.SUCCESS;
	}

	public ProcessResponse setOutputSource(OutputSource outputSource) {
		if (outputSource == null) {
			throw new IllegalArgumentException("Output source cannot be null");
		}
		this.outputSource = outputSource;
		return ProcessResponse.SUCCESS;
	}
}
