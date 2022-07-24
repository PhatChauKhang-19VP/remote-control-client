package pck.rcclient.model;

public class WinApp extends WinProcess {
    public WinApp(String name, int pid, double memUsage) {
        super(name, pid, memUsage);
    }

    @Override
    public String toString() {
        return "WinApp{" +
                "name='" + name + '\'' +
                ", pid=" + pid +
                ", memUsage=" + memUsage +
                '}';
    }
}
