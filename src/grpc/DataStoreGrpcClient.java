package grpc;

import emptyimplementations.IntegerStreamImpl;
import emptyimplementations.ReadResponseImpl;
import emptyimplementations.WriteResponseImpl;
import grpc.datastore.DataStoreServiceGrpc;
import grpc.datastore.ReadRequest;
import grpc.datastore.ReadResponse;
import grpc.datastore.ShutdownRequest;
import grpc.datastore.ShutdownResponse;
import grpc.datastore.WriteRequest;
import grpc.datastore.WriteResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import processapi.DataStorageAPI;
import processapi.IntegerStream;

import java.util.concurrent.TimeUnit;

class DataStoreGrpcClient implements DataStorageAPI {
	private final ManagedChannel channel;
	private final DataStoreServiceGrpc.DataStoreServiceBlockingStub stub;

	public DataStoreGrpcClient(String host, int port) {
		this.channel = ManagedChannelBuilder.forAddress(host, port)
				.usePlaintext()
				.maxInboundMessageSize(64 * 1024 * 1024) // Updated to allow 64mb input
				.build();
		this.stub = DataStoreServiceGrpc.newBlockingStub(channel);
		System.out.println("  Connected to Data Store at " + host + ":" + port);
	}

	// Reads data from the data store.
	@Override
	public processapi.ReadResponse readData(processapi.ReadRequest request) {
		try {
			ReadRequest grpcRequest = ReadRequest.newBuilder().setSource(request.getSource()).build();

			ReadResponse grpcResponse = stub.readData(grpcRequest);

			if (grpcResponse.getSuccess()) {
				IntegerStream stream = new IntegerStreamImpl(grpcResponse.getNumbersList());
				return new ReadResponseImpl(true, stream);
			}
			return new ReadResponseImpl(false, null);

		} catch (Exception e) {
			System.err.println("  gRPC Read Error: " + e.getMessage());
			return new ReadResponseImpl(false, null);
		}
	}

	// Writes data to the data store.
	@Override
	public processapi.WriteResponse writeData(processapi.WriteRequest request) {
		try {
			WriteRequest grpcRequest = WriteRequest.newBuilder().setDestination(request.getDestination())
					.addAllResults(request.getResults()).build();

			WriteResponse grpcResponse = stub.writeData(grpcRequest);

			return new WriteResponseImpl(grpcResponse.getSuccess());

		} catch (Exception e) {
			System.err.println("  gRPC Write Error: " + e.getMessage());
			return new WriteResponseImpl(false);
		}
	}

	// Sends a shutdown request to the data store server.
	public ShutdownResponse shutdown(ShutdownRequest request) {
		try {
			return stub.shutdown(request);
		} catch (Exception e) {
			return ShutdownResponse.newBuilder().setSuccess(false).setMessage("Error: " + e.getMessage()).build();
		}
	}

	// Shuts down the gRPC channel.
	public void shutdown() throws InterruptedException {
		channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	}
}