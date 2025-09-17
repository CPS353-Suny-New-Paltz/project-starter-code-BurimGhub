package processapi;
// Represents a request to read data from a specified source
public class ReadRequest {
    private final String source;

    public ReadRequest(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }
}