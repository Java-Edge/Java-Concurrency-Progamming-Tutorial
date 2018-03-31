package com.javaedge.concurrency.common.stop;

/**
 * 线程stop强制性中止，破坏线程安全的示例
 *
 * @author JavaEdge
 * @date 2019/8/26
 */
public class ThreadStop {
    public static void main(String[] args) throws InterruptedException {
        StopThread thread = new StopThread();
        thread.start();
        // 休眠1秒，确保i变量自增成功
        Thread.sleep(1000);
        // 暂停线程
        // thread.stop(); // 错误的终止
        thread.interrupt(); // 正确的终止
        while (thread.isAlive()) {
            // 确保线程已经终止
        } // 输出结果
        thread.print();
    }
}



