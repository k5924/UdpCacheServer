package org.example.ucs.messages.response;

import org.example.ucs.messages.shared.HeaderEncoder;

import java.nio.ByteBuffer;

public final class PutResponseEncoder {
    public static final int MSG_ID = 3;
    private static final int LENGTH = Integer.BYTES;
    private Status status = null;

    public void encode(final ByteBuffer buffer, final HeaderEncoder headerEncoder) {
        if (buffer.remaining() < headerEncoder.length() + length()) {
            System.err.println("unable to encode response as not enough space in buffer");
            return;
        }
        if (status == null)
        {
            System.err.println("no status was set, skipping encoding information");
            return;
        }
        headerEncoder.encode(buffer, MSG_ID);
        buffer.putInt(status.getValue());
        buffer.flip();
    }

    public PutResponseEncoder setStatus(final Status status)
    {
        this.status = status;
        return this;
    }

    public int length() {
        return LENGTH;
    }

    public enum Status {
        CREATED(1),
        OVERWRITTEN(2),
        UNKNOWN(-1);

        private final int value;

        Status(final int value)
        {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }

        public static Status getStatus(final int value) {
            for (Status status : Status.values())
            {
                if (status.getValue() == value) {
                    return status;
                }
            }
            return UNKNOWN;
        }
    }

}
