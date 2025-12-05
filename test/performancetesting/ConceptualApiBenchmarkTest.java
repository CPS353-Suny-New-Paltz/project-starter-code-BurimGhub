package performancetesting;

import conceptualapi.ComputationApi;
import conceptualapi.ComputeRequest;
import emptyimplementations.ConceptualAPIImpl;
import emptyimplementations.OptimizedConceptualAPIImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ConceptualApiBenchmarkTest {

    private static final int ITERATIONS = 1_000_000;
    private static final double REQUIRED_IMPROVEMENT = 1.10; // 10%

    @Test
    void optimizedShouldBeAtLeastTenPercentFaster() {
        ComputationApi original = new ConceptualAPIImpl();
        ComputationApi optimized = new OptimizedConceptualAPIImpl();

        long originalTime = runBenchmark(original);
        long optimizedTime = runBenchmark(optimized);

        double ratio = (double) originalTime / optimizedTime;
        double improvementPercent = (ratio - 1.0) * 100.0;

        System.out.println("Original time: " + originalTime + " ms");
        System.out.println("Optimized time: " + optimizedTime + " ms");
        System.out.println("Improvement: " + String.format("%.2f", improvementPercent) + "%");

        assertTrue(ratio >= REQUIRED_IMPROVEMENT, "Expected at least 10% faster; got " + improvementPercent + "%");
    }

    private long runBenchmark(ComputationApi api) {
        long start = System.currentTimeMillis();
        // for jvm warmup
        for (int i = 1; i <= ITERATIONS; i++) {
            // cycle through a realistic range
            int n = (i % 20_000) + 1;
            api.compute(new ComputeRequest(n));
        }
        for (int i = 1; i <= ITERATIONS; i++) {
            // cycle through a realistic range
            int n = (i % 20_000) + 1;
            api.compute(new ComputeRequest(n));
        }
        return System.currentTimeMillis() - start;
    }
}