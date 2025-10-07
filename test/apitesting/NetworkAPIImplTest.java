package apitesting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import conceptualapi.ComputationApi;
import conceptualapi.ComputeRequest;
import conceptualapi.ComputeResponse;
import emptyimplementations.NetWorkAPIImpl;
import networkapi.JobResponse;
import networkapi.JobStatus;
import networkapi.JobSubmission;
import processapi.DataStorageAPI;
import processapi.IntegerStream;
import processapi.ReadRequest;
import processapi.ReadResponse;
import processapi.WriteRequest;
import processapi.WriteResponse;

public class NetworkAPIImplTest {

    @Mock
    private DataStorageAPI mockDataStorageAPI;
    
    @Mock
    private ComputationApi mockComputationApi;

    private NetWorkAPIImpl networkAPI;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        networkAPI = new NetWorkAPIImpl(mockDataStorageAPI, mockComputationApi);
    }

    @Test
    void testSubmitJob() {
        ReadResponse mockReadResponse = mock(ReadResponse.class);
        IntegerStream mockStream = mock(IntegerStream.class);
        when(mockReadResponse.isSuccess()).thenReturn(true);
        when(mockReadResponse.getIntegerStream()).thenReturn(mockStream);
        when(mockStream.getNumbers()).thenReturn(Arrays.asList(1, 10, 25));
        when(mockDataStorageAPI.readData(any(ReadRequest.class))).thenReturn(mockReadResponse);
        
        // Mock the computation API response
        ComputeResponse mockComputeResponse = mock(ComputeResponse.class);
        when(mockComputeResponse.isSuccess()).thenReturn(true);
        when(mockComputeResponse.getResult()).thenReturn("test-result");
        when(mockComputationApi.compute(any(ComputeRequest.class))).thenReturn(mockComputeResponse);
        
        // Mock the data storage write response
        WriteResponse mockWriteResponse = mock(WriteResponse.class);
        when(mockWriteResponse.isSuccess()).thenReturn(true);
        when(mockDataStorageAPI.writeData(any(WriteRequest.class))).thenReturn(mockWriteResponse);
        
        // Now test the submitJob method
        JobSubmission request = new JobSubmission("input stream", "output stream");
        JobResponse response = networkAPI.submitJob(request);

        // Verify the final result.
        assertTrue(response.isSuccess());
        assertNotNull(response.getJobId());
        assertEquals(JobStatus.COMPLETED, response.getStatus());
    }
    @Test
    public void testGetJobStatus() {
        String jobId = "test-job-id-123";
        JobResponse response = networkAPI.getJobStatus(jobId);
        // The following assertions will fail until the implementation is complete.
        assertNotNull(response);
        assertEquals(JobStatus.COMPLETED, response.getStatus());
    }

    @Test
    public void testCancelJob() {
        String jobId = "test-job-id-456";
        JobResponse response = networkAPI.cancelJob(jobId);
        assertNotNull(response);
        // The following assertions will fail until the implementation is complete.
        assertTrue(response.isSuccess());
        assertEquals(JobStatus.CANCELLED, response.getStatus());
    }
}