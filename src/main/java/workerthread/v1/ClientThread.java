package main.java.workerthread.v1;

import java.util.Random;

/**
 * 发送工作请求
 *
 * @author JavaEdge
 */
public class ClientThread extends Thread {
    private final Channel channel;

    private static final Random random = new Random();

    public ClientThread(String name, Channel channel) {
        super(name);
        this.channel = channel;
    }

    @Override
    public void run() {
        for (int i = 0; true; i++) {
            // 创建请求实例
            Request request = new Request(getName(), i);
            channel.putRequest(request);
            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
