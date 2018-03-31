package main.java.workerthread.v1;

/**
 * 工作线程
 *
 * @author JavaEdge
 */
public class WorkerThread extends Thread {

    private final Channel channel;

    public WorkerThread(String name, Channel channel) {
        super(name);
        this.channel = channel;
    }

    @Override
    public void run() {
        while (true) {
            // 获得一个 Req 实例
            Request request = channel.takeRequest();
            // 执行之
            request.execute();
        }
    }
}
