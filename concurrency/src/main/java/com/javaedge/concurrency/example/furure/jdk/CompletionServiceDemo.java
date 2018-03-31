package com.javaedge.concurrency.example.furure.jdk;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author JavaEdge
 * @date 2021/4/18
 */
public class CompletionServiceDemo {

//    public static void main(String[] args) throws InterruptedException, ExecutionException {
//        test1();
//
//        test2();
//
//        test3();
//    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        test0();
    }

    private static void test0() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        Future<Integer> f1 = executor.submit(() -> getPriceByS1());
        Future<Integer> f2 = executor.submit(() -> getPriceByS2());
        Future<Integer> f3 = executor.submit(() -> getPriceByS3());

        executor.execute(() -> {
            try {
                save(f1.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
        executor.execute(() -> {
            try {
                save(f2.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
        executor.execute(() -> {
            try {
                save(f3.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        });
        f3.get();
    }

    private static AtomicReference<Integer> test3() {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        // 创建CompletionService
        CompletionService<Integer> service = new ExecutorCompletionService<>(executor);
        // 异步查询价格
        service.submit(CompletionServiceDemo::getPriceByS1);
        service.submit(CompletionServiceDemo::getPriceByS2);
        service.submit(CompletionServiceDemo::getPriceByS3);
        // 将询价结果异步保存到数据库
        // 并计算最低报价
        AtomicReference<Integer> m = new AtomicReference<>(Integer.MAX_VALUE);
        for (int i = 0; i < 3; i++) {
            executor.execute(() -> {
                Integer r = null;
                try {
                    r = service.take().get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                save(r);
                m.set(Integer.min(m.get(), r));
            });
        }
        return m;
    }

    private static void test2() throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(3);
        CompletionService<Integer> cs = new ExecutorCompletionService<>(executor);
        // 异步询价
        cs.submit(CompletionServiceDemo::getPriceByS1);
        cs.submit(CompletionServiceDemo::getPriceByS2);
        cs.submit(CompletionServiceDemo::getPriceByS3);

        // 将结果异步保存到MySQL
        for (int i = 0; i < 3; i++) {
            Integer r = cs.take().get();
            executor.execute(() -> save(r));
        }
    }

    private static void test1() throws ExecutionException, InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(3);

        // 异步询价
        Future<Integer> f1 = executor.submit(CompletionServiceDemo::getPriceByS1);

        Future<Integer> f2 = executor.submit(CompletionServiceDemo::getPriceByS2);

        Future<Integer> f3 = executor.submit(CompletionServiceDemo::getPriceByS3);

        Integer r;
        // 获取各商家报价并保存
        r = f1.get();
        Integer finalR = r;
        executor.execute(() -> save(finalR));

        r = f2.get();
        Integer finalR1 = r;
        executor.execute(() -> save(finalR1));

        r = f3.get();
        Integer finalR2 = r;
        executor.execute(() -> save(finalR2));
    }

    private static void save(Integer r) {

    }

    private static Integer getPriceByS3() {
        return 3;
    }

    private static Integer getPriceByS2() {
        return 2;
    }

    private static Integer getPriceByS1() {
        return 1;
    }
}
