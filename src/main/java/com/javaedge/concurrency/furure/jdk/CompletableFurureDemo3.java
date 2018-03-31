package com.javaedge.concurrency.furure.jdk;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author JavaEdge
 * @date 2022/5/28
 */
public class CompletableFurureDemo3 {

    @Test
    public void CompletableFutureAndExecutor() {
        Stream<Integer> integerStream = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        ExecutorService executor = Executors.newFixedThreadPool((int) integerStream.count());
        List<CompletableFuture<Integer>> futureList = integerStream
                .map(operand ->
                        CompletableFuture.supplyAsync(() -> {
                                    getThreadName();
                                    return 1;
                                }, executor
                        )
                ).collect(Collectors.toList());
        long count = futureList.stream().map(CompletableFuture::join).count();
        System.out.println(count);
    }

    /**
     * 使用CountDownLatch改造主线程阻塞直到任务都完成
     * @throws InterruptedException
     */
    @Test
    public void CompletableFutureAndExecutor2() throws InterruptedException {
        Stream<Integer> integerStream = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        CountDownLatch countDownLatch = new CountDownLatch(10);
        ExecutorService executor = Executors.newFixedThreadPool(10);
//        ExecutorService executor = Executors.newFixedThreadPool((int) integerStream.count()); FIXME bug写法
        List<CompletableFuture<Integer>> futureList = integerStream
                .map(operand ->
                        CompletableFuture.supplyAsync(() -> {
                                    getThreadName();
                                    countDownLatch.countDown();
                                    return 1;
                                }, executor
                        )
                ).collect(Collectors.toList());
        countDownLatch.await(10, TimeUnit.MINUTES);
        long count = futureList.stream().map(CompletableFuture::join).count();
        System.out.println(count);
    }

    private void getThreadName() {
        // sleep 1秒
        LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(1));
        String time = LocalDateTime.now().toLocalTime().toString();
        String name = Thread.currentThread().getName();
        System.out.println(time + " " + name);
    }
}
