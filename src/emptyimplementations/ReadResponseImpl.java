package emptyimplementations;

import processapi.IntegerStream;
import processapi.ReadResponse;

public class ReadResponseImpl implements ReadResponse {
    private final boolean success;
    private final IntegerStream stream;

    // Add this constructor
    public ReadResponseImpl(boolean success, IntegerStream stream) {
        this.success = success;
        this.stream = stream;
    }

    @Override
    public boolean isSuccess() {
        return this.success;
    }

    @Override
    public IntegerStream getIntegerStream() {
        return this.stream;
    }
}