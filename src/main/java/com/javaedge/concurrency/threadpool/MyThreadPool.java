package com.javaedge.concurrency.threadpool;

import com.google.common.collect.Lists;
import lombok.SneakyThrows;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 简化线程池
 *
 * @author JavaEdge
 * @date 2021/5/16
 */
public class MyThreadPool {

    /**
     * 利用阻塞队列实现生产者-消费者模式
     */
    BlockingQueue<Runnable> workQueue;

    /**
     * 保存内部工作线程
     */
    List<WorkerThread> threads = Lists.newArrayList();

    MyThreadPool(int poolSize, BlockingQueue<Runnable> workQueue) {
        this.workQueue = workQueue;
        // 创建工作线程
        for (int idx = 0; idx < poolSize; idx++) {
            WorkerThread work = new WorkerThread();
            work.start();
            threads.add(work);
        }
    }

    /**
     * 提交任务
     */
    void execute(Runnable command) throws InterruptedException {
        // 将任务加入到workQueue
        workQueue.put(command);
    }

    /**
     * 工作线程：负责消费任务，并执行任务
     */
    class WorkerThread extends Thread {

        @SneakyThrows
        @Override
        public void run() {
            // 消费workQueue中的任务并执行
            while (true) {
                Runnable task = workQueue.take();
                task.run();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 创建有界阻塞队列
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(2);
        // 创建线程池
        MyThreadPool pool = new MyThreadPool(10, workQueue);
        // 提交任务
        pool.execute(() -> System.out.println("hello"));
    }
}