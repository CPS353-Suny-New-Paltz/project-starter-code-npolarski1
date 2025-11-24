package networkapi;

import io.grpc.stub.StreamObserver;
import shared.InputSource;
import shared.OutputSource;
import shared.ProcessResponse;
import shared.Delimiter;
import shared.ProtoConverters;
import com.google.protobuf.Empty;
import networkapi.grpc.Ack;

public class UserRequestServiceImpl extends networkapi.grpc.UserRequestServiceGrpc.UserRequestServiceImplBase {

    private final UserRequestNetworkAPI delegate;

    public UserRequestServiceImpl(UserRequestNetworkAPI delegate) {
        this.delegate = delegate;
    }

    @Override
    public void processInputSource(networkapi.grpc.InputSource request,
                                   StreamObserver<networkapi.grpc.ProcessResponse> responseObserver) {
        InputSource src = ProtoConverters.fromProto(request);
        ProcessResponse resp = delegate.processInputSource(src);
        networkapi.grpc.ProcessResponse protoResp = ProtoConverters.toProto(resp);
        responseObserver.onNext(protoResp);
        responseObserver.onCompleted();
    }

    @Override
    public void processOutputSource(networkapi.grpc.OutputSource request,
                                    StreamObserver<networkapi.grpc.ProcessResponse> responseObserver) {
        OutputSource out = ProtoConverters.fromProto(request);
        ProcessResponse resp = delegate.processOutputSource(out);
        responseObserver.onNext(ProtoConverters.toProto(resp));
        responseObserver.onCompleted();
    }

    @Override
    public void requestReadInput(Empty request,
                                 StreamObserver<Ack> responseObserver) {
        delegate.requestReadInput();
        responseObserver.onNext(Ack.newBuilder().setOk(true).build());
        responseObserver.onCompleted();
    }

    @Override
    public void passInput(Empty request,
                          StreamObserver<Ack> responseObserver) {
        delegate.passInput();
        responseObserver.onNext(Ack.newBuilder().setOk(true).build());
        responseObserver.onCompleted();
    }

    @Override
    public void requestStartComputation(Empty request,
                                        StreamObserver<networkapi.grpc.ProcessResponse> responseObserver) {
        ProcessResponse resp = delegate.requestStartComputation();
        responseObserver.onNext(ProtoConverters.toProto(resp));
        responseObserver.onCompleted();
    }

    @Override
    public void requestWriteResults(Empty request,
                                    StreamObserver<Ack> responseObserver) {
        delegate.requestWriteResults();
        responseObserver.onNext(Ack.newBuilder().setOk(true).build());
        responseObserver.onCompleted();
    }

    @Override
    public void processDelimiter(networkapi.grpc.Delimiter request,
                                 StreamObserver<Ack> responseObserver) {
        Delimiter delim = ProtoConverters.fromProto(request);
        delegate.processDelimiter(delim);
        responseObserver.onNext(Ack.newBuilder().setOk(true).build());
        responseObserver.onCompleted();
    }
}
