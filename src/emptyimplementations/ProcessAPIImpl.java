package emptyimplementations;

import java.util.Collections;

import processapi.DataStorageAPI;
import processapi.IntegerStream;
import processapi.ReadRequest;
import processapi.ReadResponse;
import processapi.WriteRequest;
import processapi.WriteResponse;

/**
 * An empty implementation of the DataStorageAPI.
 * This class handles reading from and writing to a data source,
 * but in this skeleton form, it always returns a failure response.
 */
public class ProcessAPIImpl implements DataStorageAPI {

    /**
     * Reads data from a source.
     * In this empty implementation, it does not perform any I/O and
     * immediately returns a response indicating failure.
     *
     * @param request The request specifying the data source.
     * @return A default ReadResponse indicating failure.
     */
    @Override
    public ReadResponse readData(ReadRequest request) {
        // Return a default failure response with an empty stream to avoid errors.
        IntegerStream emptyStream = new IntegerStreamImpl();
        return new ReadResponseImpl();
    }

    /**
     * Writes data to a destination.
     * In this empty implementation, it does not perform any I/O and
     * immediately returns a response indicating failure.
     *
     * @param request The request specifying the data and destination.
     * @return A default WriteResponse indicating failure.
     */
    @Override
    public WriteResponse writeData(WriteRequest request) {
        // Return a default failure response as the logic is not implemented.
        return new WriteResponseImpl();
    }
}