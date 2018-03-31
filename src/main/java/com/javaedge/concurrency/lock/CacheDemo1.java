package com.javaedge.concurrency.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author JavaEdge
 * @date 2021/4/22
 */
public class CacheDemo1<K, V> {

    final Map<String, Data> m = new HashMap<String, Data>();

    final ReadWriteLock rwl = new ReentrantReadWriteLock();

    final Lock r = rwl.readLock();

    final Lock w = rwl.writeLock();

    Data get(String key) {
        r.lock();
        try {
            return m.get(key);
        } finally {
            r.unlock();
        }
    }

    Data put(String key, Data v) {
        w.lock();
        try {
            return m.put(key, v);
        } finally {
            w.unlock();
        }
    }

    class Data<K,V> { }

    public static void main(String[] args) {

    }
}
