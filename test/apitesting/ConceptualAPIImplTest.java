package apitesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import conceptualapi.ComputationResultCode;
import conceptualapi.ComputeRequest;
import conceptualapi.ComputeResponse;
import emptyimplementations.ConceptualAPIImpl;

public class ConceptualAPIImplTest {

	private ConceptualAPIImpl conceptualAPI;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		conceptualAPI = new ConceptualAPIImpl();
	}

	@Test
	@Tag("FailingTest")
	public void testCompute() {
		// Create a compute request
		int numberToCompute = 12345;
		ComputeRequest request = new ComputeRequest(numberToCompute);

		ComputeResponse response = conceptualAPI.compute(request);

		// Verify response (will fail until implementation is complete)
		assertNotNull(response);
		// These assertions will fail with current empty implementation
		assertTrue(response.isSuccess());
		assertNotNull(response.getResult());
	}

	@Test
	@Tag("FailingTest")
	public void testComputeWithValidNumber() {
		// Test with a valid number
		ComputeRequest request = new ComputeRequest(1);

		ComputeResponse response = conceptualAPI.compute(request);

		// Verify response
		assertNotNull(response);
		// This will fail with current empty implementation
		assertTrue(response.isSuccess());
		assertEquals(ComputationResultCode.SUCCESS, response.getResultCode());
	}

	@Test // No tag needed because this test is expected to pass
    public void testComputeWithInvalidNumber() {
        // This test asserts that creating a ComputeRequest
        // with an invalid number throws an IllegalArgumentException.
        assertThrows(IllegalArgumentException.class, () -> {
            new ComputeRequest(-1);
        });
    }

	@Test
	@Tag("FailingTest")
	public void testComputeWithLargeNumber() {
		// Test with a large number
		ComputeRequest request = new ComputeRequest(1000000);

		ComputeResponse response = conceptualAPI.compute(request);

		// Verify response
		assertNotNull(response);
		// This will fail with current empty implementation
		assertTrue(response.isSuccess());
	}

}