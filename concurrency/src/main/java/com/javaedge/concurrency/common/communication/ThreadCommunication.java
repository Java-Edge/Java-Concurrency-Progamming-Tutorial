package com.javaedge.concurrency.common.communication;

import java.util.concurrent.locks.LockSupport;

import org.junit.Test;

/**
 * 三种线程协作通信的方式：suspend/resume、wait/notify、park/unpark
 *
 * @author JavaEdge
 * @date 2019/10/8
 */
public class ThreadCommunication {

    /**
     * 包子店
     */
    public static Object bunShop = null;

    /**
     * 正常的suspend/resume
     */
    @Test
    public void suspendResumeTest() throws Exception {
        // 启动线程
        Thread consumerThread = new Thread(() -> {
            if (bunShop == null) {
                // 没包子，则进入等待
                System.out.println("1、进入等待");
                Thread.currentThread().suspend();
            }
            System.out.println("2、买到包子，回家");
        });
        consumerThread.start();
        // 3秒之后，生产一个包子
        Thread.sleep(3000L);
        bunShop = new Object();
        consumerThread.resume();
        System.out.println("3、通知消费者");
    }

    /**
     * 死锁的suspend/resume。 suspend并不会像wait一样释放锁，容易写出死锁代码
     */
    @Test
    public void suspendResumeDeadLockTest() throws Exception {
        // 启动线程
        Thread consumerThread = new Thread(() -> {
            if (bunShop == null) {
                // 没包子，则进入等待
                System.out.println("1、进入等待");
                // 当前线程拿到锁，然后挂起
                synchronized (this) {
                    Thread.currentThread().suspend();
                }
            }
            System.out.println("2、买到包子，回家");
        });
        consumerThread.start();
        // 3秒之后，生产一个包子
        Thread.sleep(3000L);
        bunShop = new Object();
        // 争取到锁以后，再恢复consumerThread
        synchronized (this) {
            consumerThread.resume();
        }
        System.out.println("3、通知消费者");
    }

    /**
     * 导致程序永久挂起的suspend/resume
     */
    @Test
    public void suspendResumeDeadLockTest2() throws Exception {
        // 启动线程
        Thread consumerThread = new Thread(() -> {
            if (bunShop == null) {
                System.out.println("1、没包子，进入等待");
                try { // 为这个线程加上一点延时
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 这里的挂起执行在resume后面
                Thread.currentThread().suspend();
            }
            System.out.println("2、买到包子，回家");
        });
        consumerThread.start();
        // 3秒之后，生产一个包子
        Thread.sleep(3000L);
        bunShop = new Object();
        consumerThread.resume();
        System.out.println("3、通知消费者");
        consumerThread.join();
    }

    /**
     * 正常的wait/notify
     */
    @Test
    public void waitNotifyTest() throws Exception {
        // 启动消费者线程
        new Thread(() -> {
            if (bunShop == null) {
                // 如果没包子，则进入等待
                synchronized (this) {
                    try {
                        System.out.println("1、进入等待");
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("2、买到包子，回家");
        }).start();
        // 3秒之后，生产一个包子
        Thread.sleep(3000L);
        bunShop = new Object();
        synchronized (this) {
            this.notifyAll();
            System.out.println("3、通知消费者");
        }
    }

    /**
     * 会导致程序永久等待的wait/notify
     */
    @Test
    public void waitNotifyDeadLockTest() throws Exception {
        // 启动线程
        new Thread(() -> {
            if (bunShop == null) { // 如果没包子，则进入等待
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                synchronized (this) {
                    try {
                        System.out.println("1、进入等待");
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("2、买到包子，回家");
        }).start();
        // 3秒之后，生产一个包子
        Thread.sleep(3000L);
        bunShop = new Object();
        synchronized (this) {
            this.notifyAll();
            System.out.println("3、通知消费者");
        }
    }

    /**
     * 正常的park/unpark
     */
    @Test
    public void parkUnparkTest() throws Exception {
        // 启动线程
        Thread consumerThread = new Thread(() -> {
            if (bunShop == null) { // 如果没包子，则进入等待
                System.out.println("1、进入等待");
                LockSupport.park();
            }
            System.out.println("2、买到包子，回家");
        });
        consumerThread.start();
        // 3秒之后，生产一个包子
        Thread.sleep(3000L);
        bunShop = new Object();
        LockSupport.unpark(consumerThread);
        System.out.println("3、通知消费者");
    }

    /**
     * 死锁的park/unpark
     */
    @Test
    public void parkUnparkDeadLockTest() throws Exception {
        // 启动线程
        Thread consumerThread = new Thread(() -> {
            if (bunShop == null) { // 如果没包子，则进入等待
                System.out.println("1、进入等待");
                // 当前线程拿到锁，然后挂起
                synchronized (this) {
                    LockSupport.park();
                }
            }
            System.out.println("2、买到包子，回家");
        });
        consumerThread.start();
        // 3秒之后，生产一个包子
        Thread.sleep(3000L);
        bunShop = new Object();
        // 争取到锁以后，再恢复consumerThread
        synchronized (this) {
            LockSupport.unpark(consumerThread);
        }
        System.out.println("3、通知消费者");
    }

    public static void main(String[] args) throws Exception {
        // 对调用顺序有要求，也要开发自己注意锁的释放。这个被弃用的API， 容易死锁，也容易导致永久挂起。
        // new Demo6().suspendResumeTest();
        // new Demo6().suspendResumeDeadLockTest();
        // new Demo6().suspendResumeDeadLockTest2();

        // wait/notify要求再同步关键字里面使用，免去了死锁的困扰，但是一定要先调用wait，再调用notify，否则永久等待了
        // new Demo6().waitNotifyTest();
        // new Demo6().waitNotifyDeadLockTest();

        // park/unpark没有顺序要求，但是park并不会释放锁，所有再同步代码中使用要注意
        // new Demo6().parkUnparkTest();
        // new Demo6().parkUnparkDeadLockTest();

    }
}
