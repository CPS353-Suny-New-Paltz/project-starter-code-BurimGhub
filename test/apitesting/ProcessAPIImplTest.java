package apitesting;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import emptyimplementations.ProcessAPIImpl;
import processapi.ReadRequest;
import processapi.ReadResponse;
import processapi.WriteRequest;
import processapi.WriteResponse;

import java.util.Arrays;
import java.util.List;

public class ProcessAPIImplTest {

	private ProcessAPIImpl processAPI;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		processAPI = new ProcessAPIImpl();
	}

	@Test
	@Tag("FailingTest")
	public void testReadData() {
		// Create a read request with a valid source
		ReadRequest readRequest = new ReadRequest("file:///data/input.csv");
		// Read data using the storage API
		ReadResponse readResponse = processAPI.readData(readRequest);
		// Verify the response from the empty implementation
		assertNotNull(readResponse);
		// The following assertions will fail until the implementation is complete.
		assertTrue(readResponse.isSuccess());
		assertNotNull(readResponse.getIntegerStream());
	}

	@Test
	@Tag("FailingTest")
	public void testWriteData() {
		// Arrange: Create some formatted results and a write request
		List<String> formattedResults = Arrays.asList("1:one", "21:twenty-one", "105:one hundred five");
		WriteRequest writeRequest = new WriteRequest("file:///data/output.txt", formattedResults);
		// Write data using the storage API
		WriteResponse writeResponse = processAPI.writeData(writeRequest);
		// Verifying the response
		assertNotNull(writeResponse);
		// This assertion will fail until the implementation is complete.
		assertTrue(writeResponse.isSuccess());
	}

	@Test
	@Tag("FailingTest")
	public void testReadDataWithInvalidSource() {
		// Create a read request with an invalid (empty) source
		ReadRequest readRequest = new ReadRequest("");
		// Attempt to read data
		ReadResponse readResponse = processAPI.readData(readRequest);
		// Verify that the operation fails gracefully
		assertNotNull(readResponse);
		// This should pass, as the empty implementation returns a failure state.
		assertFalse(readResponse.isSuccess());
	}
}