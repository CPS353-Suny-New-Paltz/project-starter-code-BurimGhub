package emptyimplementations;

import conceptualapi.ComputationApi;
import conceptualapi.ComputationResultCode;
import conceptualapi.ComputeRequest;
import conceptualapi.ComputeResponse;

// An empty implementation of the ComputationAPI.
public class ConceptualAPIImpl implements ComputationApi {

    @Override
    public ComputeResponse compute(ComputeRequest request) {
        // Return a default error response as the logic is not yet implemented.
        return new ComputeResponse(ComputationResultCode.ERROR);
    }
}