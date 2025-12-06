package grpc;

import io.grpc.stub.StreamObserver;
import com.google.protobuf.Empty;

import datastore.grpc.DataStoreServiceGrpc;

import shared.ProtoConverters;
import shared.InputInts;
import shared.InputSource;
import shared.OutputSource;
import shared.ProcessResponse;
import shared.Delimiter;
import shared.ComputationResult;

import processapi.DataStorageProcessAPI;

public class DataStoreServiceImpl extends DataStoreServiceGrpc.DataStoreServiceImplBase {

    private final DataStorageProcessAPI delegate;

    public DataStoreServiceImpl(DataStorageProcessAPI delegate) {
        this.delegate = delegate;
    }

    @Override
    public void setInputSource(datastore.grpc.InputSource request,
                               StreamObserver<datastore.grpc.ProcessResponse> responseObserver) {
        InputSource src = ProtoConverters.fromProto(request);
        ProcessResponse resp = delegate.setInputSource(src);
        responseObserver.onNext(ProtoConverters.toProtoDS(resp));
        responseObserver.onCompleted();
    }

    @Override
    public void setOutputSource(datastore.grpc.OutputSource request,
                                StreamObserver<datastore.grpc.ProcessResponse> responseObserver) {
        OutputSource out = ProtoConverters.fromProto(request);
        ProcessResponse resp = delegate.setOutputSource(out);
        responseObserver.onNext(ProtoConverters.toProtoDS(resp));
        responseObserver.onCompleted();
    }

    @Override
    public void readInput(Empty request, StreamObserver<datastore.grpc.InputInts> responseObserver) {
        InputInts ints = delegate.readInput();
        responseObserver.onNext(ProtoConverters.toProto(ints));
        responseObserver.onCompleted();
    }

    @Override
    public void writeOutput(datastore.grpc.WriteRequest request,
                            StreamObserver<datastore.grpc.ProcessResponse> responseObserver) {
        datastore.grpc.ComputationResult protoRes = request.getResult();
        ComputationResult res = ProtoConverters.fromProto(protoRes);
        boolean last = request.getLast();
        ProcessResponse resp = delegate.writeOutput(res, last);
        responseObserver.onNext(ProtoConverters.toProtoDS(resp));
        responseObserver.onCompleted();
    }

    @Override
    public void setDelimiter(datastore.grpc.Delimiter request,
                             StreamObserver<datastore.grpc.ProcessResponse> responseObserver) {
        Delimiter delim = ProtoConverters.fromProto(request);
        delegate.setDelimiter(delim);
        responseObserver.onNext(ProtoConverters.toProtoDS(ProcessResponse.SUCCESS));
        responseObserver.onCompleted();
    }
}
