package io.collective;

import java.time.Clock;

public class SimpleAgedCache {

    int size = 0;
    ExpirableEntry[] elements = new ExpirableEntry[10];
    Clock clock;

    public SimpleAgedCache(Clock clock) {
        this.clock = clock;
    }

    public SimpleAgedCache() {
    }

    public void put(String key, String value, long retentionInMillis) {
        ExpirableEntry newEntry = new ExpirableEntry();
        newEntry.setKey(key);
        newEntry.setValue(value);
        newEntry.setRetention(retentionInMillis);
        if (clock != null) {
            newEntry.setBaseTime(clock.millis()); // in case of expirable cache
        }
        elements[size] = newEntry;

        size += 1;
    }

    public boolean isEmpty() {
        if (size() == 0) {
            return true;
        }
        return false;
    }

    public int size() {
        if (clock == null) {
            return size;
        }
        long currTime = clock.instant().toEpochMilli();
        int count = 0; // keeps count of expired entries
        for (int index=0; index<size; index++) {
            long baseTime = elements[index].getBaseTime();
            long retentionTime = elements[index].getRetention();
            if ( baseTime + retentionTime < currTime ) { // entry is expired
                elements[index] = null;
                count++;
            }
        }
        return size-count;
    }

    public Object get(Object key) {
        for (int index=0; index<size; index++) {
            if (elements[index] != null && elements[index].getKey() == key) {
                return elements[index].getValue();
            }
        }
        return null;
    }

    static class ExpirableEntry {

        String key;
        String value;
        long retention;
        long baseTime;

        public long getBaseTime() {
            return baseTime;
        }

        public void setBaseTime(long baseTime) {
            this.baseTime = baseTime;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public long getRetention() {
            return retention;
        }

        public void setRetention(long retention) {
            this.retention = retention;
        }
    }
}