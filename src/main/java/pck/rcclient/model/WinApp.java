package pck.rcclient.model;

public class WinApp {
        String imageName;
        int pid;
        String memUsage;
        String packageName;

        public WinApp(String imageName, int pid, String memUsage, String packageName) {
                this.imageName = imageName;
                this.pid = pid;
                this.memUsage = memUsage;
                this.packageName = packageName;
        }

        public String getImageName() {
                return imageName;
        }

        public void setImageName(String imageName) {
                this.imageName = imageName;
        }

        public int getPid() {
                return pid;
        }

        public void setPid(int pid) {
                this.pid = pid;
        }

        public String getMemUsage() {
                return memUsage;
        }

        public void setMemUsage(String memUsage) {
                this.memUsage = memUsage;
        }

        public String getPackageName() {
                return packageName;
        }

        public void setPackageName(String packageName) {
                this.packageName = packageName;
        }

        @Override
        public String toString() {
                return "WinApp{" +
                        "imageName='" + imageName + '\'' +
                        ", pid=" + pid +
                        ", memUsage='" + memUsage + '\'' +
                        ", packageName='" + packageName + '\'' +
                        '}';
        }
}
