package processapi;

import java.util.Arrays;
import project.annotations.ProcessAPIPrototype;
import java.util.List;

public class DataStoragePrototype {
	@ProcessAPIPrototype
	public void prototype(DataStorageAPI storage) {

		// Create a request to read data from a source
		ReadRequest readRequest = new ReadRequest("file:///data/input.csv");
		ReadResponse readResponse = storage.readData(readRequest);

		// Check the response and process the data
		if (readResponse.isSuccess()) {
			List<Integer> numbers = readResponse.getIntegerStream().getNumbers();
			System.out.println("Read operation successful. Read " + numbers.size() + " integers.");

			// Create a request to write some results back
			List<String> formattedResults = Arrays.asList("1:one", "21:twenty-one", "105:one hundred five");
			WriteRequest writeRequest = new WriteRequest("file:///data/output.txt", formattedResults);
			WriteResponse writeResponse = storage.writeData(writeRequest);

			if (writeResponse.isSuccess()) {
				System.out.println("Write operation successful.");
			} else {
				System.out.println("Write operation failed.");
			}
		} else {
			System.out.println("Read operation failed.");
		}
	}
}