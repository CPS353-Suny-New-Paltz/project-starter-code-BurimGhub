package emptyimplementations;


import networkapi.ComputeEngine;
import networkapi.JobResponse;
import networkapi.JobStatus;
import networkapi.JobSubmission;
import processapi.DataStorageAPI;

public class ComputeEngineImpl implements ComputeEngine {
    private DataStorageAPI dataStorageAPI;
    
    public ComputeEngineImpl(DataStorageAPI dataStorageAPI) {
        this.dataStorageAPI = dataStorageAPI;
    }
    
    @Override
    public JobResponse submitJob(JobSubmission request) {
        // Return a default failure response for now
        return new JobResponseImpl("", false, "Not implemented yet", JobStatus.FAILED);
    }
    
    @Override
    public JobResponse getJobStatus(String jobId) {
        // Return a default failure response for now
        return new JobResponseImpl(jobId, false, "Not implemented yet", JobStatus.FAILED);
    }
    
    @Override
    public JobResponse cancelJob(String jobId) {
        // Return a default failure response for now
        return new JobResponseImpl(jobId, false, "Not implemented yet", JobStatus.FAILED);
    }
}