package emptyimplementations;

import processapi.WriteResponse;

public class WriteResponseImpl implements WriteResponse {
    private final boolean success;

    // Add this constructor
    public WriteResponseImpl(boolean success) {
        this.success = success;
    }

    @Override
    public boolean isSuccess() {
        return this.success;
    }
}