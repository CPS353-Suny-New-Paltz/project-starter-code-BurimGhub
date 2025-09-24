package infrastructure;

import java.util.List;
import java.util.ArrayList;

public class InMemoryOutputConfig {
    private List<String> outputData;
    
    public InMemoryOutputConfig() {
        this.outputData = new ArrayList<>();
    }
    
    public List<String> getOutputData() {
        return outputData;
    }
    
    public void addAllOutputs(List<String> outputs) {
        outputData.addAll(outputs);
    }
}