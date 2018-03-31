package com.javaedge.concurrency.aqs;

/**
 * @author JavaEdge
 * @date 2021/4/21
 */
class SampleLock {
    volatile int state;

    /**
     * 加锁
     */
    void lock() {
        // ...
        state = 1;
    }

    /**
     * 解锁
     */
    void unlock() {
        // ...
        state = 0;
    }
}
