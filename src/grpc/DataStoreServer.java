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
	private Server server;

	public void start(int port) throws IOException {
		// Use your existing ProcessAPIImpl
		ProcessAPIImpl processAPI = new ProcessAPIImpl();

		server = ServerBuilder.forPort(port).addService(new DataStoreServiceImpl(processAPI)).build().start();

		System.out.println("Data Store Server started on port " + port);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.err.println(" Shutting down Data Store Server...");
			if (server != null)
				server.shutdown();
		}));
	}

	public void blockUntilShutdown() throws InterruptedException {
		if (server != null)
			server.awaitTermination();
	}

	public static void main(String[] args) throws Exception {
		int port = args.length > 0 ? Integer.parseInt(args[0]) : 9091;
		DataStoreServer server = new DataStoreServer();
		server.start(port);
		server.blockUntilShutdown();
	}

	static class DataStoreServiceImpl extends DataStoreServiceGrpc.DataStoreServiceImplBase {
		private final ProcessAPIImpl processAPI;

		public DataStoreServiceImpl(ProcessAPIImpl processAPI) {
			this.processAPI = processAPI;
		}

		@Override
		public void readData(ReadRequest request, StreamObserver<ReadResponse> responseObserver) {
			System.out.println("Reading: " + request.getSource());

			try {
				// Use your existing ProcessAPIImpl
				processapi.ReadRequest apiRequest = new processapi.ReadRequest(request.getSource());
				processapi.ReadResponse apiResponse = processAPI.readData(apiRequest);

				ReadResponse.Builder builder = ReadResponse.newBuilder().setSuccess(apiResponse.isSuccess());

				if (apiResponse.isSuccess() && apiResponse.getIntegerStream() != null) {
					List<Integer> numbers = apiResponse.getIntegerStream().getNumbers();
					builder.addAllNumbers(numbers);
					System.out.println("   Read " + numbers.size() + " numbers: " + numbers);
				}

				responseObserver.onNext(builder.build());
				responseObserver.onCompleted();

			} catch (Exception e) {
				System.err.println(" Error: " + e.getMessage());
				responseObserver.onNext(ReadResponse.newBuilder().setSuccess(false).build());
				responseObserver.onCompleted();
			}
		}

		@Override
		public void writeData(WriteRequest request, StreamObserver<WriteResponse> responseObserver) {
			System.out.println("Writing to: " + request.getDestination());

			try {
				// Use your existing ProcessAPIImpl
				processapi.WriteRequest apiRequest = new processapi.WriteRequest(request.getDestination(),
						request.getResultsList());
				processapi.WriteResponse apiResponse = processAPI.writeData(apiRequest);

				WriteResponse.Builder builder = WriteResponse.newBuilder().setSuccess(apiResponse.isSuccess());

				if (apiResponse.isSuccess()) {
					builder.setMessage("Write successful");
					System.out.println(" Wrote " + request.getResultsCount() + " results");
				} else {
					builder.setMessage("Write failed");
					System.err.println(" Write failed");
				}

				responseObserver.onNext(builder.build());
				responseObserver.onCompleted();

			} catch (Exception e) {
				System.err.println(" Error: " + e.getMessage());
				responseObserver.onNext(
						WriteResponse.newBuilder().setSuccess(false).setMessage("Error: " + e.getMessage()).build());
				responseObserver.onCompleted();
			}
		}
	}
}
