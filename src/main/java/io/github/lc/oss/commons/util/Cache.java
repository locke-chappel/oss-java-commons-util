package io.github.lc.oss.commons.util;

public interface Cache<T> {
    /**
     * Add an item to this cache
     */
    void add(String key, T value);

    /**
     * Get the item from the cache, may return <code>null</code>
     */
    T get(String key);

    /**
     * Removes all items from the cache
     */
    void clear();

    /**
     * Removes the specified item from the cache
     */
    void remove(String key);
}
