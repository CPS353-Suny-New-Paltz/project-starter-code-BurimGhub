package emptyimplementations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
		// Added request parameter check
		if (request == null) {
			return new ReadResponseImpl(false, null);
		}
		// parameter check for getting the input file
		if (request.getSource() == null || request.getSource().trim().isEmpty()) {
			return new ReadResponseImpl(false, null);
		}
		List<Integer> numbers = new ArrayList<>();
		try {
			// Create a File object from the source path.
			File inputFile = new File(request.getSource());
			// Validate file exists and is readable
			if (!inputFile.exists()) {
				return new ReadResponseImpl(false, null);
			}
			// Check if the file is readable
			if (!inputFile.canRead()) {
				return new ReadResponseImpl(false, null);
			}
			// MODIFICATION: Replaced Scanner with BufferedReader
            try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
                String line;
                // Read the file line by line
                while ((line = reader.readLine()) != null) {
                    // Split the line into parts based on the comma delimiter
                    String[] parts = line.split(",");
                    for (String part : parts) {
                        try {
                            // Trim any whitespace and parse the string to an integer
                            numbers.add(Integer.parseInt(part.trim()));
                        } catch (NumberFormatException e) {
                            // This handles cases where a part of the line is not a valid number,
                            // preventing the entire process from crashing.
                            System.err.println("Skipping invalid number format: " + part);
                        }
                    }
                }
            }

            IntegerStream stream = new IntegerStreamImpl(numbers);
            return new ReadResponseImpl(true, stream);

        } catch (IOException e) {
            // This catches file-related errors from the BufferedReader
            return new ReadResponseImpl(false, null);
        }
    }

	// Changed to use Files.write to write a single comma-separated line
	@Override
	public WriteResponse writeData(WriteRequest request) {
		// Parameter validation
		if (request == null) {
			return new WriteResponseImpl(false);
		}
		// Validate destination and results
		if (request.getDestination() == null || request.getDestination().trim().isEmpty()) {
			return new WriteResponseImpl(false);
		}
		// Validate results list is not null
		if (request.getResults() == null) {
			return new WriteResponseImpl(false);
		}

		// Empty results list is valid - no additional validation needed
		try {
			Path path = Paths.get(request.getDestination());
			// Join all results into a single comma-separated line
			String singleLine = String.join(",", request.getResults());
			Files.write(path, singleLine.getBytes());

			return new WriteResponseImpl(true);
		} catch (IOException e) {
			return new WriteResponseImpl(false);
		}
	}
}