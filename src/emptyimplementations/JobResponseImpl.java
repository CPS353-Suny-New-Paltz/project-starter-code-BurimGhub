package emptyimplementations;

import networkapi.JobResponse;
import networkapi.JobStatus;

public class JobResponseImpl implements JobResponse {
    private String jobId;
    private boolean success;
    private String message;
    private JobStatus status;
    
    public JobResponseImpl(String jobId, boolean success, String message, JobStatus status) {
        this.jobId = jobId;
        this.success = success;
        this.message = message;
        this.status = status;
    }
    
    @Override
    public String getJobId() {
        return jobId;
    }
    
    @Override
    public boolean isSuccess() {
        return success;
    }
    
    @Override
    public String getMessage() {
        return message;
    }
    
    @Override
    public JobStatus getStatus() {
        return status;
    }
}