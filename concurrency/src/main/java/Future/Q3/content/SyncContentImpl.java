package Future.Q3.content;

import java.io.DataInputStream;
import java.io.EOFException;
import java.net.URL;

class SyncContentImpl implements Content {
    private byte[] contentbytes;
    public SyncContentImpl(String urlstr) {
        System.out.println(Thread.currentThread().getName() + ": Getting " + urlstr);
        try {
            URL url = new URL(urlstr);
            DataInputStream in = new DataInputStream(url.openStream());
            byte[] buffer = new byte[1];
            int index = 0;
            try {
                while (true) {
                    int c = in.readUnsignedByte();
                    if (buffer.length <= index) {
                        byte[] largerbuffer = new byte[buffer.length * 2];
                        System.arraycopy(buffer, 0, largerbuffer, 0, index);
                        buffer = largerbuffer;
                        // System.out.println("Enlarging buffer to " + buffer.length);
                    }
                    buffer[index++] = (byte)c;
                    // System.out.print("Getting " + index + " bytes from " + urlstr);
                }
            } catch (EOFException e) {
            } finally {
                in.close();
            }
            contentbytes = new byte[index];
            System.arraycopy(buffer, 0, contentbytes, 0, index);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public byte[] getBytes() {
        return contentbytes;
    }
}
