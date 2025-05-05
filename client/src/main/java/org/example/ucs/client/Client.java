package org.example.ucs.client;

import org.example.ucs.client.commands.CommandHandler;
import org.example.ucs.client.responses.ResponseDecodingFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.DatagramChannel;

final class Client {

    private final InetSocketAddress serverAddress;
    private final ResponseDecodingFactory responseDecodingFactory;
    private final CommandHandler commandHandler;
    private DatagramChannel channel = null;
    private Thread receiverThread = null;
    private Thread senderThread = null;
    private volatile boolean running = true;

    public Client(final int port,
                  final String serverHost,
                  final ResponseDecodingFactory responseDecodingFactory,
                  final CommandHandler commandHandler) {
        this.serverAddress = new InetSocketAddress(serverHost, port);
        this.responseDecodingFactory = responseDecodingFactory;
        this.commandHandler = commandHandler;
    }

    public void start() {
        try {
            channel = DatagramChannel.open();
            channel.configureBlocking(false);
            receiverThread = new Thread(new Receiver(channel, responseDecodingFactory, this));
            senderThread = new Thread(new Sender(channel, serverAddress, commandHandler, this));
            receiverThread.start();
            senderThread.start();
            Runtime.getRuntime().addShutdownHook(new Thread(this::stop));
        } catch (Exception e) {
            System.err.println("Client error: " + e);
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void stop() {
        running = false;
        if (channel != null) {
            try {
                channel.close();
            } catch (IOException e) {
                System.err.println("Client error on shutdown: " + e);
            }
        }
        try {
            if (senderThread != null) {
                senderThread.join();
            }
            if (receiverThread != null) {
                receiverThread.join();
            }
        } catch (final InterruptedException e) {
            System.err.println("Client error on joining threads: " + e);
            Thread.currentThread().interrupt();
        }
    }
}
