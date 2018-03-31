package workerthread.v1;


/**
 * @author JavaEdge
 */
public class Main {
    public static void main(String[] args) {
        // 工作线程的个数
        Channel channel = new Channel(5);
        channel.startWorkers();
        new ClientThread("Alice", channel).start();
        new ClientThread("Bobby", channel).start();
        new ClientThread("Chris", channel).start();

        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
        }
        System.exit(0);
    }
}
