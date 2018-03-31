package com.javaedge.concurrency.furure.jdk;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * @author JavaEdge
 */
@Slf4j
public class FutureTaskExample {

    public static void main(String[] args) throws Exception {
        // 创建异步任务
        FutureTask<String> futureTask = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                log.info("do something in callable");
                Thread.sleep(5000);
                return "Done";
            }
        });
        // 启动线程
        new Thread(futureTask).start();
        log.info("do something in main");
        Thread.sleep(1000);
        // 【阻塞】等待任务执行完毕，并返回结果
        String result = futureTask.get();
        log.info("result：{}", result);
    }
}
