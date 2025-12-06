package grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import com.google.protobuf.Empty;

import datastore.grpc.DataStoreServiceGrpc;

import shared.InputInts;
import shared.InputSource;
import shared.OutputSource;
import shared.ProcessResponse;
import shared.ProtoConverters;
import shared.ComputationResult;
import shared.Delimiter;

public class DataStoreClient {

    private final ManagedChannel channel;
    private final DataStoreServiceGrpc.DataStoreServiceBlockingStub blockingStub;

    public DataStoreClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext().build());
    }

    public DataStoreClient(ManagedChannel channel) {
        this.channel = channel;
        this.blockingStub = DataStoreServiceGrpc.newBlockingStub(channel);
    }

    public InputInts readInput() {
        try {
            datastore.grpc.InputInts resp = blockingStub.readInput(Empty.getDefaultInstance());
            return ProtoConverters.fromProto(resp);
        } catch (Exception e) {
            System.err.println("Error calling readInput via gRPC: " + e.getMessage());
            return new InputInts(java.util.Collections.emptyList());
        }
    }

    public ProcessResponse setInputSource(InputSource inputSource) {
        try {
            datastore.grpc.InputSource req = ProtoConverters.toProtoDS(inputSource);
            datastore.grpc.ProcessResponse resp = blockingStub.setInputSource(req);
            return ProtoConverters.fromProto(resp);
        } catch (Exception e) {
            System.err.println("Error calling setInputSource via gRPC: " + e.getMessage());
            return ProcessResponse.FAIL;
        }
    }

    public ProcessResponse setOutputSource(OutputSource outputSource) {
        try {
            datastore.grpc.OutputSource req = ProtoConverters.toProtoDS(outputSource);
            datastore.grpc.ProcessResponse resp = blockingStub.setOutputSource(req);
            return ProtoConverters.fromProto(resp);
        } catch (Exception e) {
            System.err.println("Error calling setOutputSource via gRPC: " + e.getMessage());
            return ProcessResponse.FAIL;
        }
    }

    public ProcessResponse writeOutput(ComputationResult compResult, boolean lastResult) {
        try {
            datastore.grpc.WriteRequest req = datastore.grpc.WriteRequest.newBuilder()
                    .setResult(ProtoConverters.toProto(compResult))
                    .setLast(lastResult)
                    .build();
            datastore.grpc.ProcessResponse resp = blockingStub.writeOutput(req);
            return ProtoConverters.fromProto(resp);
        } catch (Exception e) {
            System.err.println("Error calling writeOutput via gRPC: " + e.getMessage());
            return ProcessResponse.FAIL;
        }
    }

    public void setDelimiter(Delimiter delim) {
        try {
            datastore.grpc.Delimiter req = ProtoConverters.toProto(delim);
            blockingStub.setDelimiter(req);
        } catch (Exception e) {
            System.err.println("Error calling setDelimiter via gRPC: " + e.getMessage());
        }
    }

    public void shutdown() {
        if (channel != null) {
            channel.shutdown();
        }
    }
}