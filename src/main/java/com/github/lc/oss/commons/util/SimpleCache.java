package com.github.lc.oss.commons.util;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Basic cache implementation
 */
public class SimpleCache<T> implements Cache<T> {
    private final ConcurrentHashMap<String, T> cache = new ConcurrentHashMap<>();

    @Override
    public void add(String key, T value) {
        this.cache.put(key, value);
    }

    @Override
    public T get(String key) {
        return this.cache.get(key);
    }

    @Override
    public void clear() {
        this.cache.clear();
    }

    @Override
    public void remove(String key) {
        this.cache.remove(key);
    }
}
