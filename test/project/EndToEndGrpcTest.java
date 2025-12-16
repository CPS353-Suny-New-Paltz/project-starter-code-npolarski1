package project;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import networkapi.grpc.Ack;
import networkapi.grpc.Delimiter;
import networkapi.grpc.InputSource;
import networkapi.grpc.OutputSource;
import networkapi.grpc.ProcessResponse;
import networkapi.grpc.UserRequestServiceGrpc;
import networkapi.grpc.UserRequestServiceGrpc.UserRequestServiceBlockingStub;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EndToEndGrpcTest {

    private static Process dataStoreServer;
    private static Process userRequestServer;

    @BeforeAll
    public static void startServers() throws IOException, InterruptedException {
        String javaHome = System.getProperty("java.home");
        String javaBin = javaHome + File.separator + "bin" + File.separator + "java";
        String classpath = System.getProperty("server.classpath");
        
        if (classpath == null || classpath.isEmpty()) {
            // Fallback for running in IDE without the system property set by Gradle
            classpath = System.getProperty("java.class.path");
        }

        System.out.println("Starting DataStoreServerMain...");
        // Start DataStoreServerMain
        ProcessBuilder dataStoreBuilder = new ProcessBuilder(
                javaBin, "-cp", classpath, "grpc.DataStoreServerMain");
        dataStoreBuilder.inheritIO();
        dataStoreServer = dataStoreBuilder.start();

        // Give it a moment to start
        Thread.sleep(2000);

        System.out.println("Starting ServerMain...");
        // Start ServerMain
        ProcessBuilder serverBuilder = new ProcessBuilder(
                javaBin, "-cp", classpath, "grpc.ServerMain");
        serverBuilder.inheritIO();
        userRequestServer = serverBuilder.start();

        // Give it a moment to start
        Thread.sleep(2000);
    }

    @AfterAll
    public static void stopServers() {
        System.out.println("Stopping servers...");
        if (userRequestServer != null) {
            userRequestServer.destroy();
        }
        if (dataStoreServer != null) {
            dataStoreServer.destroy();
        }
    }

    @Test
    public void testEndToEndFlow() {
        String host = "localhost";
        int port = 50051;

        ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        UserRequestServiceBlockingStub stub = UserRequestServiceGrpc.newBlockingStub(channel);

        try {
            // 1. Send input source (manual ints)
            List<Integer> ints = Arrays.asList(1, 10, 25);
            InputSource protoInput = InputSource.newBuilder().addAllManualInts(ints).build();
            
            System.out.println("Sending processInputSource...");
            ProcessResponse resp1 = stub.processInputSource(protoInput);
            assertTrue(resp1.getIsSuccess(), "processInputSource failed");

            // 2. Send output source
            String outputPath = "test_output.txt";
            OutputSource protoOutput = OutputSource.newBuilder().setFilePath(outputPath).build();
            
            System.out.println("Sending processOutputSource...");
            ProcessResponse outResp = stub.processOutputSource(protoOutput);
            assertTrue(outResp.getIsSuccess(), "processOutputSource failed");

            // 3. Request Read Input
            System.out.println("Sending requestReadInput...");
            Ack ack = stub.requestReadInput(com.google.protobuf.Empty.getDefaultInstance());
            assertTrue(ack.getOk(), "requestReadInput failed");

            // 4. Pass Input
            System.out.println("Sending passInput...");
            ack = stub.passInput(com.google.protobuf.Empty.getDefaultInstance());
            assertTrue(ack.getOk(), "passInput failed");

            // 5. Start Computation
            System.out.println("Sending requestStartComputation...");
            ProcessResponse compResp = stub.requestStartComputation(com.google.protobuf.Empty.getDefaultInstance());
            assertTrue(compResp.getIsSuccess(), "requestStartComputation failed");

            // 6. Process Delimiter
            System.out.println("Sending processDelimiter...");
            Delimiter protoDelim = Delimiter.newBuilder().setDelim(";").build();
            ack = stub.processDelimiter(protoDelim);
            assertTrue(ack.getOk(), "processDelimiter failed");

            // 7. Request Write Results
            System.out.println("Sending requestWriteResults...");
            ack = stub.requestWriteResults(com.google.protobuf.Empty.getDefaultInstance());
            assertTrue(ack.getOk(), "requestWriteResults failed");
            
            System.out.println("End-to-end test passed successfully.");

        } finally {
            channel.shutdown();
            // Clean up output file if it exists
            File outFile = new File("test_output.txt");
            if (outFile.exists()) {
                outFile.delete();
            }
        }
    }
}
