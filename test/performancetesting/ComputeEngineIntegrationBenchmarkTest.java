package performancetesting;

import conceptualapi.ComputationApi;
import emptyimplementations.ConceptualAPIImpl;
import emptyimplementations.OptimizedConceptualAPIImpl;
import networkapi.ComputeEngine;
import networkapi.Delimiters;
import networkapi.JobSubmission;
import emptyimplementations.MultiThreadedNetworkAPIImpl;
import processapi.DataStorageAPI;
import processapi.ReadResponse;
import processapi.WriteResponse;
import processapi.ReadRequest;
import processapi.WriteRequest;
import processapi.IntegerStream;
import emptyimplementations.IntegerStreamImpl;
import emptyimplementations.ReadResponseImpl;
import emptyimplementations.WriteResponseImpl;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ComputeEngineIntegrationBenchmarkTest {

	private static final int NUM_JOBS = 200;
	private static final double REQUIRED_IMPROVEMENT = 1.10; // 10%

	@Test
	void optimizedEngineIsAtLeastTenPercentFaster() {
		System.out.println("\nCOMPUTE ENGINE INTEGRATION BENCHMARK\n");

		// Setup test data store
		TestDataStore dataStore = new TestDataStore();

		// Test with original implementation
		ComputationApi originalCompute = new ConceptualAPIImpl();
		ComputeEngine originalEngine = new MultiThreadedNetworkAPIImpl(dataStore, originalCompute);
		long originalTime = runBenchmark(originalEngine, dataStore);

		// Test with optimized implementation
		ComputationApi optimizedCompute = new OptimizedConceptualAPIImpl();
		ComputeEngine optimizedEngine = new MultiThreadedNetworkAPIImpl(dataStore, optimizedCompute);
		long optimizedTime = runBenchmark(optimizedEngine, dataStore);

		double ratio = (double) originalTime / optimizedTime;
		double improvementPercent = (ratio - 1.0) * 100.0;

		System.out.println("\nRESULTS");
		System.out.println("Original engine: " + originalTime + " ms");
		System.out.println("Optimized engine: " + optimizedTime + " ms");
		System.out.println("Improvement: " + String.format("%.2f", improvementPercent) + "%");
		System.out.println("Speedup: " + String.format("%.2fx", ratio) + " faster\n");

		assertTrue(ratio >= REQUIRED_IMPROVEMENT,
				"Expected at least 10% faster; got " + String.format("%.2f", improvementPercent) + "%");
	}

	// Run full job submission benchmark
	private long runBenchmark(ComputeEngine engine, TestDataStore dataStore) {
		Delimiters delimiters = new Delimiters(',', ':');

		// Warmup
		for (int i = 0; i < 20; i++) {
			dataStore.resetForJob(i);
			JobSubmission job = new JobSubmission("test-input-" + i, "test-output-" + i, delimiters);
			engine.submitJob(job);
		}

		long start = System.currentTimeMillis();

		// Actual benchmark - submit multiple jobs
		for (int i = 0; i < NUM_JOBS; i++) {
			dataStore.resetForJob(i);
			JobSubmission job = new JobSubmission("test-input-" + i, "test-output-" + i, delimiters);
			engine.submitJob(job);
		}

		long end = System.currentTimeMillis();
		return end - start;
	}

	// In-memory data store for testing
	private static class TestDataStore implements DataStorageAPI {
		private final Map<String, List<Integer>> data = new HashMap<>();

		// Generate test data for each job
		public void resetForJob(int jobId) {
			List<Integer> numbers = new ArrayList<>();
			// Generate realistic range of numbers
			for (int i = 1; i <= 10000; i++) {
				numbers.add((jobId * 1000) + i);
			}
			data.put("test-input-" + jobId, numbers);
		}

		@Override
		public ReadResponse readData(ReadRequest request) {
			List<Integer> numbers = data.getOrDefault(request.getSource(), Arrays.asList(1, 10, 100, 1000, 10000));
			IntegerStream stream = new IntegerStreamImpl(numbers);
			return new ReadResponseImpl(true, stream);
		}

		@Override
		public WriteResponse writeData(WriteRequest request) {
			// Simulate write success
			return new WriteResponseImpl(true);
		}
	}
}