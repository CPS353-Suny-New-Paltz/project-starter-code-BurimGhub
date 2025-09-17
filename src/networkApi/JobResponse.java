package networkAPI;

/**
 * This interface is used to give information about what happened
 * when a job ran.
 * 
 * @author Burim Gashi
 * 
 * Status: Working
 */
public interface JobResponse {
	
	// Gets the job ID
	String getJobId();
	
	// Tells if the job is successful
	boolean isSuccess();
	
	// Gives a message about what happened
	String getMessage();
	
	// Gets the job's current status;
	JobStatus getStatus();
	

}
