package cachemap;

import com.sun.istack.internal.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Chervinko on 13.06.17. 15:30
 */

public class CacheMapImpl<K, V> implements CacheMap<K, V> {
    private long timeToLive = 1000; // default innerValue
    private Map<K, WrapNode<V>> coreMap = new HashMap<>();

    public void setTimeToLive(long timeToLive) {
        this.timeToLive = timeToLive;
    }

    public long getTimeToLive() {
        return timeToLive;
    }

    public V put(K key, V value) {
        coreMap.put(key, new WrapNode<>(Clock.getTime(), value));
        return coreMap.get(key).getInnerValue();
    }

    public void clearExpired() {
        coreMap = coreMap
                .entrySet()
                .stream()
                .filter(e -> isALive(e.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void clear() {
        coreMap.clear();
    }

    public boolean containsKey(Object key) {
        return coreMap.containsKey(key) && isAvailable(key);
    }

    public boolean containsValue(Object value) {
        K keyObj = coreMap
                .entrySet()
                .stream()
                .filter(e -> e.getValue().innerValue.equals(value))
                .map(Map.Entry::getKey)
                .findAny()
                .orElse(null);
        return keyObj != null && isAvailable(keyObj);
    }

    private boolean isAvailable(Object key) {
        if (isALive(key)) return true;

        coreMap.remove(key);
        return false;
    }

    private boolean isALive(Object key) {// for comfortable
        return coreMap.get(key).getStartTime() - Clock.getTime() < timeToLive;
    }

    @Nullable
    public V get(Object key) {
        return (coreMap.containsKey(key) && isAvailable(key)) ? coreMap.get(key).getInnerValue() : null;
    }

    public boolean isEmpty() {
        clearExpired();
        return coreMap.isEmpty();
    }


    @Nullable
    public V remove(Object key) {
        return coreMap.containsKey(key) ? coreMap.remove(key).getInnerValue() : null;
    }

    public int size() {
        clearExpired();
        return coreMap.size();
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