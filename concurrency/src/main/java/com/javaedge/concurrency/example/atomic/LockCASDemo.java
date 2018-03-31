package com.javaedge.concurrency.example.atomic;

import sun.misc.Unsafe;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * 两个线程，对 i 变量进行递增操作
 *
 * @author JavaEdge
 * @date 2019/10/18
 */
public class LockCASDemo {

    volatile int i = 0;

    private static Unsafe unsafe;

    /**
     *
     * 属性偏移量，用于JVM去定位属性在内存中的地址
     */
    static long valueOffset;

    static {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            unsafe = (Unsafe) theUnsafe.get(null);

            // CAS 硬件原语 ---java语言 无法直接改内存。 曲线通过对象及属性的定位方式
            valueOffset = unsafe.objectFieldOffset(LockCASDemo.class.getDeclaredField("i"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // unsafe =
    }

    public void add() { // 方法栈帧~ 局部变量
        // 无锁编程 -- cas + 自旋锁
        // TODO xx00
        // i++; // 三次操作
        int current;
        int value;
        do {
            current = unsafe.getIntVolatile(this, valueOffset); // 读取当前值
            value = current + 1; // 计算
        } while (!unsafe.compareAndSwapInt(this, valueOffset, current, value));// CAS 底层API

//        if(current == i) {
//            i = value; // 赋值
//        } else {
//            // 值发生变化，修改失败
//        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        LockCASDemo ld = new LockCASDemo();

        for (int i = 0; i < 2; i++) { // 2w相加，20000
            new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    ld.add();
                }
            }).start();
        }
        System.in.read(); // 输入任意键退出
        System.out.println(ld.i);
    }
}

