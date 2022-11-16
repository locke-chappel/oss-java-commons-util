package com.github.lc.oss.commons.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TypedEnumCacheTest extends AbstractMockTest {
    private enum TestEnum {
        A,
        b,
        C,
        d
    }

    @Test
    public void test_values() {
        TypedEnumCache<TestEnum, TestEnum> cache = new TypedEnumCache<>(TestEnum.class);
        Set<TestEnum> values = cache.values();
        Set<TestEnum> expected = new HashSet<>(Arrays.asList(TestEnum.values()));
        Assertions.assertNotSame(values, expected);
        Assertions.assertTrue(expected.containsAll(values));
        Assertions.assertTrue(values.containsAll(expected));
        Assertions.assertEquals(expected, values);
    }

    @Test
    public void test_badEnum() {
        try {
            new TypedEnumCache<>(null);
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("Enum class cannot be null.", ex.getMessage());
        }
    }

    @Test
    public void test_caseSensitive() {
        TypedEnumCache<TestEnum, TestEnum> cache = new TypedEnumCache<>(TestEnum.class, true);

        Assertions.assertFalse(cache.hasName(null));
        Assertions.assertTrue(cache.hasName("A"));
        Assertions.assertFalse(cache.hasName("B"));
        Assertions.assertTrue(cache.hasName("b"));
        Assertions.assertTrue(cache.hasName("C"));
        Assertions.assertTrue(cache.hasName("d"));

        Assertions.assertSame(TestEnum.A, cache.byName("A"));
        Assertions.assertSame(TestEnum.b, cache.byName("b"));
        Assertions.assertSame(TestEnum.C, cache.tryParse("C"));
        Assertions.assertSame(TestEnum.d, cache.tryParse("d"));

        Assertions.assertNull(cache.tryParse("c"));
        Assertions.assertNull(cache.tryParse(null));

        try {
            cache.byName(null);
            Assertions.fail("Expected exception");
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("No enum constant com.github.lc.oss.commons.util.TypedEnumCacheTest.TestEnum.null", ex.getMessage());
        }

        try {
            cache.byName("a");
            Assertions.fail("Expected exception");
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("No enum constant com.github.lc.oss.commons.util.TypedEnumCacheTest.TestEnum.a", ex.getMessage());
        }

        try {
            cache.byName("B");
            Assertions.fail("Expected exception");
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("No enum constant com.github.lc.oss.commons.util.TypedEnumCacheTest.TestEnum.B", ex.getMessage());
        }
    }

    @Test
    public void test_caseInsensitive() {
        TypedEnumCache<TestEnum, TestEnum> cache = new TypedEnumCache<>(TestEnum.class, false);

        Assertions.assertFalse(cache.hasName(null));
        Assertions.assertTrue(cache.hasName("A"));
        Assertions.assertTrue(cache.hasName("B"));
        Assertions.assertTrue(cache.hasName("b"));
        Assertions.assertTrue(cache.hasName("C"));
        Assertions.assertTrue(cache.hasName("d"));

        Assertions.assertSame(TestEnum.A, cache.byName("A"));
        Assertions.assertSame(TestEnum.A, cache.byName("a"));
        Assertions.assertSame(TestEnum.b, cache.byName("b"));
        Assertions.assertSame(TestEnum.b, cache.byName("B"));
        Assertions.assertSame(TestEnum.C, cache.tryParse("C"));
        Assertions.assertSame(TestEnum.C, cache.tryParse("c"));
        Assertions.assertSame(TestEnum.d, cache.tryParse("d"));

        Assertions.assertNull(cache.tryParse(null));

        try {
            cache.byName(null);
            Assertions.fail("Expected exception");
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("No enum constant com.github.lc.oss.commons.util.TypedEnumCacheTest.TestEnum.null", ex.getMessage());
        }
    }
}
