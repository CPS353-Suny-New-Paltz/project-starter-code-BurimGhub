package apitesting;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import emptyimplementations.NetWorkAPIImpl;
import networkapi.JobResponse;
import networkapi.JobStatus;
import networkapi.JobSubmission;
import processapi.DataStorageAPI;

public class NetworkAPIImplTest {

    @Mock
    private DataStorageAPI mockDataStorageAPI;

    private NetWorkAPIImpl networkAPI;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        networkAPI = new NetWorkAPIImpl(mockDataStorageAPI);
    }

    @Test
    @Tag("FailingTest") // Tag to exclude this test from the build
    public void testSubmitJob() {
        JobSubmission request = new JobSubmission("input stream", "output stream");
        JobResponse response = networkAPI.submitJob(request);
        assertNotNull(response);
        // The following assertions will fail until the implementation is complete.
        assertTrue(response.isSuccess());
        assertNotNull(response.getJobId());
    }
    @Test
    @Tag("FailingTest") // Tag to exclude this test from the build
    public void testGetJobStatus() {
        String jobId = "test-job-id-123";
        JobResponse response = networkAPI.getJobStatus(jobId);
        // The following assertions will fail until the implementation is complete.
        assertNotNull(response);
        assertEquals(JobStatus.RUNNING, response.getStatus());
    }

    @Test
    @Tag("FailingTest") // Tag to exclude this test from the build
    public void testCancelJob() {
        String jobId = "test-job-id-456";
        JobResponse response = networkAPI.cancelJob(jobId);
        assertNotNull(response);
        // The following assertions will fail until the implementation is complete.
        assertTrue(response.isSuccess());
        assertEquals(JobStatus.CANCELLED, response.getStatus());
    }
}