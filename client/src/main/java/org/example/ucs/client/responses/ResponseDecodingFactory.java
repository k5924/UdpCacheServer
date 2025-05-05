package org.example.ucs.client.responses;

import org.example.ucs.messages.response.*;
import org.example.ucs.messages.shared.HeaderDecoder;
import org.example.ucs.messages.shared.StringDecoder;

import java.nio.ByteBuffer;

public final class ResponseDecodingFactory {

    private final HeaderDecoder headerDecoder = new HeaderDecoder();
    private final PongDecoder pongDecoder = new PongDecoder();
    private final PutResponseDecoder putResponseDecoder = new PutResponseDecoder();
    private final GetResponseDecoder getResponseDecoder = new GetResponseDecoder();
    private final StringDecoder stringDecoder = new StringDecoder();
    private final DeleteResponseDecoder deleteResponseDecoder = new DeleteResponseDecoder();

    public void handle(final ByteBuffer buffer) {
        buffer.flip();
        if (buffer.remaining() < headerDecoder.length()) {
            System.err.println("unable to decode header information as not enough bytes in buffer");
            throw new RuntimeException();
        }
        final int version = headerDecoder.getVersion(buffer);
        final int messageTypeId = headerDecoder.getMessageTypeId(buffer);
        if (version != HeaderDecoder.VERSION) {
            System.err.println("encountered version " + version + " of message type " + messageTypeId + " skipped decode");
            throw new RuntimeException();
        }
        switch (messageTypeId)
        {
            case PongDecoder.MSG_ID -> handlePongResponse(buffer);
            case PutResponseDecoder.MSG_ID -> handlePutResponse(buffer);
            case GetResponseDecoder.MSG_ID -> handleGetResponse(buffer);
            case DeleteResponseDecoder.MSG_ID -> handleDeleteResponse(buffer);
        }
    }

    private void handleDeleteResponse(final ByteBuffer buffer) {
        deleteResponseDecoder.decode(buffer);
    }

    private void handleGetResponse(ByteBuffer buffer) {
        getResponseDecoder.decode(buffer, stringDecoder);
    }

    private void handlePutResponse(final ByteBuffer buffer) {
        putResponseDecoder.decode(buffer);
    }

    private void handlePongResponse(final ByteBuffer buffer) {
        pongDecoder.decode(buffer);
    }
}
