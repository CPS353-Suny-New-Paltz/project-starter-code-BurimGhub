package testHarness;

import java.io.File;

import networkapi.ComputeEngine;
import networkapi.Delimiters;
import networkapi.JobResponse;
import networkapi.JobSubmission;

public class TestUser {
    
    private final ComputeEngine coordinator;

    public TestUser(ComputeEngine coordinator) {
        this.coordinator = coordinator;
    }

    public void run(String outputPath) {
        char delimiter = ';';
        String inputPath = "test" + File.separatorChar + "testInputFile.test";
        
        try {
            // Create delimiters with semicolon as key-value separator
            Delimiters delimiters = new Delimiters(',', delimiter);
            
            // Create job submission request
            JobSubmission request = new JobSubmission(inputPath, outputPath, delimiters);
            
            // Submit the job
            JobResponse response = coordinator.submitJob(request);
            
            if (!response.isSuccess()) {
                throw new RuntimeException("Job failed: " + response.getMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to process job: " + e.getMessage(), e);
        }
    }
}