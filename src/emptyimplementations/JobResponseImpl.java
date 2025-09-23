package emptyimplementations;

import networkapi.JobResponse;
import networkapi.JobStatus;

public class JobResponseImpl implements JobResponse {
	// All methods return default values indication failure or null.
    @Override
    public String getJobId() {
        return null;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public String getMessage() {
        return null;
    }

    @Override
    public JobStatus getStatus() {
        return null;
    }

}