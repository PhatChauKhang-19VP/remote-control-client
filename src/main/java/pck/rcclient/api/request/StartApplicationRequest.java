package pck.rcclient.api.request;

public class StartApplicationRequest extends BaseRequest {
    private String appName;

    public StartApplicationRequest(String nameApp) {
        super(REQUEST_TYPE.START_APP);
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
        return "StartApplicationRequest{" +
                "type=" + type +
                ", nameApp='" + appName + '\'' +
                '}';
    }
}
