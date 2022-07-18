package pck.rcclient.api.request;

public class StartProcessRequest extends BaseRequest {
    private String processName;

    public StartProcessRequest(String nameProcess) {
        super(REQUEST_TYPE.START_PROCESS);
        this.processName = nameProcess;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    @Override
    public String toString() {
        return "StartProcessRequest{" +
                "processName='" + processName + '\'' +
                '}';
    }
}


