package pck.rcclient.model;

public class WinProcess {
    String imageName;
    int pid;
    String sessionName;
    int sessionId;
    String memUsage;

    public WinProcess(String imageName, int pid, String sessionName, int sessionId, String memUsage) {
        this.imageName = imageName;
        this.pid = pid;
        this.sessionName = sessionName;
        this.sessionId = sessionId;
        this.memUsage = memUsage;
    }

    public String getImageName() {
        return imageName;
    }

    public int getPid() {
        return pid;
    }

    public String getSessionName() {
        return sessionName;
    }

    public int getSessionId() {
        return sessionId;
    }

    public String getMemUsage() {
        return memUsage;
    }

    @Override
    public String toString() {
        return "Process{" +
                "imageName='" + imageName + '\'' +
                ", pid=" + pid +
                ", sessionName='" + sessionName + '\'' +
                ", sessionId=" + sessionId +
                ", memUsage=" + memUsage +
                '}';
    }
}
