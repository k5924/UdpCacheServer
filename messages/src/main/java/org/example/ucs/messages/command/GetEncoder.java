package org.example.ucs.messages.command;

import org.example.ucs.messages.shared.HeaderEncoder;
import org.example.ucs.messages.shared.StringEncoder;

import java.nio.ByteBuffer;

public final class GetEncoder {

    public static final int MSG_ID = 1;
    private String key = null;

    public GetEncoder setKey(final String key) {
        this.key = key;
        return this;
    }

    public void encode(final ByteBuffer buffer, final HeaderEncoder headerEncoder, final StringEncoder stringEncoder) {
        if (buffer.remaining() < headerEncoder.length() + stringEncoder.getLength(key)) {
            System.err.println("unable to encode get as not enough space in buffer");
            return;
        }
        headerEncoder.encode(buffer, MSG_ID);
        stringEncoder.encode(buffer, key);
        buffer.flip();
    }


}
