package org.example.ucs.server;

import org.example.ucs.server.command.CommandDecodingFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

final class Server implements Runnable {

    private final int port;
    private final ByteBuffer buffer = ByteBuffer.allocateDirect((int) Math.pow(2, 16));
    private final BufferPool bufferPool = new BufferPool();
    private final CommandDecodingFactory commandDecodingFactory;
    private final ExecutorService executorService;
    private volatile boolean running = true;

    public Server(final int port,
                  final CommandDecodingFactory commandDecodingFactory) {
        this.port = port;
        this.commandDecodingFactory = commandDecodingFactory;
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Override
    public void run() {
        try (final DatagramChannel channel = DatagramChannel.open()) {
            channel.bind(new InetSocketAddress(port));
            channel.configureBlocking(false); // Non-blocking mode

            while (running) {
                buffer.clear();
                final InetSocketAddress clientAddress = (InetSocketAddress) channel.receive(buffer);
                if (clientAddress != null) {
                    final ByteBuffer packet = bufferPool.getBuffer();
                    buffer.flip();
                    packet.put(buffer);

                    executorService.submit(() -> {
                        commandDecodingFactory.handle(packet);
                        if (packet.hasRemaining()) {
                            try {
                                channel.send(packet, clientAddress);
                            } catch (IOException e) {
                                System.err.println("Exception was thrown with cause: " + e);
                            } finally {
                                bufferPool.returnToPool(packet);
                            }
                        }
                    });
                }
            }
        } catch (Exception e) {
            System.err.println("Exception was thrown with cause: " + e);
        }
    }

    public void stop() {
        running = false;
        executorService.shutdownNow();
    }
}
