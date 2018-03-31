package com.javaedge.concurrency.common.stop;

/**
 * @author JavaEdge
 * @date 2019/8/26
 */
public class ThreadStop {
    public static void main(String[] args) throws InterruptedException {
        StopThread thread = new StopThread();
        thread.start();
        // 休眠1秒，确保i变量自增成功
        Thread.sleep(1000);
        // 错误的终止 线程stop强制性中止，破坏线程安全
//         thread.stop();

        // 正确的终止
         thread.interrupt();
        while (thread.isAlive()) {
            // 确保线程已经终止
        } // 输出结果
        thread.print();
    }
}



