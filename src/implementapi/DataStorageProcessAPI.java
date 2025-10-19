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
			// assumes first line is delimiter, second line is input data
            delim = new Delimiter(reader.readLine());
            String[] inputStrings = reader.readLine().split(delim.getDelim());
            for (String inputString : inputStrings) {
            	inputList.add(Integer.parseInt(inputString));
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
		
		return new InputInts(inputList);
	}

	@Override
	public ProcessResponse writeOutput(ComputationResult compResult) {
		String filePath = outputSource.getFilePath();
		
		// pass true as second argument to FileWriter to append to file
		try (FileWriter writer = new FileWriter(filePath, true)) {
			for (Integer i : compResult.getPrimeList()) {
				writer.write(i.toString());
				writer.write(delim.getDelim());
			}
			writer.write("\n");
		} catch (IOException e) {
            return ProcessResponse.FAIL;
        }
		
		return ProcessResponse.SUCCESS;
	}

	public ProcessResponse setInputSource(InputSource inputSource) {
		this.inputSource = inputSource;
		return ProcessResponse.SUCCESS;
	}

	public ProcessResponse setOutputSource(OutputSource outputSource) {
		this.outputSource = outputSource;
		return ProcessResponse.SUCCESS;
	}
}
