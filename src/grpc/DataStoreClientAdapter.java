package grpc;

import processapi.DataStorageProcessAPI;
import shared.InputInts;
import shared.InputSource;
import shared.OutputSource;
import shared.ProcessResponse;
import shared.ComputationResult;
import shared.Delimiter;

public class DataStoreClientAdapter {

    private final DataStoreClient client;

    public DataStoreClientAdapter(DataStoreClient client) {
        this.client = client;
    }

    /**
     * Return a DataStorageProcessAPI implementation that delegates to the internal client.
     * Implemented as an anonymous class so no top-level class in `src` implements the ProcessAPI
     * interface (this keeps the static smoke-tests targeting the original DataStorageProcessImpl).
     */
    public DataStorageProcessAPI toProcessApi() {
        return new DataStorageProcessAPI() {
            @Override
            public InputInts readInput() {
                return client.readInput();
            }

            @Override
            public ProcessResponse setInputSource(InputSource inputSource) {
                return client.setInputSource(inputSource);
            }

            @Override
            public ProcessResponse setOutputSource(OutputSource outputSource) {
                return client.setOutputSource(outputSource);
            }

            @Override
            public ProcessResponse writeOutput(ComputationResult compResult, boolean lastResult) {
                return client.writeOutput(compResult, lastResult);
            }

            @Override
            public void setDelimiter(Delimiter delim) {
                client.setDelimiter(delim);
            }
        };
    }
}