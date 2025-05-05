package org.example.ucs.messages.response;

import org.example.ucs.messages.shared.HeaderEncoder;
import org.example.ucs.messages.shared.StringEncoder;

import java.nio.ByteBuffer;

public final class GetResponseEncoder {
    public static final int MSG_ID = 1;
    private static final int LENGTH = Integer.BYTES;
    private String value = null;

    public void encode(final ByteBuffer buffer, final HeaderEncoder headerEncoder, final StringEncoder stringEncoder) {
        if (buffer.remaining() < stringEncoder.getLength(value) + headerEncoder.length() + length()) {
            System.err.println("unable to encode get response as not enough space in buffer");
            return;
        }
        headerEncoder.encode(buffer, MSG_ID);
        stringEncoder.encode(buffer, value);
        buffer.flip();
    }

    public GetResponseEncoder setValue(final String value) {
        this.value = value;
        return this;
    }

    public int length() {
        return LENGTH;
    }
}
