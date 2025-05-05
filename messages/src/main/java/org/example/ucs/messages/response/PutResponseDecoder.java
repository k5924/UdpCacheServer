package org.example.ucs.messages.response;

import org.example.ucs.messages.shared.HeaderEncoder;

import java.nio.ByteBuffer;

public final class PutResponseDecoder {
    public static final int MSG_ID = 3;
    private static final int LENGTH = Integer.BYTES;

    public void decode(final ByteBuffer buffer) {
        if (buffer.remaining() < length()) {
            System.err.println("not enough data in buffer to decode message correctly");
            return;
        }
        final int status = buffer.getInt();
        final PutResponseEncoder.Status responseStatus = PutResponseEncoder.Status.getStatus(status);
        if (responseStatus == PutResponseEncoder.Status.UNKNOWN) {
            System.err.println("UNKNOWN status");
            return;
        }
        System.out.println(responseStatus);
    }

    public int length() {
        return LENGTH;
    }
}
