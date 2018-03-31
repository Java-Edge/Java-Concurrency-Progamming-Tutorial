package com.javaedge.concurrency.disruptor;

import com.lmax.disruptor.ExceptionHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.function.BiConsumer;

@Slf4j
@AllArgsConstructor
public class DisruptorExceptionHandler<T> implements ExceptionHandler<T> {
    
    public final String name;

    public final BiConsumer<Throwable, Long> onException;

    @Override
    public void handleEventException(Throwable ex, long sequence, T event) {
        System.out.println("handleEventException: " + ex.getMessage());
        onException.accept(ex, sequence);
    }

    @Override
    public void handleOnStartException(Throwable ex) {
        System.out.println("handleOnStartException: " + ex.getMessage());
    }

    @Override
    public void handleOnShutdownException(Throwable ex) {
        System.out.println("handleOnShutdownException: " + ex.getMessage());
    }
}