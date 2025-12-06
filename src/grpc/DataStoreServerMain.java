package grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import implementapi.DataStorageProcessImpl;

public class DataStoreServerMain {

    public static void main(String[] args) throws Exception {
        int port = 50052;

        DataStorageProcessImpl storageImpl = new DataStorageProcessImpl();
        DataStoreServiceImpl service = new DataStoreServiceImpl(storageImpl);

        Server server = ServerBuilder.forPort(port)
                .addService(service)
                .build()
                .start();

        System.out.println("DataStore gRPC server started, listening on " + port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** shutting down DataStore gRPC server");
            server.shutdown();
            System.err.println("*** DataStore server shut down");
        }));

        server.awaitTermination();
    }
}
