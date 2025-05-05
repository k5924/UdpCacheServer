package org.example.ucs.messages.command;

import org.example.ucs.messages.shared.StringDecoder;

import java.nio.ByteBuffer;

public final class PutDecoder {
    public static final int MSG_ID = 3;

    public String key(final ByteBuffer buffer, final StringDecoder stringDecoder) {
        return stringDecoder.decode(buffer);
    }

    public String value(final ByteBuffer buffer, final StringDecoder stringDecoder) {
        return stringDecoder.decode(buffer);
    }
}
