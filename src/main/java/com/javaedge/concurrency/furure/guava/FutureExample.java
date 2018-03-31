package com.javaedge.concurrency.furure.guava;

import com.google.common.util.concurrent.*;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

/**
 * @author JavaEdge
 */
@Slf4j
public class FutureExample {

    static class MyCallable implements Callable<String> {

        @Override
        public String call() throws Exception {
            log.info("do something in callable");
            Thread.sleep(5000);
            return "Done";
        }
    }

    public static void main(String[] args) throws Exception {
        // 装饰器模式
        ListeningExecutorService service = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(2));

        ListenableFuture<String> future = service.submit(new MyCallable());

        System.out.println(future.hashCode());

        // 异步非阻塞：观察者模式  当ListenableFuture完成时，需要执行的程序
        Futures.addCallback(future, new FutureCallback<String>() {
            @Override
            public void onSuccess(@Nullable String s) {

            }

            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }
        }, service);

        log.info("do something in main");
        Thread.sleep(1000);
        String result = future.get();
        log.info("result：{}", result);
    }
}
