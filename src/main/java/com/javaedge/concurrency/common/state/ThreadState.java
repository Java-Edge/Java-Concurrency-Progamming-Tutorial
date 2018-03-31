package com.javaedge.concurrency.common.state;

/**
 * 多线程运行状态切换
 *
 * @author JavaEdge
 * @date 2019/8/26
 */
public class ThreadState {
    public static Thread thread1;
    public static ThreadState obj;

    public static void main(String[] args) throws Exception {

        // 第一种状态切换 - 新建 -> 运行 -> 终止
        System.out.println("=======  第一种状态切换  - 新建 -> 运行 -> 终止  =======");

        Thread thread1 = new Thread(() -> {
            System.out.println("thread1当前状态：" + Thread.currentThread().getState().toString());
            System.out.println("thread1 执行");
        });

        System.out.println("未调用start方法，thread1当前状态：" + thread1.getState().toString());

        thread1.start();

        // 等待thread1执行结束，再看状态
        Thread.sleep(2000L);
        System.out.println("等待2s，再看thread1当前状态：" + thread1.getState().toString());
        // thread1.start(); 线程终止之后，再调用start方法，会抛IllegalThreadStateException

        System.out.println("-------------------------------------");
        System.out.println("======= 第二种：新建 -> 运行 -> 等待 -> 运行 -> 终止(sleep方式) =======");
        Thread thread2 = new Thread(() -> {
            try {
                // 将线程2移动到等待状态，1500ms后自动唤醒
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("thread2当前状态：" + Thread.currentThread().getState().toString());
            System.out.println("thread2 执行");
        });
        System.out.println("未调用start方法，thread2当前状态：" + thread2.getState().toString());
        thread2.start();
        System.out.println("调用start方法，thread2当前状态：" + thread2.getState().toString());
        // 等待200毫秒，再看状态
        Thread.sleep(200L);
        System.out.println("等待200毫秒，再看thread2当前状态：" + thread2.getState().toString());
        // 再等待3秒，让thread2执行完毕，再看状态
        Thread.sleep(3000L);
        System.out.println("等待3秒，再看thread2当前状态：" + thread2.getState().toString());

        System.out.println("-------------------------------------");
        System.out.println("======= 第三种：新建 -> 运行 -> 阻塞 -> 运行 -> 终止 =======");
        Thread thread3 = new Thread(() -> {
            synchronized (ThreadState.class) {
                System.out.println("thread3当前状态：" + Thread.currentThread().getState().toString());
                System.out.println("thread3 执行了");
            }
        });
        synchronized (ThreadState.class) {
            System.out.println("未调用start方法，thread3当前状态：" + thread3.getState().toString());
            thread3.start();
            System.out.println("调用start方法，thread3当前状态：" + thread3.getState().toString());
            // 等待200毫秒，再看状态
            Thread.sleep(200L);
            System.out.println("等待200毫秒，再看thread3当前状态：" + thread3.getState().toString());
        }
        // 再等待3秒，让thread3执行完毕，再看状态
        Thread.sleep(3000L);
        System.out.println("等待3秒，让thread3抢到锁，再看thread3当前状态：" + thread2.getState().toString());

    }
}
