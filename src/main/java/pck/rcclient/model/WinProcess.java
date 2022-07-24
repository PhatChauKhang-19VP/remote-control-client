package pck.rcclient.model;

public class WinProcess {
    String name;
    int pid;
    double memUsage;

    public WinProcess(String name, int pid, double memUsage) {
        this.name = name;
        this.pid = pid;
        this.memUsage = memUsage;
    }

    public String getName() {
        return name;
    }

    public int getPid() {
        return pid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public void setMemUsage(double memUsage) {
        this.memUsage = memUsage;
    }

    public double getMemUsage() {
        return memUsage;
    }

    @Override
    public String toString() {
        return "WinProcess{" +
                "imageName='" + name + '\'' +
                ", pid=" + pid +
                ", memUsage=" + memUsage +
                '}';
    }
}
