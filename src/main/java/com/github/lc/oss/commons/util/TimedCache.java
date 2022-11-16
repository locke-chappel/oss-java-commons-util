package com.github.lc.oss.commons.util;

public interface TimedCache<T> extends Cache<T> {
    /**
     * Adds an items to the cache with a specified expiration time.
     */
    void add(String key, T value, long expires);

    /**
     * Removes any expired items from the cache
     */
    void clean();
}
