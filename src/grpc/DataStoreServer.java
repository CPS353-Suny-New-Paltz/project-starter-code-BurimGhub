package grpc;

import emptyimplementations.ProcessAPIImpl;
import grpc.datastore.DataStoreServiceGrpc;
import grpc.datastore.ReadRequest;
import grpc.datastore.ReadResponse;
import grpc.datastore.ShutdownRequest;
import grpc.datastore.ShutdownResponse;
import grpc.datastore.WriteRequest;
import grpc.datastore.WriteResponse;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DataStoreServer {
	private Server server;
	
	public void start(int port) throws IOException {
		ProcessAPIImpl processAPI = new ProcessAPIImpl();

		server = ServerBuilder.forPort(port).addService(new DataStoreServiceImpl(processAPI, this)).build().start();

		System.out.println("Data Store Server started on port " + port);

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			System.err.println("Shutting down Data Store Server...");
			DataStoreServer.this.stop();
			System.err.println("Server shut down successfully");
		}));
	}

	// Stops the Data Store server.
	public void stop() {
		System.out.println("Stopping Data Store Server...");
		if (server != null) {
			server.shutdown();
			try {
				if (!server.awaitTermination(5, TimeUnit.SECONDS)) {
					server.shutdownNow();
				}
			} catch (InterruptedException e) {
				server.shutdownNow();
				Thread.currentThread().interrupt();
			}
		}
		System.out.println("Data Store Server stopped");
		System.exit(0);
	}

	// Blocks until the server terminates.
	public void blockUntilShutdown() throws InterruptedException {
		if (server != null) {
			server.awaitTermination();
		}
	}

	public static void main(String[] args) throws Exception {
		int port = args.length > 0 ? Integer.parseInt(args[0]) : 9091;
		DataStoreServer server = new DataStoreServer();
		server.start(port);
		server.blockUntilShutdown();
	}

	static class DataStoreServiceImpl extends DataStoreServiceGrpc.DataStoreServiceImplBase {
		private final ProcessAPIImpl processAPI;
		private final DataStoreServer parentServer;
		public DataStoreServiceImpl(ProcessAPIImpl processAPI, DataStoreServer parentServer) {
			this.processAPI = processAPI;
			this.parentServer = parentServer;
		}

		@Override
		public void readData(ReadRequest request, StreamObserver<ReadResponse> responseObserver) {
			System.out.println("Reading: " + request.getSource());

			try {
				processapi.ReadRequest apiRequest = new processapi.ReadRequest(request.getSource());
				processapi.ReadResponse apiResponse = processAPI.readData(apiRequest);

				ReadResponse.Builder builder = ReadResponse.newBuilder().setSuccess(apiResponse.isSuccess());

				if (apiResponse.isSuccess() && apiResponse.getIntegerStream() != null) {
					List<Integer> numbers = apiResponse.getIntegerStream().getNumbers();
					builder.addAllNumbers(numbers);
					System.out.println("  Read " + numbers.size() + " numbers");
				}

				responseObserver.onNext(builder.build());
				responseObserver.onCompleted();

			} catch (Exception e) {
				System.err.println("  Error: " + e.getMessage());
				responseObserver.onNext(ReadResponse.newBuilder().setSuccess(false).build());
				responseObserver.onCompleted();
			}
		}
		@Override
		public void writeData(WriteRequest request, StreamObserver<WriteResponse> responseObserver) {
			System.out.println("Writing to: " + request.getDestination());

			try {
				processapi.WriteRequest apiRequest = new processapi.WriteRequest(request.getDestination(),
						request.getResultsList());
				processapi.WriteResponse apiResponse = processAPI.writeData(apiRequest);

				WriteResponse.Builder builder = WriteResponse.newBuilder().setSuccess(apiResponse.isSuccess());

				if (apiResponse.isSuccess()) {
					builder.setMessage("Write successful");
					System.out.println("  Write successful");
				} else {
					builder.setMessage("Write failed");
					System.err.println("  Write failed");
				}

				responseObserver.onNext(builder.build());
				responseObserver.onCompleted();

			} catch (Exception e) {
				System.err.println("  Error: " + e.getMessage());
				responseObserver.onNext(
						WriteResponse.newBuilder().setSuccess(false).setMessage("Error: " + e.getMessage()).build());
				responseObserver.onCompleted();
			}
		}

		@Override
		public void shutdown(ShutdownRequest request, StreamObserver<ShutdownResponse> responseObserver) {
			System.out.println("\nShutdown request received");

			ShutdownResponse response = ShutdownResponse.newBuilder().setSuccess(true).setMessage("Shutting down...")
					.build();

			responseObserver.onNext(response);
			responseObserver.onCompleted();

			new Thread(() -> {
				try {
					Thread.sleep(500);
					parentServer.stop();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}).start();
		}
	}
}
