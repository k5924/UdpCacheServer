package org.example.ucs.messages.response;

import java.nio.ByteBuffer;

public final class DeleteResponseDecoder {
    public static final int MSG_ID = 2;
    private static final int LENGTH = Integer.BYTES;

    public void decode(final ByteBuffer buffer) {
        if (buffer.remaining() < length()) {
            System.err.println("unable to decode message as not enough data in buffer");
            return;
        }
        final int value = buffer.getInt();
        final DeleteResponseEncoder.Status status = DeleteResponseEncoder.Status.getStatus(value);
        if (status == DeleteResponseEncoder.Status.UNKNOWN) {
            System.err.println("unknown was returned from delete request");
            return;
        }
        System.out.println("status of delete was " + status);
    }

    public int length() {
        return LENGTH;
    }
}
