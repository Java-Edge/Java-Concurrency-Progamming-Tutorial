package com.javaedge.concurrency.forkjoin;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * @author JavaEdge
 */
@Slf4j
public class ForkJoinTaskExample2 {
    public static void main(String[] args) {
        // 创建分治任务线程池
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);

        // 创建分治任务
        Fibonacci fib = new Fibonacci(30);

        // 启动分治任务
        Integer result = forkJoinPool.invoke(fib);

        System.out.println(result);
    }

    /**
     * 数列的递归任务 需要有返回值
     */
    static class Fibonacci extends RecursiveTask<Integer> {
        final int n;

        Fibonacci(int n) {
            this.n = n;
        }

        @Override
        protected Integer compute() {
            if (n <= 1) {
                return n;
            }

            Fibonacci f1 = new Fibonacci(n - 1);

            // 创建子任务
            f1.fork();
            Fibonacci f2 = new Fibonacci(n - 2);

            // 等待子任务结果，并合并结果
            return f2.compute() + f1.join();
        }
    }
}
