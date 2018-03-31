package com.javaedge.concurrency.aqs.semaphore;

import java.util.concurrent.Semaphore;

/**
 * @author JavaEdge
 */
public class SemaphoreExample6 {

    static int count;

    /**
     * 初始化信号量
     */
    static final Semaphore SEMAPHORE = new Semaphore(1);

    /**
     * 信号量保证互斥
     */
    static void add() throws InterruptedException {
        // 进入临界区之前执行
        SEMAPHORE.acquire();
        try {
            count += 1;
        } finally {
            // 退出临界区之前执行
            SEMAPHORE.release();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        add();
    }
}
