package cachemap;

import org.junit.Before;
import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.Assert.*;

/**
 * Created by mike on 13.06.17.
 */
public class CacheMapImplTest {
    private CacheMap<Integer, String> cache = new CacheMapImpl<>();
    private final static long ONE_SECOND = 1000;
    private final Integer DEFAULT_KEY = 777;
    private final String DEFAULT_VALUE = "TEST_STRING";

    @Before //runs BEFORE EACH TEST>
    public void preStart() throws Exception {
        cache.clear();
        cache.setTimeToLive(ONE_SECOND);
        Clock.setTime(0);
    }

    @Test
    public void getTimeToLive() throws Exception {
        assertNotEquals(100, cache.getTimeToLive());
        assertEquals(ONE_SECOND, cache.getTimeToLive());
    }

    @Test
    public void setTimeToLive() throws Exception {
        assertTrue(ONE_SECOND == cache.getTimeToLive());
        cache.setTimeToLive(ONE_SECOND * 3);
        assertFalse(ONE_SECOND == cache.getTimeToLive());
        assertEquals(3000, cache.getTimeToLive());
    }

    @Test
    public void put() throws Exception {
        assertEquals(DEFAULT_VALUE, cache.put(DEFAULT_KEY, DEFAULT_VALUE)); // PUT DEFAULT
        assertEquals(1, cache.size());
        assertEquals(DEFAULT_VALUE, cache.get(DEFAULT_KEY));

        putDefault(); // same code
        assertEquals(1, cache.size());// same size (rewriting data)
    }

    @Test
    public void clearExpired() throws Exception {
        putDefault();
        assertEquals(1, cache.size());
        assertEquals(DEFAULT_VALUE, cache.get(DEFAULT_KEY));
        Clock.setTime(2000);

        cache.clearExpired();
        assertNull(cache.get(DEFAULT_KEY));
        assertEquals(0, cache.size());
    }

    @Test
    public void clear() throws Exception {
        assertEquals(0, cache.size());
        putDefault();
        assertEquals(1, cache.size());
        assertEquals(DEFAULT_VALUE, cache.get(DEFAULT_KEY));

        cache.clear();
        assertEquals(0, cache.size());
        assertNotEquals(DEFAULT_VALUE, cache.get(DEFAULT_KEY));
    }

    @Test
    public void containsKey() throws Exception {
        assertFalse(cache.containsKey(DEFAULT_KEY));
        putDefault();
        assertTrue(cache.containsKey(DEFAULT_KEY));

        assertFalse(cache.containsKey(13));
        cache.put(13, "Yes");
        assertTrue(cache.containsKey(13));
        assertEquals(2, cache.size()); // default + (13, "Yes")

        Clock.setTime(1001); // all must die
        assertEquals(0, cache.size());

        cache.put(13, "Yes"); // time = 1001
        Clock.setTime(2000);
        assertTrue(cache.containsKey(13));// 1 mls to live
    }

    @Test
    public void containsValue() throws Exception {
        assertFalse(cache.containsValue(DEFAULT_VALUE));
        putDefault();           // first
        assertTrue(cache.containsValue(DEFAULT_VALUE));
        assertFalse(cache.containsValue("Yes"));

        assertFalse(cache.containsValue("Yes"));
        cache.put(13, "Yes");   // second
        assertTrue(cache.containsValue("Yes"));
        assertEquals(2, cache.size());

        Clock.setTime(3333); // all must die
        assertFalse(cache.containsValue("Yes"));
        assertEquals(0, cache.size());

        cache.put(13, "Yes"); // time = 3333
        Clock.setTime(4444);

        assertFalse(cache.containsValue("Yes"));
        assertEquals(false, cache.containsValue("Yes"));// 0 mls to live

    }

    @Test
    public void get() throws Exception {
        cache.setTimeToLive(3000);
        assertFalse(cache.size() > 0);
        putDefault();
        assertTrue(cache.size() > 0);

        Clock.setTime(4000); // all died>
        assertNull(cache.get(DEFAULT_KEY));
        assertEquals(0, cache.size());

        cache.put(13, "Yes");// saved time = 4000 | time to live = 3000

        Clock.setTime(6999); // 1 mls left
        assertNotNull(cache.get(13));
        assertEquals("Yes", cache.get(13));

        Clock.setTime(7000);
        assertNull(cache.get(DEFAULT_KEY));
        assertNull(cache.get(13));
        assertEquals(0, cache.size());
    }

    @Test
    public void isEmpty() throws Exception {
        assertEquals(0, cache.size());
        assertFalse(cache.size() > 0);
        assertTrue(cache.isEmpty());

        putDefault();
        assertNotEquals(0, cache.size());
        assertTrue(cache.size() > 0);
        assertFalse(cache.isEmpty());
        assertNotEquals(true, cache.isEmpty());

        cache.clear();
        assertEquals(0, cache.size());
        assertTrue(cache.size() == 0 && cache.isEmpty());
        assertEquals(true, cache.isEmpty());
    }

    @Test
    public void remove() throws Exception {
        assertTrue(cache.size() == 0 && cache.isEmpty());
        assertNull(cache.remove(DEFAULT_KEY));

        putDefault();

        assertNotNull(cache.remove(DEFAULT_KEY));
        assertNotEquals(DEFAULT_VALUE, cache.remove(DEFAULT_KEY));

        assertEquals(null, cache.remove(DEFAULT_KEY));
        assertTrue(cache.size() == 0 && cache.isEmpty());
    }

    @Test
    public void size() throws Exception {
        assertTrue(cache.size() == 0 && cache.isEmpty());

        putDefault();
        assertEquals(1, cache.size());

        IntStream.range(0, 9).forEach(i -> cache.put(i, "i=" + i)); // nine iterations
        assertEquals(10, cache.size());

        IntStream.range(0, 10).forEach(cache::remove);
        assertNotEquals(0, cache.size());
        assertEquals(1, cache.size()); // no such key '0'

        cache.clear();
        assertEquals(0, cache.size());
        assertTrue(cache.size() == 0 && cache.isEmpty());
    }

    private void putDefault() {
        cache.put(DEFAULT_KEY, DEFAULT_VALUE);
    }
}