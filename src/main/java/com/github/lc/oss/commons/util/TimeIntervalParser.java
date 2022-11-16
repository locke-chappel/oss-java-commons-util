package com.github.lc.oss.commons.util;

import java.util.regex.Pattern;

public class TimeIntervalParser {
    private static final Pattern IS_NUMBER = Pattern.compile("^\\d+\\D*$");

    public long parse(String interval) {
        if (interval == null || !TimeIntervalParser.IS_NUMBER.matcher(interval).matches()) {
            throw new NullPointerException("Invalid interval value, must be in the format <number><unit=ms, s, m, h, d, w>");
        }

        String[] parts = interval.split("(?<=\\d)(?=\\D)");
        if (parts.length != 2) {
            return Long.valueOf(interval);
        }

        long value = Long.valueOf(parts[0]);
        switch (parts[1]) {
            case "s":
                return value * 1000l;
            case "m":
                return value * 1000l * 60l;
            case "h":
                return value * 1000l * 60l * 60l;
            case "d":
                return value * 1000l * 60l * 60l * 24l;
            case "w":
                return value * 1000l * 60l * 60l * 24l * 7l;
            case "ms":
                return value;
            default:
                throw new RuntimeException("Invalid interval value, unknown unit. Valid units are ms, s, m, h, d, or w.");
        }
    }
}
