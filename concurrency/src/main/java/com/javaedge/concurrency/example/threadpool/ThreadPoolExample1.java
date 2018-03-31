package com.javaedge.concurrency.example.threadpool;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author JavaEdge
 * @date 2019/10/9
 */
@Slf4j
public class ThreadPoolExample1 {

    public static void main(String[] args) {

        ExecutorService executorService = Executors.newCachedThreadPool();

        for (int i = 0; i < 10; i++) {
            final int index = i;
            executorService.execute(() -> log.info("task:{}", index));
        }
        executorService.shutdown();
    }
}
