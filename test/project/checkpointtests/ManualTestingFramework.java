package project.checkpointtests;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import implementapi.ComputeComponentAPI;
import implementapi.DataStorageProcessAPI;
import implementapi.UserRequestNetworkAPI;
import shared.ComputationResult;
import shared.InputInts;

public class ManualTestingFramework {
    
    public static final String INPUT = "manualTestInput.txt";
    public static final String OUTPUT = "manualTestOutput.txt";

    public static void main(String[] args) {
        // TODO 1:
        // Instantiate a real (ie, class definition lives in the src/ folder) implementation 
        // of all 3 APIs
        UserRequestNetworkAPI userApi = new UserRequestNetworkAPI();
        DataStorageProcessAPI storageApi = new DataStorageProcessAPI();
        ComputeComponentAPI computeApi = new ComputeComponentAPI();

        // TODO 2:
        // Run a computation with an input file of <root project dir>/manualTestInput.txt
        // and an output of <root project dir>/manualTestOutput.txt, with a delimiter of ',' 
        try {
            Path inputPath = Paths.get(INPUT);
            List<String> lines = Files.readAllLines(inputPath);

            List<Integer> inputs = new ArrayList<>();
            for (String line : lines) {
                if (line != null && !line.isBlank()) {
                    inputs.add(Integer.parseInt(line.trim()));
                }
            }

            computeApi.setInput(new InputInts(inputs));
            List<ComputationResult> results = computeApi.compute();

            // Produce one comma-separated line; one output per input
            // Use the count of primes per input as the scalar output
            String csv = results.stream()
                    .map(r -> Integer.toString(r.getPrimeList().size()))
                    .collect(Collectors.joining(","));

            Path outputPath = Paths.get(OUTPUT);
            Files.writeString(outputPath, csv);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Helpful hint: the working directory of this program is <root project dir>,
        // so you can refer to the files just using the INPUT/OUTPUT constants. You do not 
        // need to (and should not) actually create those files in your repo
    }
}
