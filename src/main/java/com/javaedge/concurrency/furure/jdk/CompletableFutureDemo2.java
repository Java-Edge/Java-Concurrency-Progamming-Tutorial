package com.javaedge.concurrency.furure.jdk;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

/**
 * @author JavaEdge
 * @date 2021/4/15
 */
@Slf4j
public class CompletableFutureDemo2 {

    public static void main(String[] args) throws Exception {
        CompletableFuture<String> future1 = CompletableFuture
                // 1
                .supplyAsync(() -> "Hello World")
                // 2
                .thenApply(s -> s + " Java")
                // 3
                .thenApply(String::toUpperCase);
        System.out.println(future1.join());

//        CompletableFuture<Integer> future2 = CompletableFuture
//                .supplyAsync(() -> (1 / 0))
//                .thenApply(r -> r * 2);
//        System.out.println(future2.join());

        CompletableFuture<Integer> future3 = CompletableFuture
                .supplyAsync(() -> 1 / 0)
                .thenApply(r -> r * 2)
                .exceptionally(e -> 0);
        System.out.println(future3.join());

        Random rand = new Random(47);
        CompletableFuture<String> f1 =
                CompletableFuture.supplyAsync(() -> {
                    int t = rand.nextInt(20);
                    try {
                        Thread.sleep(t);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return String.valueOf(t);
                });

        CompletableFuture<String> f2 =
                CompletableFuture.supplyAsync(() -> {
                    int t = rand.nextInt();
                    try {
                        Thread.sleep(t);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return String.valueOf(t);
                });

        CompletableFuture<String> f3 =
                f1.applyToEither(f2, s -> s);

        System.out.println(f3.join());

    }
}
