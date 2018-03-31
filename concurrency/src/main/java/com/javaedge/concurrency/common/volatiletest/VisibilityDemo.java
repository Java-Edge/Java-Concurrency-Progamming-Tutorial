package com.javaedge.concurrency.common.volatiletest;

/**
 * @author JavaEdge
 * @date 2019/10/17
 */
import java.util.concurrent.TimeUnit;

// 1、 jre/bin/server  放置hsdis动态链接库
//  测试代码 将运行模式设置为-server， 变成死循环   。 没加默认就是client模式，就是正常（可见性问题）
// 2、 通过设置JVM的参数，打印出jit编译的内容 （这里说的编译非class文件），通过可视化工具jitwatch进行查看
// -server -XX:+UnlockDiagnosticVMOptions -XX:+PrintAssembly -XX:+LogCompilation -XX:LogFile=jit.log
//  关闭jit优化-Djava.compiler=NONE

public class VisibilityDemo {

    /**
     * 运行标志
     */
    public boolean flag = true;

    /**
     * JIT just in time
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        VisibilityDemo demo1 = new VisibilityDemo();
        System.out.println("代码开始了");
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
//                boolean f = demo1.flag; 不加valo是的jvm优化
//                if(f) {
//                    while(true){
//                        i++;
//                    }
//                }
                while (demo1.flag) {
                    synchronized (this) {
                        i++;
                    }
                }
                System.out.println(i);
            }
        });
        thread1.start();

        TimeUnit.SECONDS.sleep(2);
        // 设置is为false，使上面的线程结束while循环
        demo1.flag = false;
        System.out.println("被置为false了.");
    }
}
