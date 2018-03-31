package com.javaedge.concurrency.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author JavaEdge
 * @date 2021/4/21
 */
public class LockTest2 {
    private final Lock reentrantLock = new ReentrantLock();
    int value;

    public int get() {
        // 2.获取锁
        //  此时，若锁可重入，则t1可再次加锁
        //      若不可重入，则t1此时会被阻塞
        reentrantLock.lock();
        try {
            return value;
        } finally {
            // 保证锁能释放
            reentrantLock.unlock();
        }
    }

    public void add() {
        // 获取锁
        reentrantLock.lock();
        try {
            // 1.线程 t1 此时已经获取到锁
            value = 1 + get();
        } finally {
            // 确保释放锁
            reentrantLock.unlock();
        }
    }
}
