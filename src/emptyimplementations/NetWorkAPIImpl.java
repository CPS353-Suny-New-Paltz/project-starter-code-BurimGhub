package emptyimplementations;

import conceptualapi.ComputationApi;
import networkapi.ComputeEngine;
import networkapi.JobResponse;
import networkapi.JobSubmission;
import processapi.DataStorageAPI;

// Implementation of the ComputeEngine interface with unimplemented methods.
public class NetWorkAPIImpl implements ComputeEngine {
    // Dependencies on DataStorageAPI and ComputationAPI
    private DataStorageAPI dataStore;
    private ComputationApi computation;

    public NetWorkAPIImpl(DataStorageAPI dataStore, ComputationApi computation) {
        this.dataStore = dataStore;
        this.computation = computation;
    }

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