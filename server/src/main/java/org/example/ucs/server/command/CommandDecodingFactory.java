package org.example.ucs.server.command;

import org.example.ucs.messages.command.*;
import org.example.ucs.messages.response.PutResponseEncoder;
import org.example.ucs.messages.shared.HeaderDecoder;
import org.example.ucs.messages.shared.StringDecoder;
import org.example.ucs.server.Cache;
import org.example.ucs.server.response.ResponseEncodingFactory;

import java.nio.ByteBuffer;

import static org.example.ucs.messages.response.DeleteResponseEncoder.Status.DELETED;
import static org.example.ucs.messages.response.DeleteResponseEncoder.Status.UNKNOWN;
import static org.example.ucs.messages.shared.StringDecoder.EMPTY_STRING;

public final class CommandDecodingFactory {

    private final ThreadLocal<PingDecoder> pingDecoder;

    {
        new ThreadLocal<>();
        pingDecoder = ThreadLocal.withInitial(PingDecoder::new);
    }

    private final ThreadLocal<PutDecoder> putDecoder;

    {
        new ThreadLocal<>();
        putDecoder = ThreadLocal.withInitial(PutDecoder::new);
    }

    private final ThreadLocal<GetDecoder> getDecoder;

    {
        new ThreadLocal<>();
        getDecoder = ThreadLocal.withInitial(GetDecoder::new);
    }

    private final ThreadLocal<DeleteDecoder> deleteDecoder;

    {
        new ThreadLocal<>();
        deleteDecoder = ThreadLocal.withInitial(DeleteDecoder::new);
    }

    private final ThreadLocal<HeaderDecoder> headerDecoder;

    {
        new ThreadLocal<>();
        headerDecoder = ThreadLocal.withInitial(HeaderDecoder::new);
    }

    private final ThreadLocal<StringDecoder> stringDecoder;

    {
        new ThreadLocal<>();
        stringDecoder = ThreadLocal.withInitial(StringDecoder::new);
    }

    private final Cache cache;
    private final ResponseEncodingFactory responseEncodingFactory;

    public CommandDecodingFactory(final Cache cache, final ResponseEncodingFactory responseEncodingFactory) {
        this.cache = cache;
        this.responseEncodingFactory = responseEncodingFactory;
    }

    public void handle(final ByteBuffer buffer) {
        buffer.flip();
        if (buffer.remaining() < headerDecoder.get().length()) {
            System.err.println("not enough data in buffer to decode header information");
            return;
        }
        final int version = headerDecoder.get().getVersion(buffer);
        final int messageTypeId = headerDecoder.get().getMessageTypeId(buffer);
        if (version != HeaderDecoder.VERSION) {
            System.err.println("encountered version " + version + " of message type " + messageTypeId + " skipped decode");
            return;
        }
        switch (messageTypeId)
        {
            case PingDecoder.MSG_ID -> handlePingRequest(buffer);
            case PutDecoder.MSG_ID -> handlePutRequest(buffer);
            case GetDecoder.MSG_ID -> handleGetRequest(buffer);
            case DeleteEncoder.MSG_ID -> handleDeleteRequest(buffer);
        }
    }

    private void handleDeleteRequest(final ByteBuffer buffer) {
        final String key = deleteDecoder.get().decodeKey(buffer, stringDecoder.get());
        buffer.compact();
        if (key.isBlank()) {
            responseEncodingFactory.encodeDeleteResponse(buffer, UNKNOWN);
            return;
        }
        final boolean wasDeleted = cache.delete(key);
        if (wasDeleted) {
            responseEncodingFactory.encodeDeleteResponse(buffer, DELETED);
        } else {
            responseEncodingFactory.encodeDeleteResponse(buffer, UNKNOWN);
        }
    }

    private void handleGetRequest(final ByteBuffer buffer) {
        final String key = getDecoder.get().decodeKey(buffer, stringDecoder.get());
        buffer.compact();
        if (key.isBlank()) {
            responseEncodingFactory.encodeGetResponse(buffer, EMPTY_STRING);
            return;
        }
        final String value = cache.get(key);
        System.out.println("value is " + value);
        responseEncodingFactory.encodeGetResponse(buffer, value);
    }

    private void handlePutRequest(final ByteBuffer buffer) {
        final String key = putDecoder.get().key(buffer, stringDecoder.get());
        final String value = putDecoder.get().value(buffer, stringDecoder.get());
        System.out.println("put key is " + key + " value is " + value);
        buffer.compact();
        if (key.isBlank() || value.isBlank()) {
            responseEncodingFactory.encodePutResponse(buffer, PutResponseEncoder.Status.UNKNOWN);
            return;
        }
        final boolean created = cache.put(key, value);
        if (created) {
            responseEncodingFactory.encodePutResponse(buffer, PutResponseEncoder.Status.CREATED);
        } else {
            responseEncodingFactory.encodePutResponse(buffer, PutResponseEncoder.Status.OVERWRITTEN);
        }
    }

    private void handlePingRequest(final ByteBuffer buffer) {
        pingDecoder.get().decode(buffer);
        buffer.compact();
        responseEncodingFactory.encodePong(buffer);
    }
}
