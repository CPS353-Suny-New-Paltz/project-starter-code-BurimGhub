package grpc;

import java.util.concurrent.TimeUnit;

import emptyimplementations.IntegerStreamImpl;
import emptyimplementations.ReadResponseImpl;
import emptyimplementations.WriteResponseImpl;
import grpc.datastore.DataStoreServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import processapi.DataStorageAPI;
import processapi.IntegerStream;

public class DataStoreGrpcClient implements DataStorageAPI {
    private final ManagedChannel channel;
    private final DataStoreServiceGrpc.DataStoreServiceBlockingStub stub;

    public DataStoreGrpcClient(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
        this.stub = DataStoreServiceGrpc.newBlockingStub(channel);
        System.out.println(" Connected to Data Store at " + host + ":" + port);
    }
    @Override
    public processapi.ReadResponse readData(processapi.ReadRequest request) { 
        try {
            
            grpc.datastore.ReadRequest grpcRequest = grpc.datastore.ReadRequest.newBuilder()
                    .setSource(request.getSource())
                    .build();

            
            grpc.datastore.ReadResponse grpcResponse = stub.readData(grpcRequest);

          
            if (grpcResponse.getSuccess()) {
                IntegerStream stream = new IntegerStreamImpl(grpcResponse.getNumbersList());
                
                return new ReadResponseImpl(true, stream);
            }
            return new ReadResponseImpl(false, null);
            
        } catch (Exception e) {
            System.err.println("  ✗ gRPC Read Error: " + e.getMessage());
            return new ReadResponseImpl(false, null);
        }
    }

 
    @Override
    public processapi.WriteResponse writeData(processapi.WriteRequest request) {
        try {
            grpc.datastore.WriteRequest grpcRequest = grpc.datastore.WriteRequest.newBuilder()
                    .setDestination(request.getDestination())
                    .addAllResults(request.getResults())
                    .build();

 
            grpc.datastore.WriteResponse grpcResponse = stub.writeData(grpcRequest);

            
            return new WriteResponseImpl(grpcResponse.getSuccess());
            
        } catch (Exception e) {
            System.err.println("  gRPC Write Error: " + e.getMessage());
            return new WriteResponseImpl(false);
        }
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }
}