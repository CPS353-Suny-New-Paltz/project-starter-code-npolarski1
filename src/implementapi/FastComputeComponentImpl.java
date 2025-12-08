package implementapi;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.BitSet;

import shared.ComputationResult;
import shared.InputInts;

/*
 * An optimized implementation of the ComputeComponentAPI that uses a more efficient
 * algorithm and data structures to calculate prime numbers up to n.
 * 
 * Bottleneck found to be compute component in JConsole
 * 
 * Found to be 60% faster for an input of 10,000,000
 */
public class FastComputeComponentImpl implements conceptualapi.ComputeComponentAPI {
	
	private InputInts input;
	
	private List<Integer> calculatePrimes(Integer n) {
		// there are no prime numbers less than 2, so return empty list if n < 2
		if (n == null || n < 2) {
			return Collections.emptyList();
		}

		// BitSet is more efficient than boolean[] for large n because it uses 1 bit per entry instead of 1 byte
		// only store odd numbers, since all even numbers > 2 are not prime
		// map index i -> number = 2*i + 1. Index 0 corresponds to 1 (not prime), index 1 -> 3, etc.
		// only need to store up to (n-1)/2 indices to represent all odd numbers <= n
		int maxIndex = (n - 1) / 2;
		BitSet isComposite = new BitSet(maxIndex + 1);

		int limit = (int) Math.sqrt(n);
		int limitIndex = (limit - 1) / 2;

		for (int pIndex = 1; pIndex <= limitIndex; pIndex++) {
			if (!isComposite.get(pIndex)) {
				int p = 2 * pIndex + 1;
				long startNum = (long) p * (long) p; // p*p
				int start = (int) ((startNum - 1L) / 2L);
				for (int j = start; j <= maxIndex; j += p) {
					isComposite.set(j);
				}
			}
		}

		List<Integer> primes = new ArrayList<>();
		if (n >= 2) {
			primes.add(2); // 2 wasn't included in the BitSet since it was even
		}
		for (int i = 1; i <= maxIndex; i++) {
			if (!isComposite.get(i)) {
				primes.add(2 * i + 1); // map index back to number
			}
		}
		return primes;
	}

	public List<ComputationResult> compute() {
		try {
			List<Integer> inputs = input.getInts();
			List<ComputationResult> outputs = new ArrayList<ComputationResult>();

			for (Integer n : inputs) {
				ComputationResult currentOutput = new ComputationResult();
				currentOutput.setPrimeList(calculatePrimes(n));
				outputs.add(currentOutput);
			}

			return outputs;
		} catch (Exception e) {
			// Exception handling
			System.err.println("Error during computation: " + e.getMessage());
			return Collections.emptyList();
		}
	}
	
	public void setInput(InputInts input) {
		if (input == null) {
			System.err.println("setInput called with null; ignoring and leaving existing input unchanged");
			return;
		}
		this.input = input;
	}
}