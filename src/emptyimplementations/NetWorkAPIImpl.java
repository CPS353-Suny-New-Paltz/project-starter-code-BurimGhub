package emptyimplementations;

import networkapi.ComputeEngine;
import networkapi.JobResponse;
import networkapi.JobSubmission;

public class NetWorkAPIImpl implements ComputeEngine {

    @Override
    public JobResponse submitJob(JobSubmission request) {
        // Method is not implemented yet, return a default failure response.
        return new JobResponseImpl();
    }

    @Override
    public JobResponse getJobStatus(String jobId) {
        // Method is not implemented yet, return a default failure response.
        return new JobResponseImpl();
    }

    @Override
    public JobResponse cancelJob(String jobId) {
        // Method is not implemented yet, return a default failure response.
        return new JobResponseImpl();
    }
}