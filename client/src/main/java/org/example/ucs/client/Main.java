package org.example.ucs.client;

import org.example.ucs.client.commands.CommandEncodingFactory;
import org.example.ucs.client.commands.CommandHandler;
import org.example.ucs.client.responses.ResponseDecodingFactory;

final class Main {
    public static void main(final String[] args) {
        System.out.println("Running client");
        final CommandEncodingFactory commandEncodingFactory = new CommandEncodingFactory();
        final CommandHandler commandHandler = new CommandHandler(commandEncodingFactory);
        final Client client = new Client(7890, "localhost", new ResponseDecodingFactory(),
                commandHandler);
        client.start();
    }
}