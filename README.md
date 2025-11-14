# Software Engineering Project Starter Code

The system lists all prime numbers less than or equal to the input.  
For example, an input of '10' will have an output of '2, 3, 5, 7'.  
![Checkpoint 2 System Diagram](https://github.com/CPS353-Suny-New-Paltz/project-starter-code-npolarski1/blob/main/Checkpoint2Diagram.png?raw=true)

## Multithreaded Network API

`implementapi.MultithreadedUserRequestNetworkImpl` parallelizes the conceptual compute step by splitting the input integers into chunks and submitting those chunks to an internal fixed thread pool. The implementation uses `Executors.newFixedThreadPool` with a default upper bound of 4 worker threads.