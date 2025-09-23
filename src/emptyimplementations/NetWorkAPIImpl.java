package emptyimplementations;

import networkapi.ComputeEngine;
import networkapi.JobResponse;
import networkapi.JobSubmission;
import processapi.DataStorageAPI;

public class NetWorkAPIImpl implements ComputeEngine {
	// Reference to the DataStorageAPI, not used in current implementation.
   private  DataStorageAPI dataStore;

   public NetWorkAPIImpl(DataStorageAPI dataStore) {
       this.dataStore = dataStore;
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