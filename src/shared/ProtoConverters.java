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

    // --- datastore proto conversions ---
    public static shared.InputSource fromProto(datastore.grpc.InputSource proto) {
        if (proto == null) {
        	return new shared.InputSource();
        }
        if (proto.getManualIntsCount() > 0) {
            return new shared.InputSource(new ArrayList<Integer>(proto.getManualIntsList()));
        }
        if (proto.hasFilePath()) {
            return new shared.InputSource(proto.getFilePath());
        }
        return new shared.InputSource();
    }

    public static datastore.grpc.InputSource toProtoDS(shared.InputSource in) {
        datastore.grpc.InputSource.Builder b = datastore.grpc.InputSource.newBuilder();
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

    public static datastore.grpc.InputInts toProto(shared.InputInts in) {
        datastore.grpc.InputInts.Builder b = datastore.grpc.InputInts.newBuilder();
        if (in == null || in.getInts() == null) {
        	return b.build();
        }
        b.addAllInts(in.getInts());
        return b.build();
    }

    public static ComputationResult fromProto(datastore.grpc.ComputationResult proto) {
        ComputationResult cr = new ComputationResult();
        if (proto == null) {
        	return cr;
        }
        List<Integer> primes = new ArrayList<Integer>(proto.getPrimeListList());
        cr.setPrimeList(primes);
        return cr;
    }

    public static datastore.grpc.ComputationResult toProto(ComputationResult cr) {
        datastore.grpc.ComputationResult.Builder b = datastore.grpc.ComputationResult.newBuilder();
        if (cr == null || cr.getPrimeList() == null) {
        	return b.build();
        }
        b.addAllPrimeList(cr.getPrimeList());
        return b.build();
    }

    public static shared.ProcessResponse fromProto(datastore.grpc.ProcessResponse proto) {
        if (proto == null) {
        	return shared.ProcessResponse.FAIL;
        }
        return proto.getIsSuccess() ? shared.ProcessResponse.SUCCESS : shared.ProcessResponse.FAIL;
    }

    public static datastore.grpc.ProcessResponse toProtoDS(shared.ProcessResponse resp) {
        return datastore.grpc.ProcessResponse.newBuilder()
                .setIsSuccess(resp.isSuccess())
                .build();
    }

    public static datastore.grpc.OutputSource toProtoDS(shared.OutputSource out) {
        datastore.grpc.OutputSource.Builder b = datastore.grpc.OutputSource.newBuilder();
        if (out == null) {
        	return b.build();
        }
        if (out.getFilePath() != null) {
        	b.setFilePath(out.getFilePath());
        }
        return b.build();
    }

    public static datastore.grpc.Delimiter toProto(datastore.grpc.Delimiter.Builder b, shared.Delimiter delim) {
        return datastore.grpc.Delimiter.newBuilder().setDelim(delim.getDelim()).build();
    }

    public static datastore.grpc.Delimiter toProto(shared.Delimiter delim) {
        datastore.grpc.Delimiter.Builder b = datastore.grpc.Delimiter.newBuilder();
        if (delim == null) {
        	return b.build();
        }
        b.setDelim(delim.getDelim());
        return b.build();
    }

    public static shared.InputInts fromProto(datastore.grpc.InputInts proto) {
        if (proto == null) {
        	return new shared.InputInts(java.util.Collections.emptyList());
        }
        return new shared.InputInts(new ArrayList<Integer>(proto.getIntsList()));
    }

    public static shared.OutputSource fromProto(datastore.grpc.OutputSource proto) {
        if (proto == null) {
        	return new shared.OutputSource();
        }
        if (proto.hasFilePath()) {
            return new shared.OutputSource(proto.getFilePath());
        }
        return new shared.OutputSource();
    }

    public static shared.Delimiter fromProto(datastore.grpc.Delimiter proto) {
        if (proto == null) {
        	return new shared.Delimiter(";");
        }
        return new shared.Delimiter(proto.getDelim());
    }
}