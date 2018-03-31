package com.javaedge.concurrency.forkjoin;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * @author JavaEdge
 */
@Slf4j
public class ForkJoinTaskExample3 {

    public static void main(String[] args) {
        String[] fc = {"hello world",
                "hello me",
                "hello fork",
                "hello join",
                "fork join in world"};
        //创建ForkJoin线程池
        ForkJoinPool fjp = new ForkJoinPool(3);
        //创建任务
        MR mr = new MR(fc, 0, fc.length);
        //启动任务
        Map<String, Long> result = fjp.invoke(mr);
        //输出结果
        result.forEach((k, v) -> System.out.println(k + ":" + v));
    }

    /**
     * MR模拟类
     */
    static class MR extends RecursiveTask<Map<String, Long>> {
        private String[] fc;
        private int start, end;

        /**
         * 构造函数
         *
         * @param fc
         * @param fr
         * @param to
         */
        MR(String[] fc, int fr, int to) {
            this.fc = fc;
            this.start = fr;
            this.end = to;
        }

        @Override
        protected Map<String, Long> compute() {
            if (end - start == 1) {
                return calc(fc[start]);
            } else {
                int mid = (start + end) / 2;
                MR mr1 = new MR(fc, start, mid);
                // 前半部分数据fork一个递归任务去处理
                mr1.fork();
                MR mr2 = new MR(fc, mid, end);
                //计算子任务，并返回合并的结果  后半部分数据则在当前任务中递归处理:mr2.compute()
                // 如果join在前面会先首先让当前线程阻塞在join()上。当join()执行完才会执行mr2.compute(),这样并行度会降低
                /**
                 * 为什么不能merge mr1.compute和mr2..compute或者mr1.join和mr2的join呢？
                 * compute+compute相当于没用forkjoin，都在一个线程里跑的。
                 * 如果用join+join也可以，不过jdk官方有个建议，顺序要用：a.fork(); b.fork(); b.join(); a.join();否则性能有问题。
                 * 所以还是用fork+compute更简单
                 */
                return merge(mr2.compute(), mr1.join());
            }
        }

        /**
         * 合并结果
         *
         * @param r1
         * @param r2
         * @return
         */
        private Map<String, Long> merge(
                Map<String, Long> r1,
                Map<String, Long> r2) {
            Map<String, Long> result = new HashMap<>();
            result.putAll(r1);
            //合并结果
            r2.forEach((k, v) -> {
                Long c = result.get(k);
                if (c != null) {
                    result.put(k, c + v);
                } else {
                    result.put(k, v);
                }
            });
            return result;
        }

        //统计单词数量
        private Map<String, Long>
        calc(String line) {
            Map<String, Long> result =
                    new HashMap<>();
            //分割单词
            String[] words =
                    line.split("\\s+");
            //统计单词数量
            for (String w : words) {
                Long v = result.get(w);
                if (v != null) {
                    result.put(w, v + 1);
                } else {
                    result.put(w, 1L);
                }
            }
            return result;
        }
    }

    /**
     * Ryzen 1700 8核16线程 3.0 GHz
     */
    @Test
    public void mergeSort() {
        long[] arrs = new long[100000000];
        for (int i = 0; i < 100000000; i++) {
            arrs[i] = (long) (Math.random() * 100000000);
        }
        long startTime = System.currentTimeMillis();
        ForkJoinPool forkJoinPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        MergeSort mergeSort = new MergeSort(arrs);
//        arrs = forkJoinPool.invoke(mergeSort);
        //传统递归
        arrs = mergeSort(arrs);
        long endTime = System.currentTimeMillis();
        System.out.println("耗时：" + (endTime - startTime));
    }

    /**
     * 传统递归
     * 耗时：
     */
    public static long[] mergeSort(long[] arrs) {
        if (arrs.length < 2) {
            return arrs;
        }
        int mid = arrs.length / 2;
        long[] left = Arrays.copyOfRange(arrs, 0, mid);
        long[] right = Arrays.copyOfRange(arrs, mid, arrs.length);
        return merge(mergeSort(left), mergeSort(right));
    }

    public static long[] merge(long[] left, long[] right) {
        long[] result = new long[left.length + right.length];
        for (int i = 0, m = 0, j = 0; m < result.length; m++) {
            if (i >= left.length) {
                result[m] = right[j++];
            } else if (j >= right.length) {
                result[m] = left[i++];
            } else if (left[i] > right[j]) {
                result[m] = right[j++];
            } else {
                result[m] = left[i++];
            }
        }
        return result;
    }

    /**
     * fork/join
     * 耗时：14853 ms
     */
    class MergeSort extends RecursiveTask<long[]> {
        long[] arrs;

        public MergeSort(long[] arrs) {
            this.arrs = arrs;
        }

        @Override
        protected long[] compute() {
            if (arrs.length < 2) {
                return arrs;
            }
            int mid = arrs.length / 2;
            MergeSort left = new MergeSort(Arrays.copyOfRange(arrs, 0, mid));
            left.fork();
            MergeSort right = new MergeSort(Arrays.copyOfRange(arrs, mid, arrs.length));
            return merge(right.compute(), left.join());
        }
    }
}
