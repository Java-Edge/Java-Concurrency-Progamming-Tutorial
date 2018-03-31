package com.javaedge.concurrency.common.stop;

import com.javaedge.concurrency.annoations.ThreadSafe;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.concurrent.GuardedBy;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author JavaEdge
 * @date 2021/5/8
 */
@ThreadSafe
@Slf4j
public class PrimeGenerator implements Runnable {
    private static ExecutorService exec = Executors.newCachedThreadPool();

    @GuardedBy("this")
    private final List<BigInteger> primes = new ArrayList<>();
    private volatile boolean cancelled;

    @Override
    public void run() {
        BigInteger p = BigInteger.ONE;
        // 主循环在搜索下一个素数之前会首先检查取消标志
        while (!cancelled) {
            // 持续枚举素数，直到被取消
            p = p.nextProbablePrime();
            synchronized (this) {
                primes.add(p);
            }
        }
    }

    public void cancel() {
        cancelled = true;
    }

    public synchronized List<BigInteger> get() {
        return new ArrayList<>(primes);
    }

    static List<BigInteger> aSecondOfPrimes() throws InterruptedException {
        PrimeGenerator generator = new PrimeGenerator();
        exec.execute(generator);
        try {

            /**
             * 素数生成器通常并不会刚好在运行 1s 后停止，因为在 请求取消的时刻 和 run方法中循环执行下一次检查 之间可能存在延迟
             */
            SECONDS.sleep(1);
        } finally {
            // cancel方法由finally块调用，从而确保即使在调用sleep时被中断，也能取消素数生成器的执行
            // 如果cancel没有被调用，那么搜索素数的线程将永远运行下去，不断消耗CPU时钟周期，且使得JVM不能正常退出
            generator.cancel();
        }
        return generator.get();
    }

    public static void main(String[] args) throws InterruptedException {
        log.info("result:{}", aSecondOfPrimes());
    }
}

