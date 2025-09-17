package processapi;
// Represents a request to write formatted results to a specified destination
import java.util.List;

public class WriteRequest {
    private String destination;
    private List<String> results;

    public WriteRequest(String destination, List<String> results) {
        this.destination = destination;
        this.results = results;
    }

    public String getDestination() {
        return destination;
    }

    public List<String> getResults() {
        return results;
    }
}