package grpc;

import implementapi.UserRequestNetworkImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import networkapi.UserRequestNetworkAPI;

public class ServerMain {

    public static void main(String[] args) throws Exception {
        int port = 50051; // default gRPC port

        UserRequestNetworkAPI impl = new UserRequestNetworkImpl();

        Server server = ServerBuilder
                .forPort(port)
                .addService(new UserRequestServiceImpl(impl))
                .build()
                .start();

        System.out.println("gRPC server started, listening on " + port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** shutting down gRPC server");
            server.shutdown();
            System.err.println("*** server shut down");
        }));

        server.awaitTermination();
    }
}
