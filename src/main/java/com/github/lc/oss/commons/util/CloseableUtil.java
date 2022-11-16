package com.github.lc.oss.commons.util;

import java.io.Closeable;
import java.io.IOException;

public class CloseableUtil {
    public static void close(Closeable closeable) {
        if (closeable == null) {
            return;
        }

        try {
            closeable.close();
        } catch (IOException ex) {
            throw new RuntimeException("Error closing closable", ex);
        }
    }

    private CloseableUtil() {
    }
}
