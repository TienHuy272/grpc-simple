package com.hnt.grpc.calculator.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class CalculatorServer {
    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(50055)
                .addService(new CalculatorServiceImpl())
                .build();
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("CalculatorServer: Receive shutdown request");
            server.shutdown();
            System.out.println("CalculatorServer: Successfully stop the server");
        }));
        server.awaitTermination();;
    }
}
