package infrastructure;

import processapi.DataStorageAPI;
import processapi.ReadRequest;
import processapi.ReadResponse;
import processapi.WriteRequest;
import processapi.WriteResponse;

import java.util.List;

import emptyimplementations.ReadResponseImpl;
import emptyimplementations.WriteResponseImpl;

public class InMemoryDataStorageAPI implements DataStorageAPI {
    private InMemoryInputConfig inputConfig;
    private InMemoryOutputConfig outputConfig;
    
    public InMemoryDataStorageAPI(InMemoryInputConfig inputConfig, InMemoryOutputConfig outputConfig) {
        this.inputConfig = inputConfig;
        this.outputConfig = outputConfig;
    }
    
    @Override
    public ReadResponse readData(ReadRequest request) {
        try {
            // For testing, we ignore the source in the request and read from our input config
            List<Integer> inputData = inputConfig.getInputData();
            InMemoryIntegerStream stream = new InMemoryIntegerStream(inputData);
            return new ReadResponseImpl(true, stream);
        } catch (Exception e) {
            return new ReadResponseImpl(false, null);
        }
    }
    
    @Override
    public WriteResponse writeData(WriteRequest request) {
        try {
            // For testing, we ignore the destination and write to our output config
            List<String> results = request.getResults();
            outputConfig.addAllOutputs(results);
            return new WriteResponseImpl(true);
        } catch (Exception e) {
            return new WriteResponseImpl(false);
        }
    }
}