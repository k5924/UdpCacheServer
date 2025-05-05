package org.example.ucs.server;

import org.example.ucs.server.command.CommandDecodingFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

final class Server implements Runnable {

    private final int port;
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
            channel.configureBlocking(false);

            while (running) {
                final ByteBuffer buffer = bufferPool.getBuffer();
                buffer.clear();
                final InetSocketAddress clientAddress = (InetSocketAddress) channel.receive(buffer);
                if (clientAddress != null) {
                    executorService.submit(() -> {
                        try {
                            commandDecodingFactory.handle(buffer);
                            if (buffer.hasRemaining()) {
                                channel.send(buffer, clientAddress);
                            }
                        } catch (IOException e) {
                            System.err.println("Exception was thrown with cause: " + e);
                        } finally {
                            bufferPool.returnToPool(buffer);
                        }
                    });
                } else {
                    bufferPool.returnToPool(buffer);
                    Thread.sleep(1);
                }
            }
        } catch (Exception e) {
            System.err.println("Exception was thrown with cause: " + e);
        }
    }

    public void stop() {
        running = false;
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (final Exception e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
