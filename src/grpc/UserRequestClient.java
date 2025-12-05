package grpc;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import networkapi.grpc.Ack;
import networkapi.grpc.Delimiter;
import networkapi.grpc.InputSource;
import networkapi.grpc.OutputSource;
import networkapi.grpc.ProcessResponse;
import networkapi.grpc.UserRequestServiceGrpc;
import networkapi.grpc.UserRequestServiceGrpc.UserRequestServiceBlockingStub;

/**
 * Simple command-line gRPC client for the UserRequestService.
 * Prompts the user to provide an input source (file or typed numbers), an output file,
 * and an optional delimiter, then sends the appropriate RPCs to the server and
 * prints whether the requested computation succeeded.
 */
public class UserRequestClient {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        System.out.println("User Request Client");

        String host = "localhost";
        int port = 50051;

        System.out.println("Choose input mode:");
        System.out.println("  1) Upload a file");
        System.out.println("  2) Type a list of integers (comma or space separated)");
        System.out.print("Selection (1 or 2): ");
        String choice = scanner.nextLine().trim();

        networkapi.grpc.InputSource protoInput = null; // to be constructed based on user choice

        if (choice.equals("1")) {
            System.out.print("Path to input file: ");
            String path = scanner.nextLine().trim();
            if (!Files.exists(Paths.get(path))) {
                System.out.println("Warning: file '" + path + "' does not exist on this machine. Ensure the server can access this path.");
            }
            // send the file path in the proto field file_path
            protoInput = InputSource.newBuilder().setFilePath(path).build();
        } else {
            System.out.print("Enter numbers (space or comma separated): ");
            String nums = scanner.nextLine().trim();
            String[] parts = nums.split("[\\s,]+"); // split by whitespace or comma
            List<Integer> ints = new ArrayList<>();
            for (String p : parts) {
                if (p.isEmpty()) {
                	continue;
                }
                try { 
                	ints.add(Integer.parseInt(p)); 
                } catch (NumberFormatException e) {
                    System.out.println("Skipping invalid number: " + p);
                }
            }
            // put manual integers into the repeated manual_ints field
            protoInput = InputSource.newBuilder().addAllManualInts(ints).build();
        }

        System.out.print("Output file path: ");
        String outputPath = scanner.nextLine().trim();

        System.out.print("Optional delimiter (press Enter to skip, default is semicolon): ");
        String delim = scanner.nextLine();
        if (delim == null || delim.isEmpty()) {
        	delim = ";";
        }

        // build proto for output path
        networkapi.grpc.OutputSource protoOutput = OutputSource.newBuilder()
                .setFilePath(outputPath)
                .build();

        // connect to gRPC server
        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        UserRequestServiceBlockingStub stub = UserRequestServiceGrpc.newBlockingStub(channel);

        try {
        	// send the RPCs in order, printing responses
            System.out.println("Sending input source to server...");
            ProcessResponse resp1 = stub.processInputSource(protoInput);
            System.out.println("processInputSource response: success=" + resp1.getIsSuccess());

            System.out.println("Sending output source to server...");
            ProcessResponse outResp = stub.processOutputSource(protoOutput);
            System.out.println("processOutputSource response: success=" + outResp.getIsSuccess());

            System.out.println("Telling server to read input...");
            Ack ack = stub.requestReadInput(com.google.protobuf.Empty.getDefaultInstance());
            System.out.println("requestReadInput ack=" + ack.getOk());

            System.out.println("Telling server to pass input into computation component...");
            ack = stub.passInput(com.google.protobuf.Empty.getDefaultInstance());
            System.out.println("passInput ack=" + ack.getOk());

            System.out.println("Starting computation on server...");
            ProcessResponse compResp = stub.requestStartComputation(com.google.protobuf.Empty.getDefaultInstance());
            System.out.println("Computation completed: success=" + compResp.getIsSuccess());

            System.out.println("Setting delimiter (" + delim + ") and requesting write results...");
            Delimiter protoDelim = Delimiter.newBuilder().setDelim(delim).build();
            ack = stub.processDelimiter(protoDelim);
            System.out.println("processDelimiter ack=" + ack.getOk());

            ack = stub.requestWriteResults(com.google.protobuf.Empty.getDefaultInstance());
            System.out.println("requestWriteResults ack=" + ack.getOk());

            System.out.println("Task complete. Computation success=" + compResp.getIsSuccess());

        } catch (Exception e) {
            System.err.println("Error during RPCs: " + e.getMessage());
            e.printStackTrace();
        } finally { // clean up
            channel.shutdownNow();
        }

        scanner.close();
    }
}