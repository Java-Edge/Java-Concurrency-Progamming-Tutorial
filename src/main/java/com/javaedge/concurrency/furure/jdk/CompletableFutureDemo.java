package com.javaedge.concurrency.furure.jdk;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * @author JavaEdge
 * @date 2021/4/15
 */
@Slf4j
public class CompletableFutureDemo {

    @AllArgsConstructor
    private static class MyTask implements Callable<Boolean> {

        private String name;
        private int timeInSeconds;
        private boolean ret;

        @Override
        public Boolean call() {
            // 模拟业务执行时间
            // 实际中时间不固定，可能在处理计算任务，
            try {
                Thread.sleep(timeInSeconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(name + " task callback");
            return ret;
        }
    }

    public static void main(String[] args) throws Exception {
        MyTask task1 = new MyTask("task1", 3, true);
        MyTask task2 = new MyTask("task2", 4, true);
        MyTask task3 = new MyTask("task3", 1, false);

        CompletableFuture f1 = CompletableFuture.supplyAsync(task1::call).thenAccept((result) -> callback(result));

        CompletableFuture f2 = CompletableFuture.supplyAsync(task2::call).thenAccept((result) -> callback(result));

        CompletableFuture f3 = CompletableFuture.supplyAsync(task3::call).thenAccept((result) -> callback(result));

        System.in.read();
    }

    private static void callback(Boolean result) {
        if (!result) {
            // 处理结束流程 通知其他线程结束（回滚） 超时处理
            System.exit(0);
        }
    }
}
