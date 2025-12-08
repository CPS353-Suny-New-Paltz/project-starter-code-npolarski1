package perftest;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import implementapi.ComputeComponentImpl;
import implementapi.FastComputeComponentImpl;
import shared.InputInts;
import shared.ComputationResult;

public class ComputeTimingTest {

    private static long timeOriginalCompute(List<Integer> inputs) {
        ComputeComponentImpl impl = new ComputeComponentImpl();
        impl.setInput(new InputInts(inputs));
        long start = System.nanoTime();
        List<ComputationResult> res = impl.compute();
        long end = System.nanoTime();
        return TimeUnit.NANOSECONDS.toMillis(end - start);
    }

    private static long timeFastCompute(List<Integer> inputs) {
        FastComputeComponentImpl impl = new FastComputeComponentImpl();
        impl.setInput(new InputInts(inputs));
        long start = System.nanoTime();
        List<ComputationResult> res = impl.compute();
        long end = System.nanoTime();
        return TimeUnit.NANOSECONDS.toMillis(end - start);
    }

    @Test
    public void timingComparisonSimple() throws Exception {
    	// test large input to see significant timing differences
        List<Integer> input = Arrays.asList(10_000_000);
        int runs = 5;

        List<Long> orig = new ArrayList<>();
        List<Long> fast = new ArrayList<>();

        for (int i = 0; i < runs; i++) {
            long t1 = timeOriginalCompute(input);
            orig.add(t1);
            System.err.println("original run=" + (i+1) + " ms=" + t1);

            long t2 = timeFastCompute(input);
            fast.add(t2);
            System.err.println("fast run=" + (i+1) + " ms=" + t2);
        }

        Collections.sort(orig);
        Collections.sort(fast);
        long origMed = orig.get(orig.size() / 2);
        long fastMed = fast.get(fast.size() / 2);

        System.err.println("median_original_ms=" + origMed + " median_fast_ms=" + fastMed + " speedup=" + String.format("%.2fx", (double)origMed / (double)fastMed));
    }
}
