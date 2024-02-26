package com.swiggy.wallet.clients;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import proto.GreetRequest;
import proto.GreetResponse;
import proto.GreetServiceGrpc;

@Service
public class GreetClient {
    public String greet(String name) throws StatusRuntimeException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50051)
                .usePlaintext()
                .build();
        GreetServiceGrpc.GreetServiceBlockingStub stub = GreetServiceGrpc.newBlockingStub(channel);
        GreetRequest request = GreetRequest.newBuilder().setName(name).build();
        GreetResponse response = stub.greet(request);
        channel.shutdown();
        return response.getMessage();
    }
}
