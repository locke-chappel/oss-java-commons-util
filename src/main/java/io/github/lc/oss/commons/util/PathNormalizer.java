package io.github.lc.oss.commons.util;

public class PathNormalizer {
    public String dir(String path) {
        return this.dir(path, null);
    }

    public String dir(String path, String defaultValue) {
        String tmp = path;
        if (tmp == null) {
            return defaultValue != null ? this.dir(defaultValue) : tmp;
        }
        tmp = tmp.trim();
        if (tmp.equals("")) {
            return defaultValue != null ? this.dir(defaultValue) : tmp;
        }

        tmp = tmp.replace("\\", "/");
        if (!tmp.endsWith("/")) {
            tmp += "/";
        }
        return tmp;
    }
}
