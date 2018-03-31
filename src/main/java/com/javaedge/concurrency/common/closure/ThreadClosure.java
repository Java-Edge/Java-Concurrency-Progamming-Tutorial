package com.javaedge.concurrency.common.closure;

import org.testng.annotations.Test;


/**
 *
 * @author JavaEdge
 * @date 2019/10/9
 */
public class ThreadClosure {

    /**
     * threadLocal变量，每个线程都有一个副本，互不干扰
     */
    public static ThreadLocal<String> value = new ThreadLocal<>();

    @Test
    public void threadLocalTest() throws Exception {

        // 主线程设值
        value.set("这是主线程设置的123");
        String v = value.get();
        System.out.println("线程1执行之前，主线程取到的值：" + v);

        new Thread(() -> {
            String v1 = value.get();
            System.out.println("线程1取到的值：" + v1);
            // 设置 threadLocal
            value.set("这是线程1设置的456");

            v1 = value.get();
            System.out.println("重新设置之后，线程1取到的值：" + v1);
            System.out.println("线程1执行结束");
        }).start();

        // 等待所有线程执行结束
        Thread.sleep(5000L);

        v = value.get();
        System.out.println("线程1执行之后，主线程取到的值：" + v);

    }
}

