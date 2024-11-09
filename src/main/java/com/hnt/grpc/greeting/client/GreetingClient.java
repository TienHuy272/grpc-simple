package com.hnt.grpc.greeting.client;

import com.proto.dummy.DummyServiceGrpc;
import com.proto.greet.Greeting;
import com.proto.greet.GreetingRequest;
import com.proto.greet.GreetingResponse;
import com.proto.greet.GreetingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GreetingClient {
    public static void main(String[] args) {
        System.out.println("GRPC Client is up !");
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50052)
                .usePlaintext()
                .build();

        GreetingServiceGrpc.GreetingServiceBlockingStub syncClient = GreetingServiceGrpc.newBlockingStub(channel);
        Greeting greeting = Greeting.newBuilder().setFirstName("Tien").setLastName("Huy").build();
        GreetingRequest request = GreetingRequest.newBuilder().setGreeting(greeting).build();

        //send request gpc
        GreetingResponse response = syncClient.greet(request);
        System.out.println("Response : " + response.getResult());

        /*
         * old and dummy code
        DummyServiceGrpc.DummyServiceBlockingStub syncClient = DummyServiceGrpc.newBlockingStub(channel);
        DummyServiceGrpc.DummyServiceFutureStub asyncClient = DummyServiceGrpc.newFutureStub(channel);
         */

        System.out.println("Shutting down channel");
        channel.shutdown();
    }
}
