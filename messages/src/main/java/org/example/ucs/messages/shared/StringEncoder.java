package org.example.ucs.messages.shared;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

public final class StringEncoder {

    public void encode(final ByteBuffer buffer, final String string) {
        if (string != null && !string.isBlank()) {
            final byte[] byteArr = string.getBytes(StandardCharsets.UTF_8);
            buffer.putInt(byteArr.length);
            buffer.put(byteArr);
        } else {
            buffer.putInt(0);
        }
    }

    public int getLength(final String value) {
        if (value == null || value.isBlank()) {
            return 0;
        }
        return value.length();
    }
}
