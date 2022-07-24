package pck.rcclient.api;

import pck.rcclient.api.request.*;
import pck.rcclient.api.response.*;
import pck.rcclient.be.model.Server;
import pck.rcclient.model.WinApp;
import pck.rcclient.model.WinProcess;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;

public class API {
    private static final Server server = Server.getInstance();
    static DataOutputStream dataOut = null;
    static DataInputStream dataIn = null;

    public static void main(String[] args) {
        connectToServer();

        // sendReq(new Request(REQUEST_TYPE.GET_LIST_RUNNING_APP));
        // System.out.println(rcvRes());

        sendReq(new StartApplicationRequest("notepad.exe"));
    }

    public static boolean connectToServer() {
        boolean createConnRes = server.createConnection();

        if (createConnRes) {
            dataIn = Server.getInstance().getDataIn();
            dataOut = Server.getInstance().getDataOut();

            if (sendReq(new Request(REQUEST_TYPE.TEST_CONNECTION))) {
                BaseResponse res = rcvRes();

                assert res != null;
                return res.getType() == REQUEST_TYPE.TEST_CONNECTION && res.getStatus() == RESPONSE_STATUS.SUCCESS;
            }

        }

        return false;
    }

    public static boolean sendReq(BaseRequest request) {
        try {
            if (server == null) {
                System.out.println("CANNOT CONNECT TO SERVER !!! APPLICATION WILL EXIT");
                return false;
            }

            REQUEST_TYPE reqType = request.getType();

            switch (reqType) {
                case TEST_CONNECTION, GET_LIST_RUNNING_PROCESS, GET_LIST_RUNNING_APP, TAKE_SCREENSHOT, KEYSTROKE_HOOK, KEYSTROKE_UNHOOK, SHUTDOWN -> {
                    Request req = (Request) request;

                    dataOut.writeUTF(req.getType().name());

                    return true;
                }
                case START_PROCESS -> {
                    StartProcessRequest req = (StartProcessRequest) request;

                    dataOut.writeUTF(req.getType().name());

                    dataOut.writeUTF(req.getProcessName());

                    return true;
                }
                case STOP_PROCESS -> {
                    StopProcessRequest req = (StopProcessRequest) request;

                    dataOut.writeUTF(req.getType().name());

                    dataOut.writeInt(req.getPid());

                    return true;
                }
                case START_APP -> {
                    StartApplicationRequest req = (StartApplicationRequest) request;

                    dataOut.writeUTF(req.getType().name());

                    dataOut.writeUTF(req.getAppName());

                    return true;
                }
                case STOP_APP -> {
                    StopApplicationRequest req = (StopApplicationRequest) request;

                    dataOut.writeUTF(req.getType().name());

                    dataOut.writeInt(req.getPid());

                    return true;
                }
                default -> {
                    return false;
                }
            }

        } catch (Exception e) {
            return false;
        }
    }

    public static BaseResponse rcvRes() {
        try {
            REQUEST_TYPE requestType = REQUEST_TYPE.valueOf(dataIn.readUTF());
            switch (requestType) {

                case TEST_CONNECTION, START_PROCESS, STOP_PROCESS, START_APP, STOP_APP, KEYSTROKE_HOOK, SHUTDOWN -> {

                    RESPONSE_STATUS responseStatus = RESPONSE_STATUS.valueOf(dataIn.readUTF());
                    String msg = dataIn.readUTF();

                    return new Response(requestType, responseStatus, msg);
                }
                case GET_LIST_RUNNING_PROCESS -> {
                    RESPONSE_STATUS responseStatus = RESPONSE_STATUS.valueOf(dataIn.readUTF());
                    String msg = dataIn.readUTF();

                    // number processes
                    int numberProcess = dataIn.readInt();
                    ArrayList<WinProcess> winProcesses = new ArrayList<>();
                    for (int i = 0; i < numberProcess; i++) {
                        String name = dataIn.readUTF();
                        int pid = dataIn.readInt();
                        double memUsage = dataIn.readDouble();

                        winProcesses.add(new WinProcess(name, pid, memUsage));
                    }

                    return new GetListRunningProcessResponse(responseStatus, msg, requestType, winProcesses);
                }
                case GET_LIST_RUNNING_APP -> {
                    RESPONSE_STATUS responseStatus = RESPONSE_STATUS.valueOf(dataIn.readUTF());
                    String msg = dataIn.readUTF();

                    // number apps
                    int numberApps = dataIn.readInt();
                    ArrayList<WinApp> winApps = new ArrayList<>();
                    for (int i = 0; i < numberApps; i++) {
                        String name = dataIn.readUTF();
                        int pid = dataIn.readInt();
                        double memUsage = dataIn.readDouble();

                        winApps.add(new WinApp(name, pid, memUsage));
                    }

                    return new GetListRunningAppResponse(responseStatus, msg, requestType, winApps);
                }
                case TAKE_SCREENSHOT -> {
                    RESPONSE_STATUS responseStatus = RESPONSE_STATUS.valueOf(dataIn.readUTF());
                    String msg = dataIn.readUTF();

                    // get file content
                    int length = dataIn.readInt();
                    byte[] buffer = null;
                    if (length > 0) {
                        buffer = new byte[length];
                        dataIn.readFully(buffer, 0, buffer.length);
                    }

                    return new TakeScreenshotResponse(responseStatus, msg, requestType, buffer);
                }
                case KEYSTROKE_UNHOOK -> {
                    RESPONSE_STATUS responseStatus = RESPONSE_STATUS.valueOf(dataIn.readUTF());
                    String msg = dataIn.readUTF();

                    int size = dataIn.readInt();
                    ArrayList<Integer> keycodes = new ArrayList<>();
                    for (int i = 0; i < size; i++) {
                        keycodes.add(dataIn.readInt());
                    }

                    return new KeyStrokeUnhookResponse(responseStatus, msg, requestType, keycodes);
                } default -> {
                    return null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
