package io.github.lc.oss.commons.util;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Basic time limited cache implementation
 */
public class SimpleTimedCache<T> implements TimedCache<T> {
    private final ConcurrentHashMap<String, Cached<T>> cache = new ConcurrentHashMap<>();

    public static class Cached<T> {
        private final long expires;
        private final T value;

        public Cached(T value, long expires) {
            this.value = value;
            this.expires = expires;
        }

        public long getExpires() {
            return this.expires;
        }

        public T getValue() {
            return this.value;
        }
    }

    private final long defaultTimeToLive;

    public SimpleTimedCache(long defaultTimeToLive) {
        this.defaultTimeToLive = defaultTimeToLive;
    }

    @Override
    public void add(String key, T value) {
        this.add(key, value, System.currentTimeMillis() + this.getDefaultTimeToLive());
    }

    @Override
    public void add(String key, T value, long expires) {
        this.cache.put(key, new Cached<T>(value, expires));
    }

    @Override
    public T get(String key) {
        Cached<T> v = this.cache.get(key);
        if (v == null) {
            return null;
        }

        if (v.getExpires() <= System.currentTimeMillis()) {
            this.cache.remove(key);
            return null;
        }

        return v.getValue();
    }

    @Override
    public void clean() {
        final long now = System.currentTimeMillis();
        this.cache.values().removeIf(i -> now >= i.getExpires());
    }

    @Override
    public void clear() {
        this.cache.clear();
    }

    @Override
    public void remove(String key) {
        this.cache.remove(key);
    }

    protected long getDefaultTimeToLive() {
        return this.defaultTimeToLive;
    }
}
