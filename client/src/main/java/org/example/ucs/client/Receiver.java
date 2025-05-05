package org.example.ucs.client;

import org.example.ucs.client.responses.ResponseDecodingFactory;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;

final class Receiver implements Runnable {

    private final DatagramChannel channel;
    private final ResponseDecodingFactory responseDecodingFactory;
    private final Client client;
    private final ByteBuffer buffer = ByteBuffer.allocateDirect((int) Math.pow(2, 16));

    public Receiver(final DatagramChannel channel,
                    final ResponseDecodingFactory responseDecodingFactory,
                    final Client client) {
        this.channel = channel;
        this.responseDecodingFactory = responseDecodingFactory;
        this.client = client;
    }

    @Override
    public void run() {
        while (client.isRunning()) {
            try {
                buffer.clear();
                InetSocketAddress response = (InetSocketAddress) channel.receive(buffer);
                if (response != null) {
                    responseDecodingFactory.handle(buffer);
                } else {
                    Thread.sleep(1);
                }
            } catch (final ClosedChannelException e) {
                break;
            } catch (final InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } catch (final Exception e) {
                System.err.println("Receiver error: " + e);
            }
        }
    }
}
