package com.github.lc.oss.commons.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NumericEnumCacheTest extends AbstractMockTest {
    private enum TestEnum implements INumericEnum {
        A(1),
        b(3),
        C(5),
        d(7);

        private final int num;

        private TestEnum(int num) {
            this.num = num;
        }

        @Override
        public int getNumber() {
            return this.num;
        }
    }

    @Test
    public void test_values() {
        NumericEnumCache<TestEnum> cache = new NumericEnumCache<>(TestEnum.class);

        Assertions.assertTrue(cache.hasNumber(3));
        Assertions.assertSame(TestEnum.b, cache.byNumber(3));
        Assertions.assertSame(TestEnum.A, cache.tryParse(1));
        Assertions.assertSame(TestEnum.C, cache.tryParseNumber("5"));

        Assertions.assertNull(cache.tryParseNumber(null));
        Assertions.assertNull(cache.tryParseNumber("2"));
        Assertions.assertNull(cache.tryParseNumber("A"));
        Assertions.assertNull(cache.tryParse(4));

        try {
            cache.byNumber(2);
        } catch (IllegalArgumentException ex) {
            Assertions.assertEquals("No enum constant com.github.lc.oss.commons.util.NumericEnumCacheTest.TestEnum with number2", ex.getMessage());
        }
    }
}
