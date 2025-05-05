package org.example.ucs.client;

import org.example.ucs.client.commands.CommandEncodingFactory;
import org.example.ucs.client.commands.CommandHandler;
import org.example.ucs.client.responses.ResponseDecodingFactory;

final class Main {
    public static void main(final String[] args) {
        System.out.println("Running client");
        final String portString = System.getenv("SERVER_PORT");
        try {
            final int port = Integer.parseInt(portString);
            final String serverAddress = System.getenv("SERVER_ADDRESS");
            final CommandEncodingFactory commandEncodingFactory = new CommandEncodingFactory();
            final ResponseDecodingFactory responseDecodingFactory = new ResponseDecodingFactory();
            final CommandHandler commandHandler = new CommandHandler(commandEncodingFactory);
            final Client client = new Client(port, serverAddress, responseDecodingFactory, commandHandler);
            client.start();
        } catch (final Exception e) {
            System.err.println("failed due to: " + e);
        }
    }
}