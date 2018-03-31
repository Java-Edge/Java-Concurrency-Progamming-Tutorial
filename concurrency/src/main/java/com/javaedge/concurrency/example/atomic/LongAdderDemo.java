package com.javaedge.concurrency.example.atomic;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * 测试用例： 同时运行2秒，检查谁的次数最多
 *
 * @author JavaEdge
 * @date 2019/10/18
 */
public class LongAdderDemo {
    private long count = 0;

    /**
     * 同步代码块的方式
     * @throws InterruptedException
     */
    public void testSync() throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                long starttime = System.currentTimeMillis();
                while (System.currentTimeMillis() - starttime < 2000) { // 运行两秒
                    synchronized (this) {
                        ++count;
                    }
                }
                long endtime = System.currentTimeMillis();
                System.out.println("SyncThread spend:" + (endtime - starttime) + "ms" + " v" + count);
            }).start();
        }
    }

    /**
     * Atomic方式
     */
    private AtomicLong acount = new AtomicLong(0L);

    public void testAtomic() throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                long starttime = System.currentTimeMillis();
                while (System.currentTimeMillis() - starttime < 2000) { // 运行两秒
                    acount.incrementAndGet(); // acount++;
                }
                long endtime = System.currentTimeMillis();
                System.out.println("AtomicThread spend:" + (endtime - starttime) + "ms" + " v-" + acount.incrementAndGet());
            }).start();
        }
    }

    // LongAdder 方式
    private LongAdder lacount = new LongAdder();
    public void testLongAdder() throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                long starttime = System.currentTimeMillis();
                while (System.currentTimeMillis() - starttime < 2000) { // 运行两秒
                    lacount.increment();
                }
                long endtime = System.currentTimeMillis();
                System.out.println("LongAdderThread spend:" + (endtime - starttime) + "ms" + " v-" + lacount.sum());
            }).start();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        LongAdderDemo demo = new LongAdderDemo();
        demo.testSync();
        demo.testAtomic();
        demo.testLongAdder();
    }
}