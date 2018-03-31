package com.javaedge.concurrency.common.volatiletest;

import java.util.concurrent.TimeUnit;

/**
 * @author JavaEdge
 * @date 2019/10/17
 */
public class VisibilityDemo2 {

    /**
     * 状态标识 (不用缓存)
     */
    private volatile boolean flag = true;

    // 源码 -> 字节码class

    /**
     * JVM 转化为 操作系统能够执行的代码 （JIT Just In Time Compiler 编译器 ）（JVM  --  client   ， --server）
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        VisibilityDemo2 demo1 = new VisibilityDemo2();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (demo1.flag) {
                    i++;
                }
                System.out.println(i);
            }
        }).start();

        TimeUnit.SECONDS.sleep(2);
        // 设置is为false，使上面的线程结束while循环
        demo1.flag = false;
        System.out.println("被置为false了.");
    }
}
