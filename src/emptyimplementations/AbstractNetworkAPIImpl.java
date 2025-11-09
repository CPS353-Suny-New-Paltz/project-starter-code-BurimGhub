package emptyimplementations;

import java.util.List;
import java.util.UUID;

import conceptualapi.ComputationApi;
import networkapi.ComputeEngine;
import networkapi.Delimiters;
import networkapi.JobResponse;
import networkapi.JobStatus;
import networkapi.JobSubmission;
import processapi.DataStorageAPI;
import processapi.ReadRequest;
import processapi.ReadResponse;
import processapi.WriteRequest;
import processapi.WriteResponse;

public abstract class AbstractNetworkAPIImpl implements ComputeEngine {
	protected DataStorageAPI dataStore;
	protected ComputationApi computation;

	public AbstractNetworkAPIImpl(DataStorageAPI dataStore, ComputationApi computation) {
		this.dataStore = dataStore;
		this.computation = computation;
	}

	protected abstract List<String> processNumbers(List<Integer> numbers, char separator) throws Exception;

	@Override
	public JobResponse submitJob(JobSubmission request) {
		// Parameter validation checks for null and invalid parameters
	    if (request == null) {
	        String jobId = UUID.randomUUID().toString();
	        return new JobResponseImpl(jobId, false, "Job request cannot be null", JobStatus.FAILED);
	    }
	    
	    if (request.getInputSource() == null || request.getInputSource().trim().isEmpty()) {
	        String jobId = UUID.randomUUID().toString();
	        return new JobResponseImpl(jobId, false, "Input source cannot be null or empty", JobStatus.FAILED);
	    }
	    
	    if (request.getOutputSource() == null || request.getOutputSource().trim().isEmpty()) {
	        String jobId = UUID.randomUUID().toString();
	        return new JobResponseImpl(jobId, false, "Output source cannot be null or empty", JobStatus.FAILED);
	    }
	    
		
		try {
			String jobId = UUID.randomUUID().toString();

			// Read data from input source
			ReadRequest readRequest = new ReadRequest(request.getInputSource());
			ReadResponse readResponse = dataStore.readData(readRequest);
			// Validate read response
			if (!readResponse.isSuccess()) {
				// Return failure response if read fails
				return new JobResponseImpl(jobId, false, "Failed to read input data", JobStatus.FAILED);
			}

			// Process each number through computation
			List<Integer> numbers = readResponse.getIntegerStream().getNumbers();

			// Get the delimiters object from the request.
			Delimiters delimiters = request.getDelimiters();
			char separator = delimiters.getKeyValueSeparator();

			// Process numbers (single-threaded or multi-threaded)
			List<String> results = processNumbers(numbers, separator);

			// Write results to output
			WriteRequest writeRequest = new WriteRequest(request.getOutputSource(), results);
			WriteResponse writeResponse = dataStore.writeData(writeRequest);
			// Validate write response
			if (!writeResponse.isSuccess()) {
				return new JobResponseImpl(jobId, false, "Failed to write output data", JobStatus.FAILED);
			}
			// Return successful job response
			return new JobResponseImpl(jobId, true, "Job completed successfully", JobStatus.COMPLETED);

		} catch (Exception e) {
			// Handle exceptions and return failure response
			String jobId = UUID.randomUUID().toString();
			return new JobResponseImpl(jobId, false, "Job execution failed: " + e.getMessage(), JobStatus.FAILED);
		}
	}

    @Override
    public JobResponse getJobStatus(String jobId) {
        // parameter validation
        if (jobId == null || jobId.trim().isEmpty()) {
            return new JobResponseImpl("", false, "Job ID cannot be null or empty", JobStatus.FAILED);
        }

        // Exception handling
        try {
            // All non-empty string values are valid for job ID lookup
            return new JobResponseImpl(jobId, true, "Job status retrieved", JobStatus.COMPLETED);
        } catch (Exception e) {
            // Handle unexpected errors during status lookup
            return new JobResponseImpl(jobId, false, "Failed to retrieve job status: " + e.getMessage(), 
                    JobStatus.FAILED);
        }
    }

    @Override
    public JobResponse cancelJob(String jobId) {
        // parameter validation
        if (jobId == null || jobId.trim().isEmpty()) {
            return new JobResponseImpl("", false, "Job ID cannot be null or empty", JobStatus.FAILED);
        }

        // exception handling
        try {
            // All non-empty string values are valid for job ID cancellation
            return new JobResponseImpl(jobId, true, "Job cancelled successfully", JobStatus.CANCELLED);
        } catch (Exception e) {
            // Handle unexpected errors during cancellation
            return new JobResponseImpl(jobId, false, "Failed to cancel job: " + e.getMessage(), 
                    JobStatus.FAILED);
        }
    }
}
