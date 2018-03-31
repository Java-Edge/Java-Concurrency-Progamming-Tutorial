package ProducerConsumer.A5;

public class ClearThread extends Thread {
    private final Table table;
    public ClearThread(String name, Table table) {
        super(name);
        this.table = table;
    }
    public void run() {
        try {
            while (true) {
                Thread.sleep(1000);
                System.out.println("===== " + getName() + " clears =====");
                table.clear();
            }
        } catch (InterruptedException e) {
        }
    }
}
