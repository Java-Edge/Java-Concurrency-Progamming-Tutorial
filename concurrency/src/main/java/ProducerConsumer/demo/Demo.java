package ProducerConsumer.demo;

import org.springframework.scheduling.config.Task;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author JavaEdge
 * @date 2021/5/23
 */
public class Demo {

    /**
     * 任务队列
     */
    BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>(2000);

    //启动5个消费者线程 负责批量执行SQL
    void start() {
        ExecutorService consumer = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            consumer.execute(() -> {
                try {
                    while (true) {
                        //获取批量任务
                        List<Task> ts = pollTasks();
                        //执行批量任务
                        execTasks(ts);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * 从任务队列中获取批量任务
     */
    List<Task> pollTasks() throws InterruptedException {
        List<Task> ts = new LinkedList<>();
        // 阻塞式地获取一条任务
        // 如果任务队列中没有任务，这样能够避免无意义的循环
        Task t = taskQueue.take();
        while (t != null) {
            ts.add(t);
            // 非阻塞式地获取一条任务
            t = taskQueue.poll();
        }
        return ts;
    }

    /**
     * 批量执行任务
     *
     * @param ts
     */
    public void execTasks(List<Task> ts) {
        // ...
    }


}
