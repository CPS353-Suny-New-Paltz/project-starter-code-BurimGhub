package emptyimplementations;

import networkapi.JobResponse;
import networkapi.JobStatus;

public class JobResponseImpl implements JobResponse {
    public JobResponseImpl() {

    }
    @Override
    public String getJobId() {
        return ""; // Return empty string instead of null
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public String getMessage() {
        return "Operation failed."; // Return default message instead of null
    }

    @Override
    public JobStatus getStatus() {
        return JobStatus.FAILED; // Return a default status instead of null
    }
}