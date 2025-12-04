package shared;

import networkapi.grpc.InputSource;
import networkapi.grpc.OutputSource;
import networkapi.grpc.ProcessResponse;
import networkapi.grpc.Delimiter;
import com.google.protobuf.Any;

public class ProtoConverters {

    public static shared.InputSource fromProto(InputSource proto) {
        shared.InputSource in = new shared.InputSource(proto.getInputSource());
        return in;
    }

    public static InputSource toProto(shared.InputSource in) {
        return InputSource.newBuilder()
        		.setInputSource((Any) in.getInputSource())
        		.build();
    }

    public static shared.OutputSource fromProto(OutputSource proto) {
        shared.OutputSource out = new shared.OutputSource(proto.getOutputSource());
        return out;
    }

    public static OutputSource toProto(shared.OutputSource out) {
        return OutputSource.newBuilder()
        		.setOutputSource((Any) out.getOutputSource())
        		.build();
    }

    public static shared.ProcessResponse fromProto(ProcessResponse proto) {
        return proto.getIsSuccess() ? shared.ProcessResponse.SUCCESS : shared.ProcessResponse.FAIL;
    }

    public static ProcessResponse toProto(shared.ProcessResponse resp) {
        return ProcessResponse.newBuilder()
        		.setIsSuccess(resp.isSuccess())
        		.build();
    }

    public static shared.Delimiter fromProto(Delimiter proto) {
        shared.Delimiter d = new shared.Delimiter(proto.getDelim());
        return d;
    }

    public static Delimiter toProto(Delimiter delim) {
        return Delimiter.newBuilder()
        		.setDelim(delim.getDelim())
        		.build();
    }
}
