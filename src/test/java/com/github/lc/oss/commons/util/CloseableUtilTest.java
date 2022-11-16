package com.github.lc.oss.commons.util;

import java.io.Closeable;
import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CloseableUtilTest extends AbstractMockTest {
    @Test
    public void test_close_null() {
        CloseableUtil.close(null);
    }

    @Test
    public void test_close_error() {
        try {
            CloseableUtil.close(new Closeable() {
                @Override
                public void close() throws IOException {
                    throw new IOException("Boom!");
                }
            });
            Assertions.fail("Expected exception");
        } catch (RuntimeException ex) {
            Assertions.assertEquals("Error closing closable", ex.getMessage());
            Assertions.assertEquals("Boom!", ex.getCause().getMessage());
        }
    }

    @Test
    public void test_close() {
        CloseableUtil.close(new Closeable() {
            @Override
            public void close() throws IOException {
            }
        });
    }
}
