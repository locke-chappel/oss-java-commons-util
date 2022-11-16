package com.github.lc.oss.commons.util;

import java.util.Arrays;

public class BitUtils {
    public static byte[] set(boolean state, int index, byte... value) {
        int max = value.length * 8 - 1;
        if (index < 0 || index > max) {
            throw new IllegalArgumentException(String.format("Index must between 0 and %d inclusive", max));
        }

        byte[] b = Arrays.copyOf(value, value.length);
        int byteIndex = value.length - index / 8 - 1;
        int bitIndex = index % 8;
        if (state) {
            b[byteIndex] |= 1 << bitIndex;
        } else {
            b[byteIndex] &= ~(1 << bitIndex);
        }
        return b;
    }

    public static byte set(boolean state, int index, byte value) {
        if (index < 0 || index > 7) {
            throw new IllegalArgumentException("Index must between 0 and 7 inclusive");
        }

        if (state) {
            value |= 1 << index;
        } else {
            value &= ~(1 << index);
        }

        return value;
    }

    public static boolean get(int index, byte... value) {
        int max = value.length * 8 - 1;
        if (index < 0 || index > max) {
            throw new IllegalArgumentException(String.format("Index must between 0 and %d inclusive", max));
        }

        int byteIndex = value.length - index / 8 - 1;
        int bitIndex = index % 8;
        return (value[byteIndex] >> bitIndex & 1) == 1;
    }

    private BitUtils() {
    }
}
