package pck.rcclient.api.request;

public abstract class BaseRequest {
    protected REQUEST_TYPE type;

    public BaseRequest(REQUEST_TYPE type) {
        this.type = type;
    }

    public REQUEST_TYPE getType() {
        return type;
    }

    public void setType(REQUEST_TYPE type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "BaseReq{" +
                "type=" + type +
                '}';
    }
}
