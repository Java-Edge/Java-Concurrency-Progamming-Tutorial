package Future.Q3.content;

public class Retriever {
    public static Content retrieve(String urlstr) {
        return new SyncContentImpl(urlstr);
    }
}
