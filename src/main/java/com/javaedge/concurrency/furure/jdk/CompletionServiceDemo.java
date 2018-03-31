package com.javaedge.concurrency.furure.jdk;

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
        Future<Integer> f1 = executor.submit(() -> getPrice1());
        Future<Integer> f2 = executor.submit(() -> getPrice2());
        Future<Integer> f3 = executor.submit(() -> getPrice3());

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
        CompletionService<Integer> completionService = new ExecutorCompletionService<>(executor);

        // 异步查询价格
        completionService.submit(CompletionServiceDemo::getPrice1);
        completionService.submit(CompletionServiceDemo::getPrice2);
        completionService.submit(CompletionServiceDemo::getPrice3);
        // 将查询结果异步保存
        // 并计算最低价格
        AtomicReference<Integer> m = new AtomicReference<>(Integer.MAX_VALUE);
        for (int i = 0; i < 3; i++) {
            executor.execute(() -> {
                Integer r = null;
                try {
                    r = completionService.take().get();
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

        // 异步查询
        cs.submit(CompletionServiceDemo::getPrice1);
        cs.submit(CompletionServiceDemo::getPrice2);
        cs.submit(CompletionServiceDemo::getPrice3);

        // 将结果异步保存
        for (int i = 0; i < 3; i++) {
            Integer r = cs.take().get();
            executor.execute(() -> save(r));
        }
    }

    private static void test1() throws ExecutionException, InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(3);

        // 异步查询
        Future<Integer> f1 = executor.submit(CompletionServiceDemo::getPrice1);

        Future<Integer> f2 = executor.submit(CompletionServiceDemo::getPrice2);

        Future<Integer> f3 = executor.submit(CompletionServiceDemo::getPrice3);

        Integer r;
        // 获取各个价格并保存
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
        System.out.println("r" + r);
    }

    private static Integer getPrice3() {
        return 3;
    }

    private static Integer getPrice2() {
        return 2;
    }

    private static Integer getPrice1() {
        return 1;
    }
}
