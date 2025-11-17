package implementapi;

import java.util.List;

import shared.ComputationResult;
import shared.InputInts;
import shared.InputSource;
import shared.OutputSource;
import shared.ProcessResponse;
import shared.Delimiter;

/**
 * Helper class containing logic shared between single-threaded and multi-threaded
 * UserRequestNetwork implementations.
 */
public final class NetworkApiHelper {

    private NetworkApiHelper() {}

    public static ProcessResponse processInputSource(DataStorageProcessImpl storage, InputSource inputSource) {
        if (inputSource == null) {
            throw new IllegalArgumentException("Input source cannot be null");
        }
        try {
            if (storage.setInputSource(inputSource).isSuccess()) {
                return ProcessResponse.SUCCESS;
            }
            return ProcessResponse.FAIL;
        } catch (Exception e) {
            System.err.println("Error processing input source: " + e.getMessage());
            return ProcessResponse.FAIL;
        }
    }

    public static ProcessResponse processOutputSource(DataStorageProcessImpl storage, OutputSource outputSource) {
        if (outputSource == null) {
            throw new IllegalArgumentException("Output source cannot be null");
        }
        try {
            if (storage.setOutputSource(outputSource).isSuccess()) {
                return ProcessResponse.SUCCESS;
            }
            return ProcessResponse.FAIL;
        } catch (Exception e) {
            System.err.println("Error processing output source: " + e.getMessage());
            return ProcessResponse.FAIL;
        }
    }

    public static InputInts readInput(DataStorageProcessImpl storage) {
        try {
            InputInts read = storage.readInput();
            if (read == null) {
                return new InputInts(java.util.Collections.emptyList());
            }
            return read;
        } catch (Exception e) {
            System.err.println("Error reading input: " + e.getMessage());
            return new InputInts(java.util.Collections.emptyList());
        }
    }

    public static void passInputToEngine(ComputeComponentImpl engine, InputInts input) {
        try {
            engine.setInput(input);
        } catch (Exception e) {
            System.err.println("Error passing input to compute engine: " + e.getMessage());
        }
    }

    public static List<ComputationResult> compute(ComputeComponentImpl engine) {
        try {
            return engine.compute();
        } catch (Exception e) {
            System.err.println("Error starting computation: " + e.getMessage());
            return null;
        }
    }

    public static void writeResults(DataStorageProcessImpl storage, List<ComputationResult> res) {
    	if (storage == null) {
			throw new IllegalArgumentException("Storage cannot be null");
		}
        if (res == null) {
            throw new IllegalArgumentException("Results cannot be null");
        }
        try {
            boolean lastResult = false;
            for (ComputationResult r : res) {
                if (r == res.get(res.size() - 1)) {
                    lastResult = true;
                }
                try {
                    storage.writeOutput(r, lastResult);
                } catch (Exception e) {
                    System.err.println("Error writing result: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("Error during writeResults: " + e.getMessage());
        }
    }

    public static void processDelimiter(DataStorageProcessImpl storage, Delimiter delim) {
        try {
            storage.setDelimiter(delim);
        } catch (Exception e) {
            System.err.println("Error processing delimiter: " + e.getMessage());
        }
    }
}
