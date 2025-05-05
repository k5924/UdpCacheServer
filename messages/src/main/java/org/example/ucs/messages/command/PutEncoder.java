package org.example.ucs.messages.command;

import org.example.ucs.messages.shared.HeaderEncoder;
import org.example.ucs.messages.shared.StringEncoder;

import java.nio.ByteBuffer;

public final class PutEncoder {

    public static final int MSG_ID = 3;

    public String key = null;

    public String value = null;

    public PutEncoder setKey(final String key) {
        this.key = key;
        return this;
    }

    public PutEncoder setValue(final String value) {
        this.value = value;
        return this;
    }

    public void encode(final ByteBuffer buffer, final HeaderEncoder headerEncoder, final StringEncoder stringEncoder) {
        if (headerEncoder.length() + stringEncoder.getLength(key) + stringEncoder.getLength(value) > buffer.remaining())
        {
            System.err.println("unable to encode put of key " + key + " value of " + value + " as message is too big,"
                    + " skipping");
            return;
        }
        headerEncoder.encode(buffer, MSG_ID);
        stringEncoder.encode(buffer, key);
        stringEncoder.encode(buffer, value);
        buffer.flip();
        System.out.println("encoded put message");
    }
}
