package io.collective;

import java.time.Clock;

public class SimpleAgedCache {

    int size = 0;
    Object[] keyElements = new Object[10];
    Object[] valueElements = new Object[10];
    Object[] retentionElements = new Object[10];

    public SimpleAgedCache(Clock clock) {
    }

    public SimpleAgedCache() {
    }

    public void put(Object key, Object value, int retentionInMillis) {
        keyElements[size] = key;
        valueElements[size] = value;
        retentionElements[size] = retentionInMillis;
        size += 1;
    }

    public boolean isEmpty() {
        if (size() == 0) {
            return true;
        }
        return false;
    }

    public int size() {
        return size;
    }

    public Object get(Object key) {
        for (int index=0; index<size; index++) {
            if (keyElements[index] == key) {
                return valueElements[index];
            }
        }
        return null;
    }
}