package pck.rcclient.api.response;

import pck.rcclient.api.request.REQUEST_TYPE;

import java.util.ArrayList;

public class KeyStrokeUnhookResponse extends BaseResponse {
    private ArrayList<Integer> keypressList = new ArrayList<Integer>();

    public KeyStrokeUnhookResponse(RESPONSE_STATUS status, String msg, REQUEST_TYPE type) {
        super(status, msg, type);
    }

    public KeyStrokeUnhookResponse(RESPONSE_STATUS status, String msg, REQUEST_TYPE type, ArrayList<Integer> keypressList) {
        super(status, msg, type);
        this.keypressList = keypressList;
    }

    public ArrayList<Integer> getKeypressList() {
        return keypressList;
    }

    public void setKeypressList(ArrayList<Integer> keypressList) {
        this.keypressList = keypressList;
    }

    @Override
    public String toString() {
        return "KeyStrokeUnhookResponse{" +
                "status=" + status +
                ", msg='" + msg + '\'' +
                ", type=" + type +
                ", keypressList=" + keypressList +
                '}';
    }
}
