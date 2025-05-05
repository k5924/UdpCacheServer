package org.example.ucs.messages.shared;

import java.nio.ByteBuffer;

public final class HeaderEncoder {

    public static final int VERSION = 1;
    private static final int LENGTH = Integer.BYTES * 2;

    public void encode(final ByteBuffer buffer, final int msgTypeId) {
        buffer.putInt(VERSION);
        buffer.putInt(msgTypeId);
    }

    public int length() {
        return LENGTH;
    }
}
