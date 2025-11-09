package emptyimplementations;

import java.util.ArrayList;
import java.util.List;

import conceptualapi.ComputeRequest;
import conceptualapi.ComputeResponse;
import processapi.DataStorageAPI;
import conceptualapi.ComputationApi;

public class NetWorkAPIImpl extends AbstractNetworkAPIImpl {

    public NetWorkAPIImpl(DataStorageAPI dataStore, ComputationApi computation) {
        super(dataStore, computation);
    }

    @Override
    protected List<String> processNumbers(List<Integer> numbers, char separator) throws Exception {
        // Prepare list to hold results
        List<String> results = new ArrayList<>();

        // Compute each number (SINGLE-THREADED)
        for (Integer number : numbers) {
            ComputeRequest computeRequest = new ComputeRequest(number);
            ComputeResponse computeResponse = computation.compute(computeRequest);

            if (computeResponse.isSuccess()) {
                // Delimiters Logic
                results.add(number + String.valueOf(separator) + computeResponse.getResult());
            } else {
                throw new Exception("Computation failed for number: " + number);
            }
        }
        
        return results;
    }
}