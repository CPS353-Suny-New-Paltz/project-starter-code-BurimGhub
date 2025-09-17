package processapi;

import project.annotations.ProcessAPI;

@ProcessAPI
public interface DataStorageAPI {
	// Reads a stream of integers from the specified source
	ReadResponse readData(ReadRequest request);

	// Writes a list of formatted string results to the specified destination
	WriteResponse writeData(WriteRequest request);
}