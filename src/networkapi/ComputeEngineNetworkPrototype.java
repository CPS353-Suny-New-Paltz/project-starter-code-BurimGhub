package networkapi;
import project.annotations.NetworkAPIPrototype;
/**
 * This is a prototype of the Compute Engine Network API
 * 
 * @author Burim Gashi
 * 
 * Status: Working
 */

	
public class ComputeEngineNetworkPrototype {

	@NetworkAPIPrototype
    public void prototype(ComputeEngine engine) {
        // Create custom delimiters for separating data pairs and key-value pairs
        Delimiters customDelimiters = new Delimiters(',', ':');

        // Create a job submission request with input, output, and custom delimiters
        JobSubmission request = new JobSubmission("input stream", "output stream", customDelimiters);

        // Submit the job to the compute engine
        JobResponse response = engine.submitJob(request);

        // Check if the job submission was successful
        if (!response.isSuccess()) {
            System.out.println("Job submission failed: " + response.getMessage());
            return; // Exit if submission failed
        }

        // Print success message with job ID
        System.out.println("Job submitted successfully with ID: " + response.getJobId());

        // Check the job status
        JobResponse statusResponse = engine.getJobStatus(response.getJobId());
        System.out.println("Job status: " + statusResponse.getStatus());
        System.out.println("Message: " + statusResponse.getMessage());
    }
}