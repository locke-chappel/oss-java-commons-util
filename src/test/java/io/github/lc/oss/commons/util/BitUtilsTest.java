package io.github.lc.oss.commons.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BitUtilsTest extends AbstractMockTest {
    @Test
    public void test_getset_single() {
        byte value = 0b00000000;

        Assertions.assertEquals((byte) 0b00000000, value);

        for (int i = 0; i < 8; i++) {
            Assertions.assertFalse(BitUtils.get(i, value));
            value = BitUtils.set(true, i, value);
            Assertions.assertTrue(BitUtils.get(i, value));
            value = BitUtils.set(false, i, value);
            Assertions.assertFalse(BitUtils.get(i, value));
        }

        value = BitUtils.set(true, 7, value);
        Assertions.assertTrue(BitUtils.get(7, value));
        Assertions.assertEquals((byte) 0b10000000, value);
    }

    @Test
    public void test_getset_array() {
        final byte[] original = new byte[] { 0b00000000, 0b00000000 };

        Assertions.assertEquals((byte) 0b00000000, original[0]);
        Assertions.assertEquals((byte) 0b00000000, original[0]);

        byte[] value = original;
        for (int i = 0; i < 16; i++) {
            Assertions.assertFalse(BitUtils.get(i, value));
            value = BitUtils.set(true, i, value);
            Assertions.assertNotSame(value, original);
            Assertions.assertTrue(BitUtils.get(i, value));
            Assertions.assertFalse(BitUtils.get(i, original));
            Assertions.assertEquals((byte) 0b00000000, original[0]);
            Assertions.assertEquals((byte) 0b00000000, original[0]);
            value = BitUtils.set(false, i, value);
            Assertions.assertNotSame(value, original);
            Assertions.assertFalse(BitUtils.get(i, value));
            Assertions.assertFalse(BitUtils.get(i, original));

            Assertions.assertEquals((byte) 0b00000000, original[0]);
            Assertions.assertEquals((byte) 0b00000000, original[0]);
        }

        value = BitUtils.set(true, 7, value);
        Assertions.assertNotSame(value, original);
        Assertions.assertTrue(BitUtils.get(7, value));
        Assertions.assertEquals((byte) 0b00000000, value[0]);
        Assertions.assertEquals((byte) 0b10000000, value[1]);

        Assertions.assertEquals((byte) 0b00000000, original[0]);
        Assertions.assertEquals((byte) 0b00000000, original[0]);
    }

    @Test
    public void test_errors_single() {
        byte value = 0b00000000;

        try {
            BitUtils.set(true, -1, value);
            Assertions.fail("Expected exception");
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("Index must between 0 and 7 inclusive", ex.getMessage());
        }

        try {
            BitUtils.set(true, 8, value);
            Assertions.fail("Expected exception");
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("Index must between 0 and 7 inclusive", ex.getMessage());
        }
    }

    @Test
    public void test_errors_array() {
        byte[] value = new byte[] { 0b00000000, 0b00000000 };

        try {
            BitUtils.set(true, -1, value);
            Assertions.fail("Expected exception");
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("Index must between 0 and 15 inclusive", ex.getMessage());
        }

        try {
            BitUtils.set(true, 16, value);
            Assertions.fail("Expected exception");
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("Index must between 0 and 15 inclusive", ex.getMessage());
        }

        value = new byte[] { 0b00000000 };

        try {
            BitUtils.set(true, -1, value);
            Assertions.fail("Expected exception");
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("Index must between 0 and 7 inclusive", ex.getMessage());
        }

        try {
            BitUtils.set(true, 8, value);
            Assertions.fail("Expected exception");
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("Index must between 0 and 7 inclusive", ex.getMessage());
        }
    }

    @Test
    public void test_errors_get() {
        byte value = 0b00000000;
        byte[] array = new byte[] { 0b0000000, 0b1111111 };

        try {
            BitUtils.get(-1, value);
            Assertions.fail("Expected exception");
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("Index must between 0 and 7 inclusive", ex.getMessage());
        }

        try {
            BitUtils.get(8, value);
            Assertions.fail("Expected exception");
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("Index must between 0 and 7 inclusive", ex.getMessage());
        }

        try {
            BitUtils.get(-1, array);
            Assertions.fail("Expected exception");
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("Index must between 0 and 15 inclusive", ex.getMessage());
        }

        try {
            BitUtils.get(16, array);
            Assertions.fail("Expected exception");
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("Index must between 0 and 15 inclusive", ex.getMessage());
        }
    }
}
