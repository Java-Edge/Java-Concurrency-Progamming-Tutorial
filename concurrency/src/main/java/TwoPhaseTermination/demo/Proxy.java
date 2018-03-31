package TwoPhaseTermination.demo;

/**
 * @author JavaEdge
 * @date 2021/5/23
 */
class Proxy {
    /**
     * 很可能在线程的run()中调用第三方类库提供的方法，但我们无法保证第三方类库正确处理了线程的中断异常
     * 例如第三方类库在捕获到Thread.sleep()抛出的中断异常后，
     * 没有重新设置线程的中断状态，则会导致线程不能够正常终止
     * 所以推荐设置自己的线程终止标志位
     *
     * 线程终止标志位
     * 变量被多个线程访问，需要保证可见性
     */
    volatile boolean terminated = false;

    boolean started = false;

    /**
     * 采集线程
     */
    Thread rptThread;

    /**
     * 启动采集功能
     */
    synchronized void start() {
        // 不允许同时启动多个采集线程
        if (started) {
            return;
        }
        started = true;
        terminated = false;
        rptThread = new Thread(() -> {
            // start方法里又启动了一个新的线程，synchronized管不到这个新的线程,所以terminated需要被 volatile 修饰
            while (!terminated) {
                //省略采集、回传实现
                report();
                //每隔两秒钟采集、回传一次数据
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    //重新设置线程的中断状态    标志位是线程的中断状态
                    // 捕获Thread.sleep()的中断异常后，通过 Thread.currentThread().interrupt() 重新设置线程的中断状态
                    // 因为JVM的异常处理会清除线程的中断状态
                    Thread.currentThread().interrupt();
                }
            }

            // 执行到此处说明线程马上终止
            started = false;
        });
        rptThread.start();
    }

    /**
     * 优雅终止线程
     * 终止采集功能
     */
    synchronized void stop() {

        // 设置中断标志位
        terminated = true;
        // 中断线程rptThread  rptThread状态=》RUNNABLE
        rptThread.interrupt();
    }

    private void report() {
    }
}
