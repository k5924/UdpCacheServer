package org.example.ucs.messages.response;

import org.example.ucs.messages.shared.HeaderEncoder;

import java.nio.ByteBuffer;

public final class PongEncoder {
    public static final int MSG_ID = Integer.MAX_VALUE;

    public void encode(final ByteBuffer buffer, final HeaderEncoder headerEncoder)
    {
        if (buffer.remaining() < headerEncoder.length()) {
            System.err.println("unable to encode pong as not enough space in buffer");
            return;
        }
        headerEncoder.encode(buffer, MSG_ID);
        System.out.println("encoded pong message");
        buffer.flip();
    }
}
