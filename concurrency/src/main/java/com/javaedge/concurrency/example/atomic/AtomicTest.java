package com.javaedge.concurrency.example.atomic;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * atomic 相关测试代码
 *
 * @author JavaEdge
 * @date 2019/10/20
 */
public class AtomicTest {

    public static void main(String[] args) throws InterruptedException {
        // 自增
        AtomicInteger atomicInteger = new AtomicInteger(0);
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    atomicInteger.incrementAndGet();
                }
            }).start();
        }
        Thread.sleep(2000L);
        System.out.println(atomicInteger.get());
    }
}
