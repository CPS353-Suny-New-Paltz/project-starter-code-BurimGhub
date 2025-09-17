package networkapi;
import project.annotations.NetworkAPI;

/**
 * The ComputeEngine is used to run jobs, check their status,
 * and cancels jobs if needed
 * 
 * @author Burim Gashi
 * 
 * Status: Working
 */
	@NetworkAPI
public interface ComputeEngine {
	
	/**
	 * Starts a new job using request
	 * 
	 * @param request info about the job to run
	 * @return a JobResponse with the job ID and result info
	 */
	JobResponse submitJob(JobSubmission request);
	
	/**
	 * Checks the status of a job by its Id
	 * @param jobId the ID of the job
	 * @return a JobResponse with the current status
	 */
	JobResponse getJobStatus(String jobId);
	
	/**
	 * Tries to stop a job from running
	 * 
	 * @param jobId the ID of the job to cancel
	 * @return a JobResponse with the result of the cancel request
	 */
	JobResponse cancelJob(String jobId);
}
