package org.example.ucs.server;

import java.nio.ByteBuffer;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class BufferPool
{
    private final Queue<ByteBuffer> bufferPool;

    public BufferPool() {
        this.bufferPool = new ArrayBlockingQueue<>(Runtime.getRuntime().availableProcessors());
        for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
            bufferPool.add(ByteBuffer.allocateDirect((int) Math.pow(2, 16)));
        }
    }

    public void returnToPool(final ByteBuffer byteBuffer) {
        byteBuffer.clear();
        bufferPool.offer(byteBuffer);
    }

    public ByteBuffer getBuffer() {
        return bufferPool.poll();
    }
}
