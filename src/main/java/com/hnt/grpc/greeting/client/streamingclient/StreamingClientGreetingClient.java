package com.hnt.grpc.greeting.client.streamingclient;

import com.proto.greet.Greeting;
import com.proto.greet.GreetingServiceGrpc;
import com.proto.greet.LongGreetRequest;
import com.proto.greet.LongGreetResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class StreamingClientGreetingClient {
    public static void main(String[] args) {
        System.out.println("GRPC Client is up !");
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50058)
                .usePlaintext()
                .build();
        GreetingServiceGrpc.GreetingServiceStub asyncClient = GreetingServiceGrpc.newStub(channel);
        CountDownLatch latch = new CountDownLatch(1);
        StreamObserver<LongGreetRequest> requestStreamObserver = asyncClient.longGreet(new StreamObserver<>() {
            @Override
            public void onNext(LongGreetResponse longGreetResponse) {
                System.out.println("Receive response from server");
                System.out.println(longGreetResponse.getResult());
                //get response from server
            }

            @Override
            public void onError(Throwable throwable) {
                //get error from server
            }

            @Override
            public void onCompleted() {
                //server is done sending us data
                System.out.println("Server has completed send data");
                latch.countDown();
            }
        });

        requestStreamObserver.onNext(
                    LongGreetRequest.newBuilder()
                            .setGreeting(Greeting.newBuilder().setFirstName("Messi").build()).build());
        requestStreamObserver.onNext(
                LongGreetRequest.newBuilder()
                        .setGreeting(Greeting.newBuilder().setFirstName("Ronaldo").build()).build());
        requestStreamObserver.onNext(
                LongGreetRequest.newBuilder()
                        .setGreeting(Greeting.newBuilder().setFirstName("Kaka").build()).build());

        //tell server client is done on sending data
        requestStreamObserver.onCompleted();
        try {
            latch.await(3L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        channel.shutdown();
    }
}
