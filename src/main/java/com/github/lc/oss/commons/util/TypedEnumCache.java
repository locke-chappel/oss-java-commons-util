package com.github.lc.oss.commons.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class TypedEnumCache<EnumType extends Enum<EnumType>, Interface> {
    protected final String className;
    private final Map<String, Interface> cache;
    private final Set<Interface> values;

    public TypedEnumCache(Class<EnumType> clazz) {
        this(clazz, true);
    }

    @SuppressWarnings("unchecked")
    public TypedEnumCache(Class<EnumType> clazz, boolean caseSensitive) {
        if (clazz == null) {
            throw new IllegalArgumentException("Enum class cannot be null.");
        }

        this.className = clazz.getCanonicalName();
        Map<String, Interface> map = caseSensitive ? new HashMap<>()
                : new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
        Set<Interface> set = new HashSet<>();
        for (EnumType value : clazz.getEnumConstants()) {
            map.put(value.name(), (Interface) value);
            set.add((Interface) value);
        }
        this.cache = Collections.unmodifiableMap(map);
        this.values = Collections.unmodifiableSet(set);
    }

    /**
     * Performs an O(1) lookup for the enum value by name.
     *
     * @throws IllegalArgumentException if name is not found in the cache (implying
     *                                  name is not a valid value for the enum)
     */
    public Interface byName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("No enum constant " + this.className + ".null");
        }

        Interface value = this.cache.get(name);
        if (value == null) {
            throw new IllegalArgumentException("No enum constant " + this.className + "." + name);
        }
        return value;
    }

    /**
     * Returns <code>true</code> if the cache contains an entry for the given name.
     */
    public boolean hasName(String name) {
        if (name == null) {
            return false;
        }

        return this.cache.containsKey(name);
    }

    /**
     * Performs an O(1) lookup for the enum value by name. Returns <code>null</code>
     * if the value is not found in the cache (implying name is not a valid value
     * for the enum).
     */
    public Interface tryParse(String name) {
        if (name == null) {
            return null;
        }

        return this.cache.get(name);
    }

    /**
     * Returns an unmodifiable set of all values in the enum.
     */
    public Set<Interface> values() {
        return this.values;
    }
}
