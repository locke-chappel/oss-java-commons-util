package io.github.lc.oss.commons.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TimeIntervalParserTest extends AbstractMockTest {
    @Test
    public void test_errors() {
        TimeIntervalParser parser = new TimeIntervalParser();

        try {
            parser.parse(null);
            Assertions.fail();
        } catch (NullPointerException ex) {
            Assertions.assertEquals("Invalid interval value, must be in the format <number><unit=ms, s, m, h, d, w>", ex.getMessage());
        }

        try {
            parser.parse("");
            Assertions.fail();
        } catch (RuntimeException ex) {
            Assertions.assertEquals("Invalid interval value, must be in the format <number><unit=ms, s, m, h, d, w>", ex.getMessage());
        }

        try {
            parser.parse(" \t \r \n \t ");
            Assertions.fail();
        } catch (RuntimeException ex) {
            Assertions.assertEquals("Invalid interval value, must be in the format <number><unit=ms, s, m, h, d, w>", ex.getMessage());
        }

        try {
            parser.parse("-1s");
            Assertions.fail();
        } catch (RuntimeException ex) {
            Assertions.assertEquals("Invalid interval value, must be in the format <number><unit=ms, s, m, h, d, w>", ex.getMessage());
        }

        try {
            parser.parse("1y");
            Assertions.fail();
        } catch (RuntimeException ex) {
            Assertions.assertEquals("Invalid interval value, unknown unit. Valid units are ms, s, m, h, d, or w.", ex.getMessage());
        }
    }

    @Test
    public void test_values() {
        TimeIntervalParser parser = new TimeIntervalParser();

        Assertions.assertEquals(0, parser.parse("0"));
        Assertions.assertEquals(75, parser.parse("75"));
        Assertions.assertEquals(0, parser.parse("0ms"));
        Assertions.assertEquals(1, parser.parse("1ms"));
        Assertions.assertEquals(1000, parser.parse("1000ms"));
        Assertions.assertEquals(1000, parser.parse("1s"));
        Assertions.assertEquals(3000, parser.parse("3s"));
        Assertions.assertEquals(60000, parser.parse("60s"));
        Assertions.assertEquals(60000, parser.parse("1m"));
        Assertions.assertEquals(300000, parser.parse("5m"));
        Assertions.assertEquals(3600000, parser.parse("60m"));
        Assertions.assertEquals(3600000, parser.parse("1h"));
        Assertions.assertEquals(86400000, parser.parse("24h"));
        Assertions.assertEquals(86400000, parser.parse("1d"));
        Assertions.assertEquals(259200000, parser.parse("3d"));
        Assertions.assertEquals(604800000, parser.parse("7d"));
        Assertions.assertEquals(604800000, parser.parse("1w"));
        Assertions.assertEquals(1209600000, parser.parse("2w"));
    }
}
