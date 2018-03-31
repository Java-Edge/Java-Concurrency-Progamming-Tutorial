package ThreadPerMessage.A7_1;

public class Blackhole {
    public static void enter(Object obj) {
        System.out.println("Step 1");
        magic(obj);
        System.out.println("Step 2");
        synchronized (obj) {
            System.out.println("Step 3 (never reached here)");  //  ���ᵽ������
        }
    }
    public static void magic(final Object obj) {
        // thread��ȡ��obj��lock���������ѭ����ִ����
        // ��thread�����Ƶ���Guard������ʹ��
        Thread thread = new Thread() {      // inner class
            public void run() {
                synchronized (obj) { // �ڴ�ȡ��obj������
                    synchronized (this) {
                        this.setName("Locked"); // Guard�����ı仯
                        this.notifyAll();       // ֪ͨ�Ѿ�ȡ��obj������
                    }
                    while (true) {
                        // ����ѭ��
                    }
                }
            }
        };
        synchronized (thread) {
            thread.setName("");
            thread.start(); // �̵߳�����
            // Guarded Suspensionģʽ
            while (thread.getName().equals("")) {
                try {
                    thread.wait(); //  �ȴ��µ��߳�ȡ��obj������
                } catch (InterruptedException e) {
                }
            }
        }
    }
}
