package org.example.ucs.client;

import org.example.ucs.client.commands.CommandHandler;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

final class Sender implements Runnable {
    private final DatagramChannel channel;
    private final InetSocketAddress serverAddress;
    private final CommandHandler commandHandler;
    private final Client client;
    private final ByteBuffer buffer = ByteBuffer.allocateDirect((int) Math.pow(2, 16));

    public Sender(final DatagramChannel channel,
                  final InetSocketAddress serverAddress,
                  final CommandHandler commandHandler,
                  final Client client) {
        this.channel = channel;
        this.serverAddress = serverAddress;
        this.commandHandler = commandHandler;
        this.client = client;
    }

    @Override
    public void run() {
        while (client.isRunning()) {
            try {
                CommandHandler.InputOption inputOption = commandHandler.expectInput();
                if (inputOption == CommandHandler.InputOption.EXIT) {
                    client.stop();
                    break;
                } else {
                    buffer.clear();
                    commandHandler.executeInput(buffer, inputOption);
                    if (buffer.hasRemaining()) {
                        channel.send(buffer, serverAddress);
                    }
                }
            } catch (Exception e) {
                System.err.println("Sender error: " + e);
            }
        }
    }
}
