package com.javaedge.concurrency.cas.demo;

import com.javaedge.concurrency.cas.demo.Node;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;

public class ConcurrentStack {
    AtomicStampedReference<Node> top = new AtomicStampedReference<Node>(null,0);
    public void push(Node node){
        Node oldTop;
        int v;
        do{
            v=top.getStamp();
            oldTop = top.getReference();
            node.next = oldTop;
        }
        while(!top.compareAndSet(oldTop, node,v,v+1));
        //   }while(!top.compareAndSet(oldTop, node,top.getStamp(),top.getStamp()+1));
    }
    public Node pop(int time){
        Node newTop;
        Node oldTop;
        int v;
        do{
            v=top.getStamp();
            oldTop = top.getReference();
            if(oldTop == null){
                return null;
            }
            newTop = oldTop.next;
            try {
                if (time != 0) {
                    System.out.println(Thread.currentThread() + " 睡一下，预期拿到的数据" + oldTop.item);
                    TimeUnit.SECONDS.sleep(time); // 休眠指定的时间
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while(!top.compareAndSet(oldTop, newTop,v,v+1));
        //   }while(!top.compareAndSet(oldTop, newTop,top.getStamp(),top.getStamp())); 
        return oldTop;
    }
    public void get(){
        Node node = top.getReference();
        while(node!=null){
            System.out.println(node.item);
            node = node.next;
        }
    }
}