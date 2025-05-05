package org.example.ucs.messages.command;

import org.example.ucs.messages.shared.StringDecoder;

import java.nio.ByteBuffer;

public final class GetDecoder {
    public static final int MSG_ID = 1;

    public String decodeKey(final ByteBuffer buffer, final StringDecoder stringDecoder) {
        return stringDecoder.decode(buffer);
    }
}
