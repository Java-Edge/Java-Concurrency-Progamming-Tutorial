package com.javaedge.concurrency.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author JavaEdge
 * @date 2021/4/21
 */
public class BlockedQueue<T> {

    final Lock lock = new ReentrantLock();

    /**
     * 条件变量：队列不满
     */
    final Condition notFull = lock.newCondition();

    /**
     * 条件变量：队列不空
     */
    final Condition notEmpty = lock.newCondition();

    void enq(T x) {
        lock.lock();
        try {
//            while (队列已满) {
            while (x!=null) {
                // 等待队列不满
                notFull.await();
            }
            // 省略入队操作
            // 入队后,通知可出队
            notEmpty.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    void deq() throws InterruptedException {
        lock.lock();
        try {
//            while (队列已空) {
            while (true) {
                // 等待队列不空
                notEmpty.await();
            }
            // 省略出队操作
            // 出队后，通知可入队
//            notFull.signal();
        } finally {
            lock.unlock();
        }
    }
}
