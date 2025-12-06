package grpc;

import implementapi.UserRequestNetworkImpl;
import implementapi.ComputeComponentImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import networkapi.UserRequestNetworkAPI;

public class ServerMain {

    public static void main(String[] args) throws Exception {
        int port = 50051; // default gRPC port for user requests
        int dataStorePort = 50052; // datastore gRPC port

        // create a gRPC client that talks to the data store process
        DataStoreClient dataStoreClient = new DataStoreClient("localhost", dataStorePort);

        // wrap the client in an adapter that can produce a DataStorageProcessAPI delegate
        DataStoreClientAdapter storageAdapter = new DataStoreClientAdapter(dataStoreClient);

        // pass the gRPC-backed DataStorageProcessAPI (anonymous implementation) into the network implementation
        UserRequestNetworkAPI impl = new UserRequestNetworkImpl(storageAdapter.toProcessApi(), new ComputeComponentImpl());

        Server server = ServerBuilder
                .forPort(port)
                .addService(new UserRequestServiceImpl(impl))
                .build()
                .start();

        System.out.println("gRPC server started, listening on " + port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** shutting down gRPC server");
            server.shutdown();
            dataStoreClient.shutdown();
            System.err.println("*** server shut down");
        }));

        server.awaitTermination();
    }
}