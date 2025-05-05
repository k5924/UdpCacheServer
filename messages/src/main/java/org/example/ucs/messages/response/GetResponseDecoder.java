package org.example.ucs.messages.response;

import org.example.ucs.messages.shared.StringDecoder;

import java.nio.ByteBuffer;

public final class GetResponseDecoder {
    public static final int MSG_ID = 1;
    private static final int LENGTH = Integer.BYTES;

    public void decode(final ByteBuffer buffer, final StringDecoder stringDecoder) {
        if (buffer.remaining() < LENGTH) {
            System.err.println("unable to decode message as not enough bytes in buffer");
            return;
        }
        final String value = stringDecoder.decode(buffer);
        if (value.isBlank()) {
            System.err.println("value returned was empty");
        } else {
            System.out.println("value returned was " + value);
        }
    }

    public int length() {
        return LENGTH;
    }
}
