package datastore;

public class DataRequest {
    
    private ColorWheelOption option;

    // String of data
    private byte[] dataRequest;

    public DataRequest(byte[] dataRequest) {
        this.dataRequest = dataRequest;
    }
    
    
}
