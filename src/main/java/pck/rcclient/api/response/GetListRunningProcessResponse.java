package pck.rcclient.api.response;

import pck.rcclient.api.request.REQUEST_TYPE;
import pck.rcclient.model.WinProcess;

import java.util.ArrayList;

public class GetListRunningProcessResponse extends BaseResponse {
    private ArrayList<WinProcess> winProcesses = new ArrayList<>();

    public GetListRunningProcessResponse(RESPONSE_STATUS status, String msg, REQUEST_TYPE type) {
        super(status, msg, type);
    }

    public GetListRunningProcessResponse(RESPONSE_STATUS status, String msg, REQUEST_TYPE type, ArrayList<WinProcess> winProcesses) {
        super(status, msg, type);
        this.winProcesses = winProcesses;
    }

    public ArrayList<WinProcess> getWinProcesses() {
        return winProcesses;
    }

    public void setWinProcesses(ArrayList<WinProcess> winProcesses) {
        this.winProcesses = winProcesses;
    }

    @Override
    public String toString() {
        return "GetListRunningProcessResponse{" +
                "winProcesses=" + winProcesses +
                '}';
    }
}
