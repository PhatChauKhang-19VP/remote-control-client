package pck.rcclient.api.response;

import pck.rcclient.api.request.REQUEST_TYPE;

public class Response extends BaseResponse {
    public Response(REQUEST_TYPE type, RESPONSE_STATUS status, String msg) {
        super(status, msg, type);
    }

    @Override
    public String toString() {
        return "Response{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", type=" + type +
                '}';
    }
}
