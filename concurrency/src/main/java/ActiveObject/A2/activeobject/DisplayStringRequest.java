package ActiveObject.A2.activeobject;

class DisplayStringRequest extends MethodRequest {
    private final String string;
    public DisplayStringRequest(Servant servant, String string) {
        super(servant, null);
        this.string = string;
    }
    public void execute() {
        servant.displayString(string);
    }
}
