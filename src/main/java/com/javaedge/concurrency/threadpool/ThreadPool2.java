package com.javaedge.concurrency.threadpool;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author JavaEdge
 */
public class ThreadPool2 {

    public static void main(String[] args) throws InterruptedException {
        // P1、P2阶段共用的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        // P1阶段的闭锁
        CountDownLatch p1 = new CountDownLatch(2);
        for (int i = 0; i < 2; i++) {
            System.out.println("P1");
            //执行P1阶段任务
            executorService.execute(() -> {
                // P2阶段的闭锁
                CountDownLatch p2 = new CountDownLatch(2);
                // 执行P2阶段子任务
                for (int j = 0; j < 2; j++) {
                    executorService.execute(() -> {
                        System.out.println("P2");
                        p2.countDown();
                    });
                }

                try {

                    /**
                     * 等待P2阶段任务执行完
                     * 线程池里所有的线程都在等待P2阶段任务执行完
                     * P2阶段任务何时能执行完？
                     * 永远都无法执行完！因为线程池里的线程都被阻塞了，没有空闲线程执行P2阶段任务。
                     */
                    p2.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                p1.countDown();
            });
        }
        // 等着P1阶段任务执行完
        p1.await();
        System.out.println("end");
    }
}