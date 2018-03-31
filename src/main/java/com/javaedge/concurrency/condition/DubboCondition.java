package com.javaedge.concurrency.condition;

import redis.clients.jedis.Response;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author JavaEdge
 * @date 2021/4/21
 */
public class DubboCondition {

    Response response;
    /**
     * 创建锁
     */
    private final Lock lock = new ReentrantLock();

    /**
     * 创建条件变量
     */
    private final Condition done = lock.newCondition();

    /**
     * 调用方通过该方法等待结果
     *
     * @param timeout
     * @return
     */
    Object get(int timeout) throws TimeoutException {
        long start = System.nanoTime();
        // 获取锁
        lock.lock();
        try {
            while (!isDone()) {
                // 获取锁后，通过经典的在循环中调用await()方法来实现等待。
                done.await(timeout, TimeUnit.SECONDS);
                long cur = System.nanoTime();
                if (isDone() ||
                        cur - start > timeout) {
                    break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 释放锁
            lock.unlock();
        }
        if (!isDone()) {
            throw new TimeoutException();
        }
//        return returnFromResponse();
        return null;
    }

    /**
     * RPC结果是否已经返回
     *
     * @return
     */
    boolean isDone() {
        return response != null;
    }

    // RPC结果返回时调用该方法
    private void doReceived(Response res) {
        // 获取锁
        lock.lock();
        try {
            response = res;
            if (done != null) {
                // 通知调用线程，结果已经返回，不用继续等待
                done.signal();
            }
        } finally {
            // 释放锁
            lock.unlock();
        }
    }
}
