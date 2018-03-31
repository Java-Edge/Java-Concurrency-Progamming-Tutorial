package com.javaedge.concurrency.example.sync;

/**
 * 锁 方法(静态/非静态),代码块(对象/类)
 *
 * @author JavaEdge
 * @date 2019/10/20
 */
public class ObjectSyncDemo1 {

    static Object temp = new Object();

    // 方法上面：锁的对象 是 类的一个实例
    public void test1() {
        // 类锁(class对象，静态方法)，实例锁(this，普通方法)
        synchronized (this) {
            try {
                System.out.println(Thread.currentThread() + " 我开始执行");
                Thread.sleep(3000L);
                System.out.println(Thread.currentThread() + " 我执行结束");
            } catch (InterruptedException e) {
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> new ObjectSyncDemo1().test1()).start();

        // 等1秒钟,让前一个线程启动起来
        Thread.sleep(1000L);
        new Thread(() -> {
            new ObjectSyncDemo1().test1();
        }).start();
    }
}
