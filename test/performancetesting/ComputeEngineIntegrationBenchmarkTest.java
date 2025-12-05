package performancetesting;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import conceptualapi.ComputationApi;
import emptyimplementations.ConceptualAPIImpl;
import emptyimplementations.IntegerStreamImpl;
import emptyimplementations.MultiThreadedNetworkAPIImpl;
import emptyimplementations.OptimizedConceptualAPIImpl;
import emptyimplementations.ReadResponseImpl;
import emptyimplementations.WriteResponseImpl;
import networkapi.ComputeEngine;
import networkapi.Delimiters;
import networkapi.JobSubmission;
import processapi.DataStorageAPI;
import processapi.IntegerStream;
import processapi.ReadRequest;
import processapi.ReadResponse;
import processapi.WriteRequest;
import processapi.WriteResponse;

public class ComputeEngineIntegrationBenchmarkTest {

	private static final int NUM_JOBS = 100;
	private static final double REQUIRED_IMPROVEMENT = 1.10;

	@Test
	void optimizedEngineTest() {

		// Setup Data Store and Pre-load Data
		// We load data NOW so we don't measure data generation time later
		TestDataStore dataStore = new TestDataStore();
		dataStore.generateData(NUM_JOBS);

		// Test with Original (Slow) Implementation
		ComputationApi originalCompute = new ConceptualAPIImpl();
		ComputeEngine originalEngine = new MultiThreadedNetworkAPIImpl(dataStore, originalCompute);
		long originalTime = runBenchmark(originalEngine);

		// Test with Optimized (Fast) Implementation
		ComputationApi optimizedCompute = new OptimizedConceptualAPIImpl();
		ComputeEngine optimizedEngine = new MultiThreadedNetworkAPIImpl(dataStore, optimizedCompute);
		long optimizedTime = runBenchmark(optimizedEngine);

		// Calculate Results
		double ratio = (double) originalTime / optimizedTime;
		double improvementPercent = (ratio - 1.0) * 100.0;

		System.out.println("\nRESULTS");
		System.out.println("Original engine:" + originalTime + " ms");
		System.out.println("Optimized engine:" + optimizedTime + " ms");
		System.out.println("Improvement:" + String.format("%.2f", improvementPercent) + "%");
		System.out.println("Speedup:" + String.format("%.2fx", ratio) + " faster\n");

		assertTrue(ratio >= REQUIRED_IMPROVEMENT,
				"Expected at least 10% faster; got " + String.format("%.2f", improvementPercent) + "%");
	}

	private long runBenchmark(ComputeEngine engine) {
		Delimiters delimiters = new Delimiters(',', ':');

		// We run a few small jobs to get the  compiler working
		for (int i = 0; i < 100; i++) {
			JobSubmission job = new JobSubmission("test-input-" + i, "test-output-" + i, delimiters);
			engine.submitJob(job);
		}

		long start = System.currentTimeMillis();

		// ACTUAL MEASUREMENT
		for (int i = 0; i < NUM_JOBS; i++) {
			JobSubmission job = new JobSubmission("testinput-" + i, "testoutput-" + i, delimiters);
			engine.submitJob(job);
		}

		long end = System.currentTimeMillis();
		return end - start;
	}

	// In-memory data store for testing
	private static class TestDataStore implements DataStorageAPI {
		private final Map<String, List<Integer>> data = new HashMap<>();

		// Generate data once at the start
		public void generateData(int totalJobs) {
			for (int jobId = 0; jobId < totalJobs; jobId++) {
				List<Integer> numbers = new ArrayList<>();
				for (int i = 1; i <= 10000; i++) {
					numbers.add((jobId * 10) + i);
				}
				data.put("testinput-" + jobId, numbers);
			}
		}

		@Override
		public ReadResponse readData(ReadRequest request) {
			// Updated to use ReadRequest structure with delimiter
			List<Integer> numbers = data.getOrDefault(request.getSource(), Arrays.asList(1, 2, 3));
			IntegerStream stream = new IntegerStreamImpl(numbers);
			// Assuming ReadResponseImpl constructor
			return new ReadResponseImpl(true, stream);
		}

		@Override
		public WriteResponse writeData(WriteRequest request) {
			return new WriteResponseImpl(true);
		}
	}
}