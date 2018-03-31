package com.javaedge.concurrency.example.atomic;

import java.util.concurrent.atomic.LongAccumulator;
import java.util.function.LongBinaryOperator;

/**
 * LongAdder增强版，处理累加之外，可以自行定义其他计算
 *
 * @author JavaEdge
 * @date 2019/10/18
 */
public class LongAccumulatorDemo {
    public static void main(String[] args) throws InterruptedException {
        LongAccumulator accumulator = new LongAccumulator(new LongBinaryOperator() {
            @Override
            public long applyAsLong(long left, long right) {
                // 返回最大值，这就是自定义的计算
                return left < right ? left : right;
            }
        }, 0);

        // 1000个线程
        for (int i = 0; i < 1000; i++) {
            int finalI = i;
            new Thread(() -> {
                accumulator.accumulate(finalI); // 此处实际就是执行上面定义的操作
            }).start();
        }

        Thread.sleep(2000L);
        System.out.println(accumulator.longValue()); // 打印出结果
    }

}
