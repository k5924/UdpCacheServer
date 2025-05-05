package org.example.ucs.server.response;

import org.example.ucs.messages.response.DeleteResponseEncoder;
import org.example.ucs.messages.response.GetResponseEncoder;
import org.example.ucs.messages.response.PongEncoder;
import org.example.ucs.messages.response.PutResponseEncoder;
import org.example.ucs.messages.shared.HeaderEncoder;
import org.example.ucs.messages.shared.StringEncoder;

import java.nio.ByteBuffer;

public final class ResponseEncodingFactory {

    private final ThreadLocal<HeaderEncoder> headerEncoder;

    {
        new ThreadLocal<>();
        headerEncoder = ThreadLocal.withInitial(HeaderEncoder::new);
    }

    private final ThreadLocal<PongEncoder> pongEncoder;

    {
        new ThreadLocal<>();
        pongEncoder = ThreadLocal.withInitial(PongEncoder::new);
    }

    private final ThreadLocal<PutResponseEncoder> putResponseEncoder;

    {
        new ThreadLocal<>();
        putResponseEncoder = ThreadLocal.withInitial(PutResponseEncoder::new);
    }

    private final ThreadLocal<GetResponseEncoder> getResponseEncoder;

    {
        new ThreadLocal<>();
        getResponseEncoder = ThreadLocal.withInitial(GetResponseEncoder::new);
    }

    private final ThreadLocal<DeleteResponseEncoder> deleteResponseEncoder;

    {
        new ThreadLocal<>();
        deleteResponseEncoder = ThreadLocal.withInitial(DeleteResponseEncoder::new);
    }

    private final ThreadLocal<StringEncoder> stringEncoder;

    {
        new ThreadLocal<>();
        stringEncoder = ThreadLocal.withInitial(StringEncoder::new);
    }

    public void encodePong(final ByteBuffer buffer) {
        pongEncoder
                .get()
                .encode(buffer, headerEncoder.get());
    }

    public void encodePutResponse(final ByteBuffer buffer, final PutResponseEncoder.Status status) {
        putResponseEncoder
                .get()
                .setStatus(status)
                .encode(buffer, headerEncoder.get());
    }

    public void encodeGetResponse(final ByteBuffer buffer, final String value) {
        getResponseEncoder
                .get()
                .setValue(value)
                .encode(buffer, headerEncoder.get(), stringEncoder.get());
    }

    public void encodeDeleteResponse(final ByteBuffer buffer, final DeleteResponseEncoder.Status status) {
        deleteResponseEncoder
                .get()
                .setStatus(status)
                .encode(buffer, headerEncoder.get());
    }
}
