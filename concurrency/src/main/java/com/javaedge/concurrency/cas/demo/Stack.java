package com.javaedge.concurrency.cas.demo;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 实现一个 栈（后进先出）
 *
 * @author JavaEdge
 * @date 2019/10/20
 */
public class Stack {

    /**
     * top cas无锁修改
     */
    AtomicReference<Node> top = new AtomicReference<>();

    public void push(Node node) { // 入栈
        Node oldTop;
        do {
            oldTop = top.get();
            node.next = oldTop;
        }
        // CAS 替换栈顶
        while (!top.compareAndSet(oldTop, node));
    }

    /**
     * 为了演示ABA效果， 增加一个CAS操作的延时
     *
     * @param time
     * @return
     * @throws InterruptedException
     */
    public Node pop(int time) throws InterruptedException { // 出栈 -- 取出栈顶

        Node newTop;
        Node oldTop;
        do {
            oldTop = top.get();
            if (oldTop == null) {
                return null;
            }
            newTop = oldTop.next;
            if (time != 0) {
                System.out.println(Thread.currentThread() + " 睡一下，预期拿到的数据" + oldTop.item);
                // 休眠指定的时间
                TimeUnit.SECONDS.sleep(time);
            }
        }
        while (!top.compareAndSet(oldTop, newTop));
        return oldTop;
    }
}
