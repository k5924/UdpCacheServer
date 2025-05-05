package org.example.ucs.server;

import org.example.ucs.server.command.CommandDecodingFactory;
import org.example.ucs.server.response.ResponseEncodingFactory;

final class Main {

    public static void main(final String[] args) {
        System.out.println("Running server");
        final Server server = new Server(7890, new CommandDecodingFactory(new Cache(),
                new ResponseEncodingFactory()));
        final Thread mainThread = new Thread(server);
        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
        mainThread.start();
    }
}