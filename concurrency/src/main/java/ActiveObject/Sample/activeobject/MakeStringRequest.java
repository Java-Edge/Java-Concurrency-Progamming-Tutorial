package ActiveObject.Sample.activeobject;

class MakeStringRequest extends MethodRequest {
    private final int count;
    private final char fillchar;
    public MakeStringRequest(Servant servant, FutureResult future, int count, char fillchar) {
        super(servant, future);
        this.count = count;
        this.fillchar = fillchar;
    }
    public void execute() {
        Result result = servant.makeString(count, fillchar);
        future.setResult(result);
    }
}
