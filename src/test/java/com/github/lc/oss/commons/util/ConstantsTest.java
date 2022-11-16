package com.github.lc.oss.commons.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ConstantsTest extends AbstractMockTest {
    @Test
    public void test_valueCheck_fileSizes() {
        Assertions.assertEquals(1024, Constants.FileSizes.KB);
        Assertions.assertEquals(1024 * 1024, Constants.FileSizes.MB);
        Assertions.assertEquals(1024 * 1024 * 1024, Constants.FileSizes.GB);
        Assertions.assertEquals(1024l * 1024l * 1024l * 1024l, Constants.FileSizes.TB);
    }
}
