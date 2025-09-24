package emptyimplementations;

import processapi.DataStorageAPI;
import processapi.IntegerStream;
import processapi.ReadRequest;
import processapi.ReadResponse;
import processapi.WriteRequest;
import processapi.WriteResponse;

/*
 An empty implementation of the DataStorageAPI 
 This class handles reading from and writing to a data source,
*/
public class ProcessAPIImpl implements DataStorageAPI {

    @Override
    public ReadResponse readData(ReadRequest request) {
        // Create a default empty stream to avoid null errors
        IntegerStream emptyStream = new IntegerStreamImpl();
        
        // Use the new constructor to provide a failure status
        return new ReadResponseImpl(false, emptyStream);
    }

    @Override
    public WriteResponse writeData(WriteRequest request) {
        // Use the new constructor to provide a failure status
        return new WriteResponseImpl(false);
    }
}