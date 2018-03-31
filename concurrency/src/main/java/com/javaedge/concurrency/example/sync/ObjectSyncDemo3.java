package com.javaedge.concurrency.example.sync;

// 锁粗化(运行时 jit 编译优化)
// jit 编译后的汇编内容, jitwatch可视化工具进行查看
public class ObjectSyncDemo3 {
    int i;

    public void test1(Object arg) {
        synchronized (this) {
            i++;
        // }
        // synchronized (this) {
            i++;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 10000000; i++) {
            new ObjectSyncDemo3().test1("a");
        }
    }
}
