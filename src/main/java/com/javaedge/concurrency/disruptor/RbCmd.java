package com.javaedge.concurrency.disruptor;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RbCmd {

    private int code;
    public String msg;
}