package project.checkpointtests;

import implementapi.UserRequestNetworkAPI;
import shared.InputSource;
import shared.OutputSource;
import shared.ProcessResponse;

public class ManualTestingFramework {
    
    public static final String INPUT = "manualTestInput.txt";
    public static final String OUTPUT = "manualTestOutput.txt";

    public static void main(String[] args) {
        // TODO 1:
        // Instantiate a real (ie, class definition lives in the src/ folder) implementation 
        // of all 3 APIs
        implementapi.DataStorageProcessAPI storageApi = new implementapi.DataStorageProcessAPI();
        implementapi.ComputeComponentAPI computeApi = new implementapi.ComputeComponentAPI();
        UserRequestNetworkAPI userApi = new UserRequestNetworkAPI(storageApi, computeApi);

        // TODO 2:
        // Delegate IO and computation to the UserRequestNetworkAPI. The network API will
        // configure the storage to use the manual input/output files, read the input,
        // pass it to the compute component, run the computation, and write results.
        try {
            // Tell storage which input/output to use (the implementation expects
            // an InputSource/OutputSource object; here we pass the file path strings
            // so the DataStorageProcessAPI can interpret them if needed).
            if (userApi.processInputSource(new InputSource(INPUT)) != ProcessResponse.SUCCESS) {
                throw new RuntimeException("Failed to set input source");
            }

            if (userApi.processOutputSource(new OutputSource(OUTPUT)) != ProcessResponse.SUCCESS) {
                throw new RuntimeException("Failed to set output source");
            }

            // Read input from storage, hand it to compute engine, start computation,
            // and then write results back to storage.
            userApi.requestReadInput();
            userApi.passInput();
            if (userApi.requestStartComputation() != ProcessResponse.SUCCESS) {
                throw new RuntimeException("Computation failed to start");
            }
            userApi.requestWriteResults();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Helpful hint: the working directory of this program is <root project dir>,
        // so you can refer to the files just using the INPUT/OUTPUT constants. You do not 
        // need to (and should not) actually create those files in your repo
    }
}
