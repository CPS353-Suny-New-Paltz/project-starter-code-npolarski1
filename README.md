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