package com.javaedge.concurrency.example.sync;

// 可重入
public class ObjectSyncDemo2 {

    public synchronized void test1(Object arg) {
        // 继续执行，保证能读取到之前的修改 JMM
        System.out.println(Thread.currentThread() + " 我开始执行 " + arg);
        if (arg == null) {
            test1(new Object());
        }
        System.out.println(Thread.currentThread() + " 我执行结束" + arg);
    }

    public static void main(String[] args) throws InterruptedException {
        new ObjectSyncDemo2().test1(null);
    }
}
