package emptyimplementations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import processapi.DataStorageAPI;
import processapi.IntegerStream;
import processapi.ReadRequest;
import processapi.ReadResponse;
import processapi.WriteRequest;
import processapi.WriteResponse;

// Implementation of DataStorageAPI using Scanner for reading integers from a file
public class ProcessAPIImpl implements DataStorageAPI {

    @Override
    public ReadResponse readData(ReadRequest request) {
        List<Integer> numbers = new ArrayList<>();
        try {
            // Create a File object from the source path.
            File inputFile = new File(request.getSource());

            try (Scanner scanner = new Scanner(inputFile)) {
                // Set the delimiter to a comma
                scanner.useDelimiter(",");

                // Loop through the file as long as there is another integer to read.
                while (scanner.hasNextInt()) {
                    numbers.add(scanner.nextInt());
                }
            }

            IntegerStream stream = new IntegerStreamImpl(numbers);
            return new ReadResponseImpl(true, stream);

        } catch (IOException e) {
            // This catches errors like the file not being found.
            return new ReadResponseImpl(false, null);
        }
    }

    @Override
    public WriteResponse writeData(WriteRequest request) {
        // Write the results to the specified destination file.
        try {
            Path path = Paths.get(request.getDestination());
            Files.write(path, request.getResults());
            return new WriteResponseImpl(true);
        } catch (IOException e) {
            return new WriteResponseImpl(false);
        }
    }
}