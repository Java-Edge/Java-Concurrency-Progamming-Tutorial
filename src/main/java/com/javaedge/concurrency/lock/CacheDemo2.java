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
public class CacheDemo2<K, V> {

    public static void main(String[] args) {
    }

    final Map<K, V> m = new HashMap<>();
    final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    final Lock readLock = readWriteLock.readLock();
    final Lock writeLock = readWriteLock.writeLock();

    V get(K key) {
        V v = null;
        // 1.加读锁
        readLock.lock();
        try {
            // 2.读缓存
            v = m.get(key);
            if (v == null) {
                writeLock.lock();
                try {
                    // 再次验证并更新缓存
                } finally {
                    writeLock.unlock();
                }
            }
        } finally {
            // 3.释放锁
            readLock.unlock();
        }
        // 4.缓存中存在，返回
        if (v != null) {
            return v;
        }

        /**
         * 5.若缓存中没有缓存目标对象,就需要从 DB 加载,然后写入缓存.
         * 写缓存需要用到写锁
         */
        writeLock.lock();
        try {

            /**
             * 6.
             * 获取写锁后,并非直接去查询DB.
             * 先再验证一次缓存中是否存在,因为高并发场景下,其他线程可能已查询过 DB
             */
            v = m.get(key);
            // 7
            if (v == null) {
                // 再次验证如果还是不存在，才去查询DB
                v = null;
                // 并更新本地缓存
                m.put(key, v);
            }
        } finally {
            writeLock.unlock();
        }
        return v;
    }
}
