package integrationtest;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import implementapi.ComputeComponentAPI;
import java.util.List;

public class ComputeComponentAPIExceptionHandlingTest {

    @Test
    void compute_whenInputNotSet_returnsEmptyListNotException() {
        ComputeComponentAPI api = new ComputeComponentAPI();
        // intentionally don't call setInput to make compute() throw a NullPointerException
        List<?> result = api.compute();

        // Compute method should catch exceptions and return an empty list rather than throwing
        assertNotNull(result, "compute should never return null even on exception");
        assertTrue(result.isEmpty(), "compute should return empty list when internal exception occurs");
    }
}
