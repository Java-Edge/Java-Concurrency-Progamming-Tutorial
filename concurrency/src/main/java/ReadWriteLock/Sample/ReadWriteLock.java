package ReadWriteLock.Sample;

public final class ReadWriteLock {
    private int readingReaders = 0; // (A)...ʵ�����ڶ�ȡ��ִ��������
    private int waitingWriters = 0; // (B)...���ڵȴ�д���ִ��������
    private int writingWriters = 0; // (C)...ʵ������д���ִ��������
    private boolean preferWriter = true; // д�����ȵĻ���ֵΪtrue

    public synchronized void readLock() throws InterruptedException {
        while (writingWriters > 0 || (preferWriter && waitingWriters > 0)) {
            wait();
        }
        readingReaders++;                       //  (A)ʵ�����ڶ�ȡ���߳�������1
    }

    public synchronized void readUnlock() {
        readingReaders--;                       //  (A)ʵ�����ڶ�ȡ���߳�������1
        preferWriter = true;
        notifyAll();
    }

    public synchronized void writeLock() throws InterruptedException {
        waitingWriters++;                       // (B)���ڵȴ�д����߳�������1
        try {
            while (readingReaders > 0 || writingWriters > 0) {
                wait();
            }
        } finally {1
          waitingWriters--;                   // (B)���ڵȴ�д����߳�������1
        }
        writingWriters++;                       //  (C)ʵ������д����߳�������1
    }

    public synchronized void writeUnlock() {
        writingWriters--;                       // (C)ʵ������д����߳�������
        preferWriter = false;
        notifyAll();
    }
}
