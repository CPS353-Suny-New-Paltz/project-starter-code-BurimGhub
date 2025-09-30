package emptyimplementations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import conceptualapi.ComputationApi;
import conceptualapi.ComputeRequest;
import conceptualapi.ComputeResponse;
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

// Implementation of the ComputeEngine interface with unimplemented methods.
public class NetWorkAPIImpl implements ComputeEngine {
	private DataStorageAPI dataStore;
    private ComputationApi computation;

    public NetWorkAPIImpl(DataStorageAPI dataStore, ComputationApi computation) {
        this.dataStore = dataStore;
        this.computation = computation;
    }

    @Override
    public JobResponse submitJob(JobSubmission request) {
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
            // Prepare list to hold results
            List<String> results = new ArrayList<>();

            // Get the delimiters object from the request.
            Delimiters delimiters = request.getDelimiters();
            char separator = delimiters.getKeyValueSeparator();

            // Compute each number
            for (Integer number : numbers) {
                ComputeRequest computeRequest = new ComputeRequest(number);
                ComputeResponse computeResponse = computation.compute(computeRequest);

                if (computeResponse.isSuccess()) {
                    // Delimiters Logic
                    results.add(number + String.valueOf(separator) + computeResponse.getResult());
                } else {
                    return new JobResponseImpl(jobId, false, "Computation failed for number: " + number,
                            JobStatus.FAILED);
                }
            }

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
        // In a real implementation, job status would be tracked and retrieved
        return new JobResponseImpl(jobId, true, "Job status retrieved", JobStatus.COMPLETED);
    }

    @Override
    public JobResponse cancelJob(String jobId) {
        // In a real implementation, job cancellation logic would be applied
        return new JobResponseImpl(jobId, true, "Job cancelled", JobStatus.CANCELLED);
    }
}