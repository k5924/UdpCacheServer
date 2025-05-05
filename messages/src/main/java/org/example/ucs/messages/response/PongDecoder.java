package org.example.ucs.messages.response;

import java.nio.ByteBuffer;

public final class PongDecoder  {

    public static final int MSG_ID = Integer.MAX_VALUE;

    public void decode(final ByteBuffer buffer)
    {
        System.out.println("pong message received");
    }
}
