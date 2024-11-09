package com.hnt.grpc.greeting.client.bidirection;

import com.proto.greet.Greeting;
import com.proto.greet.GreetingServiceGrpc;
import com.proto.greet.LongGreetRequest;
import com.proto.greet.LongGreetResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class BiDirectionGreetingClient {
    public static void main(String[] args) {
        System.out.println("GRPC Client is up !");
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 50059)
                .usePlaintext()
                .build();
        GreetingServiceGrpc.GreetingServiceStub asyncClient = GreetingServiceGrpc.newStub(channel);
        CountDownLatch latch = new CountDownLatch(1);
        StreamObserver<LongGreetRequest> requestStreamObserver = asyncClient.longGreet(new StreamObserver<>() {
            @Override
            public void onNext(LongGreetResponse longGreetResponse) {
                //get response from server
                System.out.println("Response: " + longGreetResponse.getResult());
            }

            @Override
            public void onError(Throwable throwable) {
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                //server is done sending us data
                System.out.println("Server has completed send data");
                latch.countDown();
            }
        });

        Arrays.asList("Messi", "Ronaldo", "Kaka", "Neymar", "Suarez")
                        .forEach(name -> {
                            requestStreamObserver.onNext(
                                    LongGreetRequest.newBuilder()
                                            .setGreeting(Greeting.newBuilder().setFirstName(name).build()).build());
                        });

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
