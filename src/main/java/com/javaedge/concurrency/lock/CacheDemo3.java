package com.javaedge.concurrency.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author JavaEdge
 * @date 2021/4/22
 */
public class CacheDemo3 {

    Object data;

    volatile boolean cacheValid;

    final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    final Lock readLock = readWriteLock.readLock();

    final Lock writeLock = readWriteLock.writeLock();

    void processCachedData() {
        // 获取读锁
        readLock.lock();
        if (!cacheValid) {
            // 释放读锁，因为不允许读锁的升级
            readLock.unlock();
            // 获取写锁
            writeLock.lock();
            try {
                // 再次检查状态
                if (!cacheValid) {
                    // 省略具体代码
                    data = null;
                    cacheValid = true;
                }
                /**
                 * 1.获取读锁的时候线程还是持有写锁的
                 * 释放写锁前，降级为读锁
                 * 这种锁的降级是支持的
                 */
                readLock.lock();
            } finally {
                // 释放写锁
                writeLock.unlock();
            }
        }
        // 此处仍持有读锁
        try {
            use(data);
        } finally {
            readLock.unlock();
        }
    }

    private void use(Object data) {
    }
}