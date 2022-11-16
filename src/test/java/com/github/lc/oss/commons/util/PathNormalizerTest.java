package com.github.lc.oss.commons.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PathNormalizerTest extends AbstractMockTest {
    @Test
    public void test_values() {
        PathNormalizer p = new PathNormalizer();

        String input = null;
        Assertions.assertNull(p.dir(input));

        input = "";
        Assertions.assertSame(input, p.dir(input));

        input = " \t \r \n \t ";
        Assertions.assertEquals("", p.dir(input));

        input = null;
        Assertions.assertEquals("/default/path/", p.dir(input, "/default/path/"));

        input = "";
        Assertions.assertEquals("C:/test/path/", p.dir(input, "C:\\test\\path"));

        input = "C:\\Path\\To/Dir";
        Assertions.assertEquals("C:/Path/To/Dir/", p.dir(input));

        input = "/data/stuff/";
        Assertions.assertEquals("/data/stuff/", p.dir(input));

        input = "data/stuff";
        Assertions.assertEquals("data/stuff/", p.dir(input));
    }
}
