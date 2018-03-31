package com.javaedge.concurrency.disruptor;

import com.lmax.disruptor.EventFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RbCmdFactory implements EventFactory<RbCmd> {
    @Override
    public RbCmd newInstance() {
        return RbCmd.builder()
                .code(0)
                .msg("")
                .build();
    }
}