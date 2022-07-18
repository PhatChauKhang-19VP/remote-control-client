package pck.rcclient.api.request;

public class Request extends BaseRequest{
    public Request(REQUEST_TYPE type) {
        super(type);
    }

    @Override
    public String toString() {
        return "Request{" +
                "type=" + type +
                '}';
    }
}
