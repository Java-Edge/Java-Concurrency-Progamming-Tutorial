package com.javaedge.concurrency.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class Test {

    public static void main(String[] args) {
        Test test = new Test();
        test.initDisruptor();
    }

    private Disruptor<RbCmd> disruptor;

    private static final EventTranslatorOneArg<RbCmd, RbData> RUB_TRANSLSLATOR = (rbCmd, seq, rbData) -> {
        rbCmd.setCode(rbData.getCode());
        rbCmd.setMsg(rbData.getMsg());
    };

    public void initDisruptor() {
        ThreadFactory threadFactory = new ThreadFactory() {
            private final AtomicInteger threadNum = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, "aft_core-" + threadNum.getAndIncrement());
                t.setDaemon(true);
                return t;
            }
        };

        disruptor = new Disruptor<>(
                new RbCmdFactory(),
                1024,
                threadFactory,
                ProducerType.SINGLE,
                new BlockingWaitStrategy()
        );

        final DisruptorExceptionHandler<RbCmd> exceptionHandler = new DisruptorExceptionHandler<>(
                "disruptor-1",
                (ex, seq) -> {
                    log.error("exception thrown on seq={}", seq, ex);
                });
        disruptor.setDefaultExceptionHandler(exceptionHandler);

        ConsumerA consumerA = new ConsumerA();
        ConsumerB consumerB = new ConsumerB();
        disruptor.handleEventsWith(consumerA).then(consumerB);
        disruptor.start();

        new Timer().schedule(new ProductTask(), 2000, 1000);
    }

    private int index = 0;

    private class ProductTask extends TimerTask {

        @Override
        public void run() {
            disruptor.getRingBuffer().publishEvent(RUB_TRANSLSLATOR, new RbData(index, "msg-" + index));
            index++;
        }
    }

    private class ConsumerA implements EventHandler<RbCmd> {
        @Override
        public void onEvent(RbCmd event, long sequence, boolean endOfBatch) throws Exception {
            log.info("ConsumerA recv: {}", event);
        }
    }

    private class ConsumerB implements EventHandler<RbCmd> {
        @Override
        public void onEvent(RbCmd event, long sequence, boolean endOfBatch) throws Exception {
            log.info("ConsumerB recv: {}", event);
        }
    }
}