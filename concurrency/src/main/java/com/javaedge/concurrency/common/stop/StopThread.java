package com.javaedge.concurrency.common.stop;

/**
 * 线程stop强制性中止，破坏线程安全的示例
 *
 * @author JavaEdge
 * @date 2019/8/26
 */
public class StopThread extends Thread {
    private int i = 0, j = 0;

    @Override
    public void run() {
        synchronized (this) {
            // 增加同步锁，确保线程安全
            ++i;
            try {
                // 休眠10秒,模拟耗时操作
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ++j;
        }
    }

    /**
     * 打印i和j
     */
    public void print() {
        System.out.println("i=" + i + " j=" + j);
    }
}
