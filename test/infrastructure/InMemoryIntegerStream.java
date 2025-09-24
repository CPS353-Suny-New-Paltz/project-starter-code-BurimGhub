package infrastructure;

import java.util.List;

/**
 * Test-only implementation of IntegerStream for in-memory testing
 */
public class InMemoryIntegerStream implements processapi.IntegerStream {
    private List<Integer> numbers;
    
    public InMemoryIntegerStream(List<Integer> numbers) {
        this.numbers = numbers;
    }
    
    @Override
    public List<Integer> getNumbers() {
        return numbers;
    }
}