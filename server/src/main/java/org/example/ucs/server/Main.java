package org.example.ucs.server;

import org.example.ucs.server.command.CommandDecodingFactory;
import org.example.ucs.server.response.ResponseEncodingFactory;

final class Main {

    public static void main(final String[] args) {
        System.out.println("Running server");
        final String portString = System.getenv("PORT");
        try {
            final int port = Integer.parseInt(portString);
            final Cache cache = new Cache();
            final ResponseEncodingFactory responseEncodingFactory = new ResponseEncodingFactory();
            final CommandDecodingFactory commandDecodingFactory = new CommandDecodingFactory(cache,
                    responseEncodingFactory);
            final Server server = new Server(port, commandDecodingFactory);
            final Thread mainThread = new Thread(server);
            Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
            mainThread.start();
        } catch (final Exception e) {
            System.err.println("failed due to: " + e);
        }
    }
}