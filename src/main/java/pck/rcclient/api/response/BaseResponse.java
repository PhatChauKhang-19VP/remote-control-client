package pck.rcclient.api.response;

import pck.rcclient.api.request.REQUEST_TYPE;

public abstract class BaseResponse {
    protected RESPONSE_STATUS status;
    protected String msg;
    protected REQUEST_TYPE type;

    public BaseResponse(RESPONSE_STATUS status, String msg, REQUEST_TYPE type) {
        this.status = status;
        this.msg = msg;
        this.type = type;
    }

    public RESPONSE_STATUS getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public REQUEST_TYPE getType() {
        return type;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", type=" + type +
                '}';
    }
}
