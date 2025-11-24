package grpc;

import java.io.IOException;
import java.util.List;

import emptyimplementations.ProcessAPIImpl;
import grpc.datastore.DataStoreServiceGrpc;
import grpc.datastore.ReadRequest;
import grpc.datastore.ReadResponse;
import grpc.datastore.WriteRequest;
import grpc.datastore.WriteResponse;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

public class DataStoreServer {
	// gRPC server object
	private Server server;

	// Start the server on the given port
	public void start(int port) throws IOException {
		// Create ProcessAPIImpl (your existing logic)
		ProcessAPIImpl processAPI = new ProcessAPIImpl();

		// Build and start gRPC server with our service
		server = ServerBuilder.forPort(port).addService(new DataStoreServiceImpl(processAPI)).build().start();

		System.out.println("Data Store Server started on port " + port);

		// Shut down server cleanly when program stops
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.err.println("Shutting down Data Store Server...");
			if (server != null) {
				server.shutdown();
			}
		}));
	}

	// Keep the server running
	public void blockUntilShutdown() throws InterruptedException {
		if (server != null) { 
			server.awaitTermination();
		}
	}

	// gRPC service implementation class
	static class DataStoreServiceImpl extends DataStoreServiceGrpc.DataStoreServiceImplBase {
		// Reference to ProcessAPIImpl
		private final ProcessAPIImpl processAPI;

		// Constructor gets the ProcessAPIImpl
		public DataStoreServiceImpl(ProcessAPIImpl processAPI) {
			this.processAPI = processAPI;
		}

		// Handle readData RPC calls
		@Override
		public void readData(ReadRequest request, StreamObserver<ReadResponse> responseObserver) {
			// Print which source we are reading from
			System.out.println("Reading: " + request.getSource());

			try {
				// Make a Process API ReadRequest
				processapi.ReadRequest apiRequest = new processapi.ReadRequest(request.getSource());

				// Call ProcessAPIImpl to do the actual work
				processapi.ReadResponse apiResponse = processAPI.readData(apiRequest);

				// Build gRPC ReadResponse
				ReadResponse.Builder builder = ReadResponse.newBuilder().setSuccess(apiResponse.isSuccess());

				// If success and numbers are present, add them to response
				if (apiResponse.isSuccess() && apiResponse.getIntegerStream() != null) {
					List<Integer> numbers = apiResponse.getIntegerStream().getNumbers();
					builder.addAllNumbers(numbers);
					System.out.println("  Read " + numbers.size() + " numbers: " + numbers);
				}

				// Send response back to client
				responseObserver.onNext(builder.build());
				responseObserver.onCompleted();

			} catch (Exception e) {
				// Print error and send failure response
				System.err.println(" Error in readData: " + e.getMessage());
				responseObserver.onNext(ReadResponse.newBuilder().setSuccess(false).build());
				responseObserver.onCompleted();
			}
		}

		// Handle writeData RPC calls
		@Override
		public void writeData(WriteRequest request, StreamObserver<WriteResponse> responseObserver) {
			// Print which destination we are writing to
			System.out.println("Writing to: " + request.getDestination());

			try {
				// Make a Process API WriteRequest
				processapi.WriteRequest apiRequest = new processapi.WriteRequest(request.getDestination(),
						request.getResultsList());

				// Call ProcessAPIImpl to do the actual work
				processapi.WriteResponse apiResponse = processAPI.writeData(apiRequest);

				// Build gRPC WriteResponse
				WriteResponse.Builder builder = WriteResponse.newBuilder().setSuccess(apiResponse.isSuccess());

				// Set message based on success or failure
				if (apiResponse.isSuccess()) {
					builder.setMessage("Write successful");
					System.out.println(" Wrote " + request.getResultsCount() + " results");
				} else {
					builder.setMessage("Write failed");
					System.err.println(" Write failed");
				}

				// Send response back to client
				responseObserver.onNext(builder.build());
				responseObserver.onCompleted();

			} catch (Exception e) {
				// Print error and send failure response with message
				System.err.println(" Error in writeData: " + e.getMessage());
				responseObserver.onNext(
						WriteResponse.newBuilder().setSuccess(false).setMessage("Error: " + e.getMessage()).build());
				responseObserver.onCompleted();
			}
		}
	}
}
