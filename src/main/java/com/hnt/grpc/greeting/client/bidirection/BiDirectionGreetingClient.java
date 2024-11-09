package com.hnt.grpc.greeting.client.bidirection;

import com.proto.greet.*;
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
        StreamObserver<GreetEveryoneRequest> requestStreamObserver = asyncClient.greetEveryone(new StreamObserver<>() {
            @Override
            public void onNext(GreetEveryoneResponse longGreetResponse) {
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
                            System.out.println("Sending: " + name);
                            requestStreamObserver.onNext(
                                    GreetEveryoneRequest.newBuilder()
                                            .setGreeting(Greeting.newBuilder().setFirstName(name).build()).build());
                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
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
