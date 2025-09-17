package networkapi;

/**
 * This enum shows what state a job is in
 * 
 * @author Burim Gashi
 * 
 * Status: Working
 */
public enum JobStatus {
	// States that the job could be in
	PENDING(false),
	RUNNING(false),
	COMPLETED(true),
	FAILED(true),
	CANCELLED(true);
	
	public boolean check;

	JobStatus(boolean check) {
		this.check = check;
	}
	
	/**
	 * Returns true if the job is done running
	 */
	public boolean check() {
		return check;
	}
}
