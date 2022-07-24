package pck.rcclient.api.request;

public class StopApplicationRequest extends BaseRequest {
    private int pid;

    public StopApplicationRequest(int nameApp) {
        super(REQUEST_TYPE.STOP_APP);
        this.pid = nameApp;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    @Override
    public String toString() {
        return "StopApplicationRequest{" +
                "type=" + type +
                ", pid=" + pid +
                '}';
    }
}
