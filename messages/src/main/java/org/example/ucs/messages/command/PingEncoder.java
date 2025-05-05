package org.example.ucs.messages.command;

import org.example.ucs.messages.shared.HeaderEncoder;

import java.nio.ByteBuffer;

public final class PingEncoder {
    public static final int MSG_ID = Integer.MAX_VALUE;

    public void encode(final ByteBuffer buffer, final HeaderEncoder headerEncoder) {
        if (buffer.remaining() < headerEncoder.length()) {
            System.err.println("unable ot encode ping as not enough space in buffer");
            return;
        }
        headerEncoder.encode(buffer, MSG_ID);
        buffer.flip();
        System.out.println("encoded ping message");
    }
}
