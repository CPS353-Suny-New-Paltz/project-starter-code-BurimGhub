package project.checkpointtests;

import networkapi.ComputeEngine;
import networkapi.Delimiters;
import networkapi.JobResponse;
import networkapi.JobSubmission;
import conceptualapi.ComputationApi;
import emptyimplementations.ConceptualAPIImpl;
import emptyimplementations.NetWorkAPIImpl;
import emptyimplementations.ProcessAPIImpl;
import processapi.DataStorageAPI;

public class ManualTestingFramework {

    public static final String INPUT = "Input.txt";
    public static final String OUTPUT = "Output.txt";

    public static void main(String[] args) {
        // Instantiate real implementations of all 3 APIs
        DataStorageAPI dataStorage = new ProcessAPIImpl();
        // File-based I/O with Scanner
        ComputationApi computation = new ConceptualAPIImpl(); 
        // Number-to-words conversion
        ComputeEngine engine = new NetWorkAPIImpl(dataStorage, computation);

        // Run the computation
        Delimiters delimiters = new Delimiters(',', ':');
        // TODO 2: Run computation with specified files and comma delimiter
        JobSubmission jobRequest = new JobSubmission(INPUT, OUTPUT, delimiters);

        JobResponse response = engine.submitJob(jobRequest);

        if (response.isSuccess()) {
            System.out.println("Job completed successfully: " + response.getMessage());
        } else {
            System.err.println("Job failed: " + response.getMessage());
        }
    }
}