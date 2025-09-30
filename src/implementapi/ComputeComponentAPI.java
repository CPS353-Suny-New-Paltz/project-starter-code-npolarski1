package implementapi;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import shared.ComputationResult;
import shared.InputInts;

public class ComputeComponentAPI implements conceptualapi.ComputeComponentAPI {
	
	private InputInts input;
	
	private List<Integer> calculatePrimes(Integer n) {
		// there are no prime numbers less than 2, so return empty list if n < 2
		if (n < 2) return Collections.emptyList();
		
		// composite contains a true/false for each integer up to n
		// true if number is composite
		// false if number is possibly prime
		boolean[] composite = new boolean[n + 1];
        int limit = (int) Math.sqrt(n);
        
        // only check integers up to sqrt(n) because once sqrt(n) is reached in the loop, all integers
        // after that point will have been marked composite already
        for (int p = 2; p <= limit; p++) {
            if (!composite[p]) {
                // set every multiple of p to true since they must be composite
            	// start from p^2 because every integer before that would have already been marked
            	// (cast to long to avoid overflow)
                for (long m = (long) p * p; m <= n; m += p) {
                    composite[(int) m] = true;
                }
            }
        }
        
        List<Integer> primes = new ArrayList<>();
        for (int i = 2; i <= n; i++) {
        	// if composite[i] is false, then the number is prime and is added to the prime list
            if (!composite[i]) primes.add(i);
        }
        return primes;
	}

	public List<ComputationResult> compute() {
		List<Integer> inputs = input.getInts();
		List<ComputationResult> outputs = new ArrayList<ComputationResult>();
		
		for (Integer n : inputs) {
			ComputationResult currentOutput = new ComputationResult();
			currentOutput.setPrimeList(calculatePrimes(n));
			outputs.add(currentOutput);
		}
		
		return outputs;
	}
	
	public void setInput(InputInts input) {
		this.input = input;
	}
}
