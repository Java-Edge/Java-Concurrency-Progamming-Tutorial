package main.java.workerthread.v1;

/**
 * 工作请求
 *
 * @author JavaEdge
 */
public class Request {

    /**
     * 发送请求的委托人的名字
     */
    private final String name;

    /**
     * 请求的编号
     */
    private final int number;

    public Request(String name, int number) {
        this.name = name;
        this.number = number;
    }

    /**
     * 处理请求
     */
    public void execute() {
        System.out.println(Thread.currentThread().getName() + " executes " + this);
    }

    @Override
    public String toString() {
        return "[ Request from " + name + " No." + number + " ]";
    }
}
