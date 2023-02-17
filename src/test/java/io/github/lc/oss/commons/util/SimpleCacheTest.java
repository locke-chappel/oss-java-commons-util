package io.github.lc.oss.commons.util;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SimpleCacheTest extends AbstractMockTest {
    @Test
    public void test_clear() {
        String a = "A";
        String b = "B";

        SimpleCache<String> cache = new SimpleCache<>();

        final Map<?, ?> map = this.getField("cache", cache);
        Assertions.assertNotNull(map);
        Assertions.assertTrue(map.isEmpty());

        cache.add("1", a);
        cache.add("2", b);

        Assertions.assertSame(b, cache.get("2"));

        Map<?, ?> map2 = this.getField("cache", cache);
        Assertions.assertNotNull(map2);
        Assertions.assertSame(map, map2);
        Assertions.assertEquals(2, map2.size());

        cache.clear();

        map2 = this.getField("cache", cache);
        Assertions.assertNotNull(map2);
        Assertions.assertSame(map, map2);
        Assertions.assertTrue(map2.isEmpty());

        Assertions.assertNull(cache.get("1"));
        Assertions.assertNull(cache.get("2"));
    }

    @Test
    public void test_remove() {
        String a = "A";
        String b = "B";

        SimpleCache<String> cache = new SimpleCache<>();

        final Map<?, ?> map = this.getField("cache", cache);
        Assertions.assertNotNull(map);
        Assertions.assertTrue(map.isEmpty());

        cache.add("1", a);
        cache.add("2", b);

        Assertions.assertSame(b, cache.get("2"));

        Map<?, ?> map2 = this.getField("cache", cache);
        Assertions.assertNotNull(map2);
        Assertions.assertSame(map, map2);
        Assertions.assertEquals(2, map2.size());

        cache.remove("2");

        map2 = this.getField("cache", cache);
        Assertions.assertNotNull(map2);
        Assertions.assertSame(map, map2);
        Assertions.assertEquals(1, map2.size());

        Assertions.assertSame(a, cache.get("1"));
        Assertions.assertNull(cache.get("2"));
    }

    @Test
    public void test_getNonexistant() {
        SimpleCache<String> cache = new SimpleCache<>();

        Assertions.assertNull(cache.get("junk"));
    }
}
