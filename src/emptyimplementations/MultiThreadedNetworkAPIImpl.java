package emptyimplementations;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import conceptualapi.ComputationApi;
import conceptualapi.ComputeRequest;
import conceptualapi.ComputeResponse;
import processapi.DataStorageAPI;

public class MultiThreadedNetworkAPIImpl extends AbstractNetworkAPIImpl {
	private final ExecutorService executorService;
	private static final int MAX_THREADS = 8;

	public MultiThreadedNetworkAPIImpl(DataStorageAPI dataStore, ComputationApi computation) {
		super(dataStore, computation);
		
		// Create fixed thread pool use min of MAX_THREADS and available processors
		int threadCount = Math.min(MAX_THREADS, Runtime.getRuntime().availableProcessors());
		this.executorService = Executors.newFixedThreadPool(threadCount);
	}

	@Override
	protected List<String> processNumbers(List<Integer> numbers, char separator) throws Exception {
		// MULTI-THREADED PROCESSING Submit all tasks to thread pool
		List<Future<String>> futures = new ArrayList<>();
		
		for (Integer number : numbers) {
			Future<String> future = executorService.submit(() -> {
				try {
					ComputeRequest computeRequest = new ComputeRequest(number);
					ComputeResponse computeResponse = computation.compute(computeRequest);

					if (computeResponse.isSuccess()) {
						return number + String.valueOf(separator) + computeResponse.getResult();
					} else {
						throw new RuntimeException("Computation failed for number: " + number);
					}
				} catch (Exception e) {
					throw new RuntimeException("Error processing number " + number + ": " + e.getMessage());
				}
			});
			
			futures.add(future);
		}

		// Collect results in order 
		List<String> results = new ArrayList<>();
		for (Future<String> future : futures) {
			try {
				results.add(future.get(30, TimeUnit.SECONDS)); 
			} catch (TimeoutException e) {
				throw new Exception("Computation timed out");
			} catch (ExecutionException e) {
				throw new Exception("Computation error: " + e.getCause().getMessage());
			}
		}
		
		return results;
	}

	public void shutdown() {
		executorService.shutdown();
		try {
			if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
				executorService.shutdownNow();
			}
		} catch (InterruptedException e) {
			executorService.shutdownNow();
			Thread.currentThread().interrupt();
		}
	}
}