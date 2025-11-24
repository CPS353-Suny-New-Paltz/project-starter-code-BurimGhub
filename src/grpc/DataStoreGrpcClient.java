package grpc;

import java.util.concurrent.TimeUnit;

import emptyimplementations.IntegerStreamImpl;
import emptyimplementations.ReadResponseImpl;
import emptyimplementations.WriteResponseImpl;
import grpc.datastore.DataStoreServiceGrpc;
import grpc.datastore.ReadRequest;
import grpc.datastore.ReadResponse;
import grpc.datastore.WriteRequest;
import grpc.datastore.WriteResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import processapi.DataStorageAPI;
import processapi.IntegerStream;

public class DataStoreGrpcClient implements DataStorageAPI {
	private final ManagedChannel channel;
	private final DataStoreServiceGrpc.DataStoreServiceBlockingStub stub;

	public DataStoreGrpcClient(String host, int port) {
		this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
		this.stub = DataStoreServiceGrpc.newBlockingStub(channel);
		System.out.println(" Connected to Data Store at " + host + ":" + port);
	}

	@Override
	public processapi.ReadResponse readData(processapi.ReadRequest request) {
		try {
			// Convert your API request to gRPC request
			ReadRequest grpcRequest = ReadRequest.newBuilder().setSource(request.getSource()).build();

			// Call the remote gRPC service
			ReadResponse grpcResponse = stub.readData(grpcRequest);

			// Convert gRPC response back to your API response
			if (grpcResponse.getSuccess()) {
				IntegerStream stream = new IntegerStreamImpl(grpcResponse.getNumbersList());
				return new ReadResponseImpl(true, stream);
			}
			return new ReadResponseImpl(false, null);

		} catch (Exception e) {
			System.err.println("   gRPC Read Error: " + e.getMessage());
			return new ReadResponseImpl(false, null);
		}
	}

	@Override
	public processapi.WriteResponse writeData(processapi.WriteRequest request) {
		try {
			// Convert your API request to gRPC request
			WriteRequest grpcRequest = WriteRequest.newBuilder().setDestination(request.getDestination())
					.addAllResults(request.getResults()).build();

			// Call the remote gRPC service
			WriteResponse grpcResponse = stub.writeData(grpcRequest);

			// Convert gRPC response back to your API response
			return new WriteResponseImpl(grpcResponse.getSuccess());

		} catch (Exception e) {
			System.err.println("   gRPC Write Error: " + e.getMessage());
			return new WriteResponseImpl(false);
		}
	}

	public void shutdown() throws InterruptedException {
		channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
	}
}