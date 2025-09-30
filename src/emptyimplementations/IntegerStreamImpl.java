package emptyimplementations;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import processapi.IntegerStream;

public class IntegerStreamImpl implements IntegerStream {
    private List<Integer> numbers;

    // Default constructor (for empty cases)
    public IntegerStreamImpl() {
        this.numbers = Collections.emptyList();
    }

    // Constructor that accepts a list of numbers
    public IntegerStreamImpl(List<Integer> numbers) {
        this.numbers = new ArrayList<>(numbers);
    }

    @Override
    public List<Integer> getNumbers() {
        return new ArrayList<>(numbers);
    }
}