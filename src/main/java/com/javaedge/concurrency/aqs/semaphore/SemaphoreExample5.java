package com.javaedge.concurrency.aqs.semaphore;

import lombok.extern.slf4j.Slf4j;

import java.util.Queue;

/**
 * @author JavaEdge
 */
@Slf4j
public class SemaphoreExample5 {
    /**
     * 计数器
     */
    int count;

    /**
     * 等待队列
     */
    Queue queue;

    /**
     * 初始化
     *
     * @param c
     */
    void Semaphore(int c){
        this.count=c;
    }

    void down(){
        this.count--;
        if(this.count<0){
            // 将当前线程插入等待队列
            // 阻塞当前线程
        }
    }
    void up(){
        this.count++;
        if(this.count<=0) {
            // 移除等待队列中的某个线程t
            // 唤醒线程t
        }
    }

    public static void main(String[] args) {

    }
}
