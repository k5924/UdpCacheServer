package org.example.ucs.messages.shared;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public final class StringDecoder {

    public static final String EMPTY_STRING = "";

    public String decode(final ByteBuffer buffer) {
        final int length = buffer.getInt();
        if (buffer.remaining() < length) {
            System.err.println("buffer doesnt have enough space in it to contain all data that was sent");
            return EMPTY_STRING;
        }
        if (length > 0) {
            final byte[] byteArr = new byte[length];
            buffer.get(byteArr);
            return new String(byteArr, StandardCharsets.UTF_8);
        }
        return EMPTY_STRING;
    }
}
