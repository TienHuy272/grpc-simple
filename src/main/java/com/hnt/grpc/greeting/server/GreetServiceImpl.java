package com.hnt.grpc.greeting.server;

import com.proto.greet.Greeting;
import com.proto.greet.GreetingRequest;
import com.proto.greet.GreetingResponse;
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

}
