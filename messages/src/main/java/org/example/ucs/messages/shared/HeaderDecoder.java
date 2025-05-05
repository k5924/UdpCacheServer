package org.example.ucs.messages.shared;

import java.nio.ByteBuffer;

public final class HeaderDecoder {
    public static final int VERSION = 1;
    private static final int LENGTH = Integer.BYTES * 2;

    public int getVersion(final ByteBuffer buffer) {
        if (buffer.remaining() < Integer.BYTES) {
            System.err.println("not enough data in buffer to decode correctly");
            return Integer.MIN_VALUE;
        }
        return buffer.getInt();
    }

    public int getMessageTypeId(final ByteBuffer buffer) {
        if (buffer.remaining() < Integer.BYTES) {
            System.err.println("not enough data in buffer to decode correctly");
            return Integer.MIN_VALUE;
        }
        return buffer.getInt();
    }

    public int length() {
        return LENGTH;
    }
}
