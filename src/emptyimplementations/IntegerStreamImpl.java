package emptyimplementations;

import java.util.Collections;
import java.util.List;

import processapi.IntegerStream;

public class IntegerStreamImpl implements IntegerStream {

    @Override
    public List<Integer> getNumbers() {
        // Empty implementation returns an empty list
        return Collections.emptyList();
    }

}