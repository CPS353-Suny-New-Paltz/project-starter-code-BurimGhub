package infrastructure;

import java.util.List;
import java.util.ArrayList;

public class InMemoryInputConfig {
    private List<Integer> inputData;
    
    public InMemoryInputConfig(List<Integer> inputData) {
        this.inputData = new ArrayList<>(inputData);
    }
    
    public List<Integer> getInputData() {
        return new ArrayList<>(inputData);
    }
}