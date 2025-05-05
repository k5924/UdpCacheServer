package org.example.ucs.client.commands;

import org.example.ucs.messages.command.DeleteEncoder;
import org.example.ucs.messages.command.GetEncoder;
import org.example.ucs.messages.command.PingEncoder;
import org.example.ucs.messages.command.PutEncoder;
import org.example.ucs.messages.shared.HeaderEncoder;
import org.example.ucs.messages.shared.StringEncoder;

import java.nio.ByteBuffer;

public final class CommandEncodingFactory {

    private final HeaderEncoder headerEncoder = new HeaderEncoder();
    private final StringEncoder stringEncoder = new StringEncoder();
    private final GetEncoder getEncoder = new GetEncoder();
    private final DeleteEncoder deleteEncoder = new DeleteEncoder();
    private final PutEncoder putEncoder = new PutEncoder();
    private final PingEncoder pingEncoder = new PingEncoder();

    public void encodePing(final ByteBuffer buffer) {
        pingEncoder.encode(buffer, headerEncoder);
    }

    public void encodeGetRequest(final ByteBuffer buffer, final String key) {
        getEncoder.setKey(key)
                .encode(buffer, headerEncoder, stringEncoder);
    }

    public void encodePutRequest(final ByteBuffer buffer, final String key, final String value) {
        putEncoder.setKey(key)
                .setValue(value)
                .encode(buffer, headerEncoder, stringEncoder);
    }

    public void encodeDeleteRequest(final ByteBuffer buffer, final String key) {
        deleteEncoder.setKey(key)
                .encode(buffer, headerEncoder, stringEncoder);
    }
}
