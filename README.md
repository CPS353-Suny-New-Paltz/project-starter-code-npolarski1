# Software Engineering Project Starter Code

The system lists all prime numbers less than or equal to the input.  
For example, an input of '10' will have an output of '2, 3, 5, 7'.  
![Checkpoint 2 System Diagram](https://github.com/CPS353-Suny-New-Paltz/project-starter-code-npolarski1/blob/main/Checkpoint2Diagram.png?raw=true)

## Multithreaded Network API

`implementapi.MultithreadedUserRequestNetworkImpl` parallelizes the conceptual compute step by splitting the input integers into chunks and submitting those chunks to an internal fixed thread pool. The implementation uses `Executors.newFixedThreadPool` with a default upper bound of 4 worker threads.

## Improved Compute Engine Benchmark Results

Both original and new compute components were tested on an input of 10,000,000 to highlight the differences in speed. 5 runs are completed and the median times of original vs new are compared to see if the new is at least 10% faster. These are the results:

median_original_ms=61 
median_fast_ms=36 
speedup=1.69x

This speedup is 69% faster for the fast compute component.

The benchmark test is located at [test/perftest/ComputeTimingTest.java](test/perftest/ComputeTimingTest.java)

The issue was that the algorithm was not optimized enough. To fix this, a data structure that is more memory efficient was used and even numbers are now skipped in the algorithm as they are already known to be not prime.


## Running the gRPC servers (how it works)

This project contains two gRPC mains:

- `grpc.DataStoreServerMain` — a simple gRPC server that provides the data storage process API (list input integers, configure input/output sources, write output). It listens by default on port 50052.
- `grpc.ServerMain` — the user-request gRPC server that accepts user requests and delegates compute work to a `MultithreadedUserRequestNetworkImpl`. It talks to the DataStore server as a gRPC client and listens by default on port 50051.

High-level flow:
1. Start the DataStore server (it exposes an API the user-request server will call to read inputs and write outputs).
2. Start the UserRequest server; it constructs a `DataStoreClient` that connects to the DataStore server and wraps it with `DataStoreClientAdapter` before constructing `MultithreadedUserRequestNetworkImpl`.
3. The UserRequest server receives requests, reads input from the datastore, parallelizes computation across an internal fixed thread pool, and writes results back to the datastore.

Important build note: the project uses protobufs for the gRPC interfaces. If you run `./gradlew clean build` and the generated proto Java classes are missing, explicitly run the proto generation step first:

    ./gradlew generateProto

After proto generation you can build the project with:

    ./gradlew clean build

Running the servers:

- Run the DataStore server first (default port 50052):

  - From an IDE: run the `grpc.DataStoreServerMain` main class.
  - From the command line after a successful build you can run the main class with the compiled classes on the classpath. Example (macOS / Linux zsh):

    java -cp build/classes/java/main:build/resources/main grpc.DataStoreServerMain

- Run the UserRequest server (default port 50051):

  - From an IDE: run the `grpc.ServerMain` main class.
  - From the command line after a successful build:

    java -cp build/classes/java/main:build/resources/main grpc.ServerMain