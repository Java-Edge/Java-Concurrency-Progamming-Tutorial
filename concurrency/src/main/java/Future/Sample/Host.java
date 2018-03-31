package Future.Sample;

public class Host {
    public Data request(final int count, final char c) {
        System.out.println("    request(" + count + ", " + c + ") BEGIN");

        // (1) ����FutureData��ʵ��
        final FutureData future = new FutureData();

        // (2) Ϊ�˽���RealData��ʵ�壬�����µ��߳�
        new Thread() {                                      
            public void run() {                             
                RealData realdata = new RealData(count, c);
                future.setRealData(realdata);
            }                                               
        }.start();                                          

        System.out.println("    request(" + count + ", " + c + ") END");

        // (3) ȡ��FutureDataʵ�壬��Ϊ����ֵ�^��
        return future;
    }
}
