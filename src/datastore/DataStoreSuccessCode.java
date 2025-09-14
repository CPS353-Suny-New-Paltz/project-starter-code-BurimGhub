package datastore;

public enum DataStoreSuccessCode {
    SUCCESS(true),
    FAILED(false),
    SERVER_NOT_AVAILABLE(false),
    SUCCESS_WITH_ERRORS(true);

    private final boolean success;

    private DataStoreSuccessCode(boolean success) {
        this.success = success;
    }
    
    public boolean success() {
        return success;
    }
}
