package io.github.lc.oss.commons.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class NumericEnumCache<EnumType extends Enum<EnumType> & INumericEnum>
        extends TypedEnumCache<EnumType, EnumType> {
    private final Map<Integer, EnumType> numberCache;

    public NumericEnumCache(Class<EnumType> clazz) {
        this(clazz, true);
    }

    public NumericEnumCache(Class<EnumType> clazz, boolean caseSensitive) {
        super(clazz, caseSensitive);

        Map<Integer, EnumType> map = new HashMap<>();
        for (EnumType value : this.values()) {
            map.put(value.getNumber(), value);
        }
        this.numberCache = Collections.unmodifiableMap(map);
    }

    /**
     * Performs an O(1) lookup for the enum value by number.
     *
     * @throws IllegalArgumentException if number is not found in the cache
     *                                  (implying number is not a valid value for
     *                                  the enum)
     */
    public EnumType byNumber(int number) {
        EnumType value = this.numberCache.get(number);
        if (value == null) {
            throw new IllegalArgumentException(
                    "No enum constant " + this.className + " with number" + Integer.toString(number));
        }
        return value;
    }

    /**
     * Returns <code>true</code> if the cache contains an entry for the given
     * number.
     */
    public boolean hasNumber(int number) {
        return this.numberCache.containsKey(number);
    }

    /**
     * Performs an O(1) lookup for the enum value by number. Returns
     * <code>null</code> if the value is not found in the cache (implying number is
     * not a valid value for the enum).
     */
    public EnumType tryParse(int number) {
        return this.numberCache.get(number);
    }

    /**
     * Performs an O(1) lookup for the enum value by number, first converting the
     * String number into an integer then calls {@link #tryParse(int)}. Returns
     * <code>null</code> is number is not a valid number string.
     */
    public EnumType tryParseNumber(String number) {
        if (number == null) {
            return null;
        }

        try {
            Integer intNumber = Integer.valueOf(number, 10);
            return this.tryParse(intNumber);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
