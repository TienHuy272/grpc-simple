package com.hnt.grpc.greeting.client.streamingserver;

import com.proto.greet.GreetManyTimesRequest;
import com.proto.greet.Greeting;
import com.proto.greet.GreetingServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GreetingStreamingServerClient {
    public static void main(String[] args) {
        System.out.println("GRPC Client is up !");
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50056)
                .usePlaintext()
                .build();

        GreetingServiceGrpc.GreetingServiceBlockingStub syncClient = GreetingServiceGrpc.newBlockingStub(channel);
        GreetManyTimesRequest request = GreetManyTimesRequest.newBuilder()
                .setGreeting(Greeting.newBuilder().setFirstName("Lionel").setLastName("Messi").build())
                        .build();
        syncClient.greetManyTimes(request)
                .forEachRemaining(response -> {
                    System.out.println(response.getResult());
                });
        System.out.println("Shutting down channel");
        channel.shutdown();
    }
}
