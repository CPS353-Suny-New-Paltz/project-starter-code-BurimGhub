package apitesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import conceptualapi.ComputationResultCode;
import conceptualapi.ComputeRequest;
import conceptualapi.ComputeResponse;
import emptyimplementations.ConceptualAPIImpl;
import emptyimplementations.NetWorkAPIImpl;
import emptyimplementations.ProcessAPIImpl;
import networkapi.JobResponse;
import networkapi.JobStatus;
import networkapi.JobSubmission;
import processapi.ReadRequest;
import processapi.ReadResponse;
import processapi.WriteRequest;
import processapi.WriteResponse;

public class ValidationLogicTest {

	private ConceptualAPIImpl conceptualAPI;
	private NetWorkAPIImpl networkAPI;
	private ProcessAPIImpl processAPI;

	@BeforeEach
	public void setUp() {
		conceptualAPI = new ConceptualAPIImpl();
		networkAPI = new NetWorkAPIImpl(null, null);
		processAPI = new ProcessAPIImpl();
	}

	@Test
	public void testConceptualAPIValidation() {
		// Test null request
		ComputeResponse response = conceptualAPI.compute(null);
		assertNotNull(response);
		assertFalse(response.isSuccess());
		assertEquals(ComputationResultCode.INVALID_INPUT, response.getResultCode());

		// Test negative number
		// ComputeRequest constructor might throw an exception for <= 0.
		try {
			// This line might throw an exception
			ComputeRequest zeroRequest = new ComputeRequest(0); 
			ComputeResponse zeroResponse = conceptualAPI.compute(zeroRequest);
			assertNotNull(zeroResponse);
			assertFalse(zeroResponse.isSuccess());
			assertEquals(ComputationResultCode.INVALID_INPUT, zeroResponse.getResultCode());
		} catch (IllegalArgumentException e) {
			// This is also a valid outcome if the constructor performs the validation.
			assertTrue(e.getMessage().contains("must be positive"));
		}
	}

	@Test
	public void testNetworkAPIValidation() {
		// Test null job submission
		JobResponse response = networkAPI.submitJob(null);
		assertNotNull(response);
		assertFalse(response.isSuccess());
		assertEquals(JobStatus.FAILED, response.getStatus());
		assertTrue(response.getMessage().contains("cannot be null"));

		// Test null job ID for status check
		JobResponse statusResponse = networkAPI.getJobStatus(null);
		assertNotNull(statusResponse);
		assertFalse(statusResponse.isSuccess());
		assertTrue(statusResponse.getMessage().contains("cannot be null"));

		// Test empty job ID for cancellation
		JobResponse cancelResponse = networkAPI.cancelJob("");
		assertNotNull(cancelResponse);
		assertFalse(cancelResponse.isSuccess());
		assertTrue(cancelResponse.getMessage().contains("cannot be null or empty"));

		// Test empty input source
		JobSubmission emptyInput = new JobSubmission("", "Output.txt");
		JobResponse emptyInputResponse = networkAPI.submitJob(emptyInput);
		assertFalse(emptyInputResponse.isSuccess());
		assertTrue(emptyInputResponse.getMessage().contains("Input source cannot be null or empty"));

		// Test null output source
		JobSubmission nullOutput = new JobSubmission("Input.txt", null);
		JobResponse nullOutputResponse = networkAPI.submitJob(nullOutput);
		assertFalse(nullOutputResponse.isSuccess());
		assertTrue(nullOutputResponse.getMessage().contains("Output source cannot be null or empty"));

	}

	@Test
	public void testProcessAPIValidation() {
		// Test null read request
		ReadResponse readResponse = processAPI.readData(null);
		assertNotNull(readResponse);
		assertFalse(readResponse.isSuccess());
		assertNull(readResponse.getIntegerStream());

		// Test empty source path
		ReadRequest emptyRequest = new ReadRequest("");
		ReadResponse emptyResponse = processAPI.readData(emptyRequest);
		assertNotNull(emptyResponse);
		assertFalse(emptyResponse.isSuccess());

		// Test null write request
		WriteResponse writeResponse = processAPI.writeData(null);
		assertNotNull(writeResponse);
		assertFalse(writeResponse.isSuccess());

		// Test empty source path
		ReadRequest emptyPath = new ReadRequest("");
		ReadResponse emptyPathResponse = processAPI.readData(emptyPath);
		assertFalse(emptyPathResponse.isSuccess());

		// Test empty destination
		WriteRequest emptyDestRequest = new WriteRequest("", Arrays.asList("result"));
		WriteResponse emptyDestResponse = processAPI.writeData(emptyDestRequest);
		assertFalse(emptyDestResponse.isSuccess());

		// Test null results list
		WriteRequest nullResultsRequest = new WriteRequest("Output.txt", null);
		WriteResponse nullResultsResponse = processAPI.writeData(nullResultsRequest);
		assertFalse(nullResultsResponse.isSuccess());
	}
}
