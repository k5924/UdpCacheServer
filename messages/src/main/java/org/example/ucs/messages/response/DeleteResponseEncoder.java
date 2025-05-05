package org.example.ucs.messages.response;

import org.example.ucs.messages.shared.HeaderEncoder;

import java.nio.ByteBuffer;

public final class DeleteResponseEncoder {
    public static final int MSG_ID = 2;
    private static final int LENGTH = Integer.BYTES;
    private Status status = null;

    public DeleteResponseEncoder setStatus(final Status status) {
        this.status = status;
        return this;
    }

    public void encode(final ByteBuffer buffer, final HeaderEncoder headerEncoder) {
        if (status == null) {
            System.err.println("unable to encode delete response as status was not set");
            return;
        }

        if (buffer.remaining() < headerEncoder.length() + length()) {
            System.err.println("unable to encode delete response as not enough space in buffer");
            return;
        }
        headerEncoder.encode(buffer, MSG_ID);
        buffer.putInt(status.getValue());
        buffer.flip();
    }

    public int length() {
        return LENGTH;
    }

    public enum Status {
        DELETED(1),
        UNKNOWN(-1);

        private final int value;

        Status(final int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public static Status getStatus(final int value) {
            for (Status status : Status.values()) {
                if (status.getValue() == value) {
                    return status;
                }
            }
            return UNKNOWN;
        }
    }
}
