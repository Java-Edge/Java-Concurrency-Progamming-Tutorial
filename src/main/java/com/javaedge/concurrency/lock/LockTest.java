package com.javaedge.concurrency.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author JavaEdge
 * @date 2021/4/21
 */
public class LockTest {
    private final Lock reentrantLock = new ReentrantLock();
    int value;

    public void add() {
        // 获取锁
        reentrantLock.lock();
        try {
            // 若线程 t1 执行 value+=1,那后续线程t2能看到value正确值吗？
            value += 1;
        } finally {
            // 保证锁能释放
            reentrantLock.unlock();
        }
    }
}
