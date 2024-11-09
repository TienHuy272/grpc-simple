package com.hnt.grpc.greeting.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class GreetingServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("GRPC Server is up !");
        Server server = ServerBuilder.forPort(50052).build();
        server.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Receive shutdown request");
            server.shutdown();
            System.out.println("Successfully stop the server");
        }));
        server.awaitTermination();;
    }
}
