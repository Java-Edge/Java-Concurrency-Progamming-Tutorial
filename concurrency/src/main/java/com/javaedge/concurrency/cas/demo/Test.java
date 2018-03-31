package com.javaedge.concurrency.cas.demo;

import com.javaedge.concurrency.cas.demo.Node;
import com.javaedge.concurrency.cas.demo.Stack;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        Stack stack = new Stack();
        stack.push(new Node("B"));
        stack.push(new Node("A"));

        Thread thread1 = new Thread(() -> {
            try {
                System.out.println(Thread.currentThread() + " 拿到数据：" + stack.pop(3));
                // #再继续拿，就会有问题了，理想情况stack出数据应该是 A->C->D->B，实际上ABA问题导致A-B->null
                System.out.println(Thread.currentThread() + " 拿到数据：" + stack.pop(0));
                System.out.println(Thread.currentThread() + " 拿到数据：" + stack.pop(0));
                System.out.println(Thread.currentThread() + " 拿到数据：" + stack.pop(0));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread1.start();

        Thread.sleep(300); // 让线程1先启动

        Thread thread2 = new Thread(() -> {
            Node A = null;
            try {
                A = stack.pop(0);
                System.out.println(Thread.currentThread() + " 拿到数据：" + A);
                stack.push(new Node("D"));
                stack.push(new Node("C"));
                stack.push(A);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread2.start();
    }
}
