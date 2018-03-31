package TwoPhaseTermination.A5;

public class Service {
    private static GracefulThread thread = null;

   
    public synchronized static void service() {
        System.out.print("service");
        if (thread != null && thread.isAlive()) {
            // Balking
            System.out.println(" is balked.");
            return;
        }
        // Thread-Per-Message
        thread = new ServiceThread();
        thread.start();
    }

  
    public synchronized static void cancel() {
        if (thread != null) {
            System.out.println("cancel.");
            thread.shutdownRequest();
        }
    }
}
