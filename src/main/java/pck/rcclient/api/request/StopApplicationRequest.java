package pck.rcclient.api.request;

public class StopApplicationRequest extends BaseRequest {
    private String appName;

    public StopApplicationRequest(String nameApp) {
        super(REQUEST_TYPE.STOP_APP);
        this.appName = nameApp;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    @Override
    public String toString() {
        return "StopApplicationRequest{" +
                "type=" + type +
                ", appName='" + appName + '\'' +
                '}';
    }
}
