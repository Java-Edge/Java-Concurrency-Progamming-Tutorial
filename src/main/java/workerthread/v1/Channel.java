package main.java.workerthread.v1;

/**
 * 传递工作请求及保存工作线程
 *
 * @author JavaEdge
 */
public class Channel {
    private static final int MAX_REQUEST = 100;

    /**
     * 请求的队列
     */
    private final Request[] requestQueue;

    /**
     * 下次 putReq 的位置
     */
    private int tail;

    /**
     * 下次 takeReq 的位置
     */
    private int head;

    /**
     * Req 的数量
     */
    private int count;

    private final WorkerThread[] threadPool;

    public Channel(int threads) {
        this.requestQueue = new Request[MAX_REQUEST];
        this.head = 0;
        this.tail = 0;
        this.count = 0;

        threadPool = new WorkerThread[threads];
        for (int i = 0; i < threadPool.length; i++) {
            threadPool[i] = new WorkerThread("Worker-" + i, this);
        }
    }

    public void startWorkers() {
        for (int i = 0; i < threadPool.length; i++) {
            threadPool[i].start();
        }
    }

    /**
     * 将请求放入队列
     *
     * @param request
     */
    public synchronized void putRequest(Request request) {
        while (count >= requestQueue.length) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        requestQueue[tail] = request;
        tail = (tail + 1) % requestQueue.length;
        count++;
        notifyAll();
    }

    /**
     * 取出队列中的请求
     *
     * @return
     */
    public synchronized Request takeRequest() {
        while (count <= 0) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        Request request = requestQueue[head];
        head = (head + 1) % requestQueue.length;
        count--;
        notifyAll();
        return request;
    }
}
