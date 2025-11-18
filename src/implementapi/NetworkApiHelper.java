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
        if (storage == null) {
            System.err.println("processInputSource called with null storage");
            return ProcessResponse.FAIL;
        }
        if (inputSource == null) {
            System.err.println("processInputSource called with null inputSource");
            return ProcessResponse.FAIL;
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
        if (storage == null) {
            System.err.println("processOutputSource called with null storage");
            return ProcessResponse.FAIL;
        }
        if (outputSource == null) {
            System.err.println("processOutputSource called with null outputSource");
            return ProcessResponse.FAIL;
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
        if (storage == null) {
            System.err.println("readInput called with null storage");
            return new InputInts(java.util.Collections.emptyList());
        }
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
        if (engine == null) {
            System.err.println("passInputToEngine called with null engine");
            return;
        }
        try {
            engine.setInput(input);
        } catch (Exception e) {
            System.err.println("Error passing input to compute engine: " + e.getMessage());
        }
    }

    public static List<ComputationResult> compute(ComputeComponentImpl engine) {
        if (engine == null) {
            System.err.println("compute called with null engine");
            return java.util.Collections.emptyList();
        }
        try {
            return engine.compute();
        } catch (Exception e) {
            System.err.println("Error starting computation: " + e.getMessage());
            return java.util.Collections.emptyList();
        }
    }

    public static void writeResults(DataStorageProcessImpl storage, List<ComputationResult> res) {
        if (storage == null) {
            System.err.println("writeResults called with null storage");
            return;
        }
        if (res == null) {
            System.err.println("writeResults called with null results");
            return;
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
        if (storage == null) {
            System.err.println("processDelimiter called with null storage");
            return;
        }
        if (delim == null) {
            System.err.println("processDelimiter called with null delimiter");
            return;
        }
        try {
            storage.setDelimiter(delim);
        } catch (Exception e) {
            System.err.println("Error processing delimiter: " + e.getMessage());
        }
    }
}