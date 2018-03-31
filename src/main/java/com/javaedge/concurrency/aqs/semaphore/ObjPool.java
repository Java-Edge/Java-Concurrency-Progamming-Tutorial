package com.javaedge.concurrency.aqs.semaphore;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.Semaphore;
import java.util.function.Function;

/**
 * @author JavaEdge
 * @date 2021/4/22
 */
@Slf4j
public class ObjPool<T, R> {

    final List<T> pool;

    /**
     * 用信号量实现限流器
     */
    final Semaphore sem;

    ObjPool(int size, T t) {
        // 使用线程安全的vector,因为信号量支持多个线程进入临界区
        // 执行 add、remove 方法时可能有并发
        pool = new Vector<T>() {};
        for (int i = 0; i < size; i++) {
            pool.add(t);
        }
        sem = new Semaphore(size);
    }

    /**
     * 利用对象池的对象，调用func
     */
    R exec(Function<T, R> func) throws InterruptedException {
        T t = null;
        // 前10个线程调用acquire()方法，都能继续执行，相当于通过了信号灯
        // 而其他线程则会阻塞在acquire()
        sem.acquire();
        try {
            // 通过信号灯的线程，为每个线程分配一个对象 t
            t = pool.remove(0);
            // 执行回调方法
            R apply = func.apply(t);
            return apply;
        } finally {
            // 执行完回调后，释放对象
            pool.add(t);

            /**
             * 更新信号量的计数器
             * 如果此时信号量里计数器的值≤0，说明有线程在等待
             * 此时会自动唤醒等待的线程
             */
            sem.release();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // 创建对象池
        ObjPool<Long, String> pool = new ObjPool<>(10, 2L);
        // 通过对象池获取t，之后执行
        pool.exec(t -> {
            log.info("t:{}", t);
            return t.toString();
        });
    }
}

