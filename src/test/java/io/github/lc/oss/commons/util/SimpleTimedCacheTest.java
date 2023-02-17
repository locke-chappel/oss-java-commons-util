package io.github.lc.oss.commons.util;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SimpleTimedCacheTest extends AbstractMockTest {
    @Test
    public void test_cleanExpired() {
        String a = "A";
        String b = "B";

        SimpleTimedCache<String> cache = new SimpleTimedCache<>(10000);

        final Map<?, ?> map = this.getField("cache", cache);
        Assertions.assertNotNull(map);
        Assertions.assertTrue(map.isEmpty());

        cache.add("1", a, 0);
        cache.add("2", b);

        Assertions.assertSame(b, cache.get("2"));

        Map<?, ?> map2 = this.getField("cache", cache);
        Assertions.assertNotNull(map2);
        Assertions.assertSame(map, map2);
        Assertions.assertEquals(2, map2.size());
        Assertions.assertTrue(map2.containsKey("1"));
        Assertions.assertTrue(map2.containsKey("2"));

        cache.clean();

        map2 = this.getField("cache", cache);
        Assertions.assertNotNull(map2);
        Assertions.assertSame(map, map2);
        Assertions.assertEquals(1, map2.size());
        Assertions.assertFalse(map2.containsKey("1"));
        Assertions.assertTrue(map2.containsKey("2"));

        Assertions.assertNull(cache.get("1"));
        Assertions.assertSame(b, cache.get("2"));
    }

    @Test
    public void test_clear() {
        String a = "A";
        String b = "B";

        SimpleTimedCache<String> cache = new SimpleTimedCache<>(10000);

        final Map<?, ?> map = this.getField("cache", cache);
        Assertions.assertNotNull(map);
        Assertions.assertTrue(map.isEmpty());

        cache.add("1", a, 0);
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

        Assertions.assertNull(cache.get("a"));
        Assertions.assertNull(cache.get("b"));
    }

    @Test
    public void test_remove() {
        String a = "A";
        String b = "B";

        SimpleTimedCache<String> cache = new SimpleTimedCache<>(10000);

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
    public void test_getExpired() {
        String a = "A";

        SimpleTimedCache<String> cache = new SimpleTimedCache<>(10000);

        cache.add("1", a, System.currentTimeMillis() - 1000);

        Assertions.assertNull(cache.get("1"));
    }

    @Test
    public void test_getNonexistant() {
        SimpleTimedCache<String> cache = new SimpleTimedCache<>(10000);

        Assertions.assertNull(cache.get("junk"));
    }
}
