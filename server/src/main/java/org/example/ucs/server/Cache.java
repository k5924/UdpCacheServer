package org.example.ucs.server;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class Cache {

    private final ConcurrentMap<String, String> cache = new ConcurrentHashMap<>();

    public boolean put(final String key, final String value) {
        final String previousValue = cache.put(key, value);
        return previousValue == null;
    }

    public String get(final String key) {
        return cache.get(key);
    }

    public boolean delete(final String key) {
        final String previousValue = cache.remove(key);
        return previousValue != null;
    }
}
