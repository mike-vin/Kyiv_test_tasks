package cachemap;

import com.sun.istack.internal.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.System.out;

/**
 * Created by Chervinko on 13.06.17. 15:30
 */

public class CacheMapImpl<K, V> implements CacheMap<K, V> {
    private long timeToLive = 1000; // default value
    private Map<K, WrapNode<V>> MAIN_MAP = new HashMap<>();

    public void setTimeToLive(long timeToLive) {
        this.timeToLive = Math.abs(timeToLive);
    }

    public long getTimeToLive() {
        return timeToLive;
    }

    public void clearExpired() {
        MAIN_MAP = MAIN_MAP
                .entrySet()
                .stream()
                .filter(e -> isALive(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public V put(K key, V value) {
        MAIN_MAP.put(key, new WrapNode<>(Clock.getTime(), value));
        return MAIN_MAP.get(key).getInnerValue();
    }

    @Nullable //safety increase
    public V get(Object key) {
        clearExpired();
        return MAIN_MAP.containsKey(key) ? MAIN_MAP.get(key).getInnerValue() : null;
    }

    @Nullable
    public V remove(Object key) {
        return MAIN_MAP.containsKey(key) ? MAIN_MAP.remove(key).getInnerValue() : null;
    }

    public void clear() {
        MAIN_MAP.clear();
    }

    public boolean containsKey(Object key) {
        clearExpired();
        return MAIN_MAP.containsKey(key);
    }

    public boolean containsValue(Object value) {
        clearExpired();
        K keyObj = MAIN_MAP
                .entrySet()
                .stream()
                .filter(e -> e.getValue().getInnerValue().equals(value))
                .map(Map.Entry::getKey)
                .findAny()
                .orElse(null);
        return keyObj != null;
    }

    public boolean isEmpty() {
        clearExpired();
        return MAIN_MAP.isEmpty();
    }

    public int size() {
        clearExpired();
        return MAIN_MAP.size();
    }

    private boolean isALive(Object key) {// for comfortable
        return Clock.getTime() - MAIN_MAP.get(key).getStartTime() < timeToLive;
    }

    //**********************************************************************************************************************
    private class WrapNode<T> {
        private T innerValue;
        private long startTime;

        WrapNode(long time, T innerValue) {
            this.startTime = time;
            this.innerValue = innerValue;
        }

        T getInnerValue() {
            return this.innerValue;
        }

        long getStartTime() {
            return this.startTime;
        }
    }
}