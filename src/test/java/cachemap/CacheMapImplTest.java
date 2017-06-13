package cachemap;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by mike on 13.06.17.
 */
public class CacheMapImplTest {
    private CacheMap<Integer, String> cache = new CacheMapImpl<>();
    private final static long TIME_TO_LIVE = 1000;
    private final Integer DEFAULT_KEY = 777;
    private final String DEFAULT_VALUE = "TEST_STRING";

    @Before
    public void preStart() throws Exception {
        cache.clear();
        cache.setTimeToLive(TIME_TO_LIVE);
        Clock.setTime(0);
    }

    @Test
    public void getTimeToLive() throws Exception {
        assertNotEquals(100, cache.getTimeToLive());
        assertEquals(TIME_TO_LIVE, cache.getTimeToLive());
    }

    @Test
    public void setTimeToLive() throws Exception {
        assertTrue(TIME_TO_LIVE == cache.getTimeToLive());
    }

    @Test
    public void put() throws Exception {
        assertEquals(DEFAULT_VALUE, cache.put(DEFAULT_KEY, DEFAULT_VALUE)); // PUT DEFAULT
        assertEquals(DEFAULT_VALUE, cache.get(DEFAULT_KEY));
    }

    @Test
    public void clearExpired() throws Exception {
        putDefault();
        assertTrue(cache.size() == 1);
        assertEquals(DEFAULT_VALUE, cache.get(DEFAULT_KEY));
        Clock.setTime(2000);

        cache.clearExpired();
        assertNull(cache.get(13));
        assertTrue(cache.size() == 0);

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
    }


    @Test
    public void containsValue() throws Exception {
        assertFalse(cache.containsValue(DEFAULT_VALUE));
        putDefault();
        assertTrue(cache.containsValue(DEFAULT_VALUE));

        assertFalse(cache.containsValue("Yes"));
        cache.put(13, "Yes");
        assertTrue(cache.containsValue("Yes"));
    }

    @Test
    public void get() throws Exception {
        assertFalse(cache.size() > 0);
        putDefault();
        assertTrue(cache.size() > 0);

        Clock.setTime(4000);
        assertTrue(cache.containsValue(DEFAULT_VALUE));

    }

    @Test
    public void isEmpty() throws Exception {
    }

    @Test
    public void remove() throws Exception {
    }

    @Test
    public void size() throws Exception {
    }

    private void putDefault() {
        cache.put(DEFAULT_KEY, DEFAULT_VALUE);
    }
}