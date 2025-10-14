package apitesting;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import conceptualapi.ComputationApi;
import emptyimplementations.ConceptualAPIImpl;
import emptyimplementations.NetWorkAPIImpl;
import emptyimplementations.ProcessAPIImpl;
import networkapi.ComputeEngine;
import networkapi.JobResponse;
import networkapi.JobSubmission;
import processapi.DataStorageAPI;

public class ExceptionHandlingTest {

	private ComputeEngine computeEngine;

	@TempDir
	Path tempDir;

	@BeforeEach
	public void setUp() {
		DataStorageAPI dataStorage = new ProcessAPIImpl();
		ComputationApi computation = new ConceptualAPIImpl();
		computeEngine = new NetWorkAPIImpl(dataStorage, computation);
	}

	@Test
	public void testJobFailsWithNonExistentInputFile() {
		// Define paths for a non-existent input file and a valid output file.
		Path nonExistentInput = tempDir.resolve("nonexistent/path.txt");
		Path outputFile = tempDir.resolve("output.txt");

		JobSubmission request = new JobSubmission(nonExistentInput.toString(), outputFile.toString());

		JobResponse response = computeEngine.submitJob(request);

		// The exception should be caught and converted to a graceful error response.
		assertNotNull(response);
		assertFalse(response.isSuccess());
		assertTrue(response.getMessage().contains("Failed to read input data"));
	}

	@Test
	public void testJobHandlesLargeNumberInput() throws IOException {
		// Create a temporary input file with a large number.
		Path inputFile = tempDir.resolve("input.txt");
		Path outputFile = tempDir.resolve("output.txt");
		Files.write(inputFile, "999999999".getBytes());

		JobSubmission request = new JobSubmission(inputFile.toString(), outputFile.toString());

		JobResponse response = computeEngine.submitJob(request);

		// The job should complete gracefully without throwing an uncaught exception.
		assertNotNull(response);
		assertTrue(response.isSuccess());
	}

	@Test
	public void testJobFailsWithNullRequest() {
		// Test that a null job submission is handled gracefully.
		JobResponse response = computeEngine.submitJob(null);

		assertNotNull(response);
		assertFalse(response.isSuccess());
		assertTrue(response.getMessage().contains("cannot be null"));
	}

    @Test
    public void testJobFailsWithEmptyInputSource() {
        // Test that an empty input source is handled gracefully.
        Path outputFile = tempDir.resolve("output.txt");
        JobSubmission request = new JobSubmission("", outputFile.toString());

        JobResponse response = computeEngine.submitJob(request);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertTrue(response.getMessage().contains("cannot be null or empty"));
    }
}