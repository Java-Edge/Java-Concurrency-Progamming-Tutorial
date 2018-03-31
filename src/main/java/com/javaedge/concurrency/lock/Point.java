package com.javaedge.concurrency.lock;

import java.util.concurrent.locks.StampedLock;

/**
 * @author JavaEdge
 * @date 2021/4/23
 */
class Point {
    private int x, y;
    final StampedLock stampedLock = new StampedLock();

    /**
     * 计算到原点的距离
     */
    public int distanceFromOrigin() {

        // 1.乐观读,是无锁的
        long stamp = stampedLock.tryOptimisticRead();

        // 2.由于乐观读是无锁的，所以共享变量x、y读入局部变量时，x、y可能被其他线程修改
        int curX = x, curY = y;

        /**
         * 3.所以读完后，还要再验证是否存在写操作
         * 判断执行读操作期间，是否存在写操作
         * 若存在,则stampedLock.validate会返回false
         */
        if (!stampedLock.validate(stamp)) {
            // 升级为悲观读锁
            stamp = stampedLock.readLock();
            try {
                curX = x;
                curY = y;
            } finally {
                // 释放悲观读锁
                stampedLock.unlockRead(stamp);
            }
        }
        return (int) Math.sqrt(curX * curX + curY * curY);
    }
}
