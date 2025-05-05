package org.example.ucs.messages.command;

import java.nio.ByteBuffer;

public final class PingDecoder {
    public static final int MSG_ID = Integer.MAX_VALUE;

    public void decode(final ByteBuffer buffer) {
        System.out.println("decoded ping message");
    }
}
