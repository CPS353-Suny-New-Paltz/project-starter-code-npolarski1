package shared;

import networkapi.grpc.InputSource;
import networkapi.grpc.OutputSource;
import networkapi.grpc.ProcessResponse;
import networkapi.grpc.Delimiter;

import java.util.ArrayList;
import java.util.List;

public class ProtoConverters {

    public static shared.InputSource fromProto(InputSource proto) {
        if (proto == null) {
        	return new shared.InputSource();
        }
        // If manual ints are present, prefer them
        if (proto.getManualIntsCount() > 0) {
            List<Integer> ints = new ArrayList<Integer>(proto.getManualIntsList());
            return new shared.InputSource(ints);
        }
        // Otherwise use file path if present
        if (proto.hasFilePath()) {
            return new shared.InputSource(proto.getFilePath());
        }
        return new shared.InputSource();
    }

    public static InputSource toProto(shared.InputSource in) {
        InputSource.Builder b = InputSource.newBuilder();
        if (in == null) {
        	return b.build();
        }
        if (in.getManualInts() != null && !in.getManualInts().isEmpty()) {
            b.addAllManualInts(in.getManualInts());
        } else if (in.getFilePath() != null) {
            b.setFilePath(in.getFilePath());
        }
        return b.build();
    }

    public static shared.OutputSource fromProto(OutputSource proto) {
        if (proto == null) {
        	return new shared.OutputSource();
        }
        if (proto.hasFilePath()) {
            return new shared.OutputSource(proto.getFilePath());
        }
        return new shared.OutputSource();
    }

    public static OutputSource toProto(shared.OutputSource out) {
        OutputSource.Builder b = OutputSource.newBuilder();
        if (out == null) {
        	return b.build();
        }
        if (out.getFilePath() != null) {
            b.setFilePath(out.getFilePath());
        }
        return b.build();
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