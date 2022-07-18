package pck.rcclient.api.request;

public class StopProcessRequest extends BaseRequest {
    private Integer pid;

    public StopProcessRequest(Integer pid) {
        super(REQUEST_TYPE.STOP_PROCESS);
        this.pid = pid;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    @Override
    public String toString() {
        return "StopProcessRequest{" +
                "type=" + type +
                ", pid=" + pid +
                '}';
    }
}
