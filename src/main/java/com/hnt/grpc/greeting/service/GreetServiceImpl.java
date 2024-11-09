package com.hnt.grpc.greeting.service;

import com.proto.greet.*;
import com.proto.greet.GreetingServiceGrpc.GreetingServiceImplBase;
import io.grpc.stub.StreamObserver;


public class GreetServiceImpl extends GreetingServiceImplBase {

    @Override
    public void greet(GreetingRequest request, StreamObserver<GreetingResponse> responseObserver) {
        //extract fields from request
        Greeting greeting = request.getGreeting();

        //create a response
        String result = "Hi " + greeting.getFirstName() + greeting.getLastName();
        GreetingResponse response = GreetingResponse.newBuilder().setResult(result).build();

        //send response
        responseObserver.onNext(response);

        //complete RPC call
        responseObserver.onCompleted();
    }

    @Override
    public void greetManyTimes(GreetManyTimesRequest request, StreamObserver<GreetManyTimesResponse> responseObserver) {
        String firstName = request.getGreeting().getFirstName();
        try {
            for (int i = 0; i < 10; i++) {
                String result = "Hi, " + firstName + ", response number : " + i;
                GreetManyTimesResponse response = GreetManyTimesResponse.newBuilder()
                        .setResult(result).build();
                responseObserver.onNext(response);
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            responseObserver.onCompleted();
        }
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<LongGreetRequest> longGreet(StreamObserver<LongGreetResponse> responseObserver) {
        return new StreamObserver<>() {
            String result = "";

            @Override
            public void onNext(LongGreetRequest longGreetRequest) {
                //client send request
                result += "Hi " + longGreetRequest.getGreeting().getFirstName() + "\n";
            }

            @Override
            public void onError(Throwable throwable) {
                //client send error
            }

            @Override
            public void onCompleted() {
                //client is done
                responseObserver.onNext(
                        LongGreetResponse.newBuilder().setResult(result).build()
                );
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public StreamObserver<GreetEveryoneRequest> greetEveryone(StreamObserver<GreetEveryoneResponse> responseObserver) {
        return new StreamObserver<>() {
            @Override
            public void onNext(GreetEveryoneRequest value) {
                String response = "HI " + value.getGreeting().getFirstName();
                GreetEveryoneResponse greetEveryoneResponse = GreetEveryoneResponse.newBuilder()
                        .setResult(response).build();
                responseObserver.onNext(greetEveryoneResponse);
            }

            @Override
            public void onError(Throwable throwable) {
                //do nothing
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };

    }
}
