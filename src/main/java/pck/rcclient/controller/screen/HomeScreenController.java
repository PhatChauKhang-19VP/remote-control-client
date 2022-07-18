package pck.rcclient.controller.screen;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import pck.rcclient.App;
import pck.rcclient.api.API;
import pck.rcclient.api.request.*;
import pck.rcclient.api.response.*;
import pck.rcclient.model.WinApp;
import pck.rcclient.model.WinProcess;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class HomeScreenController implements Initializable {
    public TabPane tabPane;

    //tab process
    public Tab tabWinProcess;
    public TextField tfProcessName;
    public Button btnStartProcess;
    public TextField tfPID;
    public Button btnKillProcess;
    public TableView<WinProcess> tvWinProcess;

    public TableColumn<Object, Object> colNoWinProcess;
    public TableColumn<WinProcess, String> colImageNameWinProcess;
    public TableColumn<WinProcess, Integer> colPIDWinProcess;
    public TableColumn<WinProcess, String> colSessionNameWinProcess;
    public TableColumn<WinProcess, Integer> colSessionIDWinProcess;
    public TableColumn<WinProcess, String> colMemUsageWinProcess;
    public TableColumn<WinProcess, String> colBtnWinProcess;

    // tab app
    public Tab tabWinApp;
    public TextField tfAppNameForStart;
    public Button btnStartApp;
    public TextField tfAppNameForStop;
    public Button btnStopApp;

    public TableView<WinApp> tvWinApp;
    public TableColumn<Object, Object> colNoWinApp;
    public TableColumn<WinApp, String> colImageNameWinApp;
    public TableColumn<WinApp, Integer> colPIDWinApp;
    public TableColumn<WinApp, String> colMemUsageWinApp;
    public TableColumn<WinApp, String> colPackageNameWinApp;
    public TableColumn<WinApp, String> colBtnWinApp;

    // tab keystroke
    public Tab tabKeyStroke;
    public Button btnKeyStroke;
    public Label lblHookNoti;
    public TextArea taHookedKeys;

    // tab screenshot
    public Tab tabScreenshot;
    public Button btnTakeScreenshot;
    public ImageView ivScreenshotImage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        API.connectToServer();

        setupTabPane();
        setupTableWinProcess();
        setupTableWinApp();
        setupImageView();
    }

    private void setupTabPane() {
        Platform.runLater(this::loadWinProcess);
        Platform.runLater(this::loadWinApps);
        tabPane.getSelectionModel().selectedItemProperty().addListener(
                (ov, oldTab, newTab) -> {
                    // System.out.println("Tab Selection changed from " + oldTab.getText() + " to " + newTab.getText());

                    if (newTab.equals(tabWinProcess)) {
                        System.out.println("switch to tab win process");

                        Platform.runLater(this::loadWinProcess);
                    } else if (newTab.equals(tabWinApp)) {
                        System.out.println("switch to tab win app");
                    } else if (newTab.equals(tabKeyStroke)) {
                        System.out.println("switch to tab keystroke");
                    } else if (newTab.equals(tabScreenshot)) {
                        System.out.println("switch to tab keystroke");
                    }

                }
        );
    }

    private void setupTableWinProcess() {
        //* setup for table win processes
        colNoWinProcess.setCellFactory(new LineNumbersCellFactory<>());
        colImageNameWinProcess.setCellValueFactory(new PropertyValueFactory<>("imageName"));
        colPIDWinProcess.setCellValueFactory(new PropertyValueFactory<>("pid"));
        colSessionNameWinProcess.setCellValueFactory(new PropertyValueFactory<>("sessionName"));
        colSessionIDWinProcess.setCellValueFactory(new PropertyValueFactory<>("sessionId"));
        colMemUsageWinProcess.setCellValueFactory(new PropertyValueFactory<>("memUsage"));

        //col btn for ```colBtnWinProcess```
        Callback<TableColumn<WinProcess, String>, TableCell<WinProcess, String>> cellFactoryBtnWinProcess = new Callback<>() {
            @Override
            public TableCell<WinProcess, String> call(final TableColumn<WinProcess, String> param) {
                return new TableCell<>() {

                    final Button btn = new Button("Kill");

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.setOnAction(event -> {
                                WinProcess winProcess = getTableView().getItems().get(getIndex());
                                System.out.println(winProcess);

                                stopProcess(winProcess.getPid());
                            });
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
            }
        };
        colBtnWinProcess.setCellFactory(cellFactoryBtnWinProcess);
    }

    private void setupTableWinApp() {
        //* setup for table win processes
        colNoWinApp.setCellFactory(new LineNumbersCellFactory<>());
        colImageNameWinApp.setCellValueFactory(new PropertyValueFactory<>("imageName"));
        colPIDWinApp.setCellValueFactory(new PropertyValueFactory<>("pid"));
        colMemUsageWinApp.setCellValueFactory(new PropertyValueFactory<>("memUsage"));
        colPackageNameWinApp.setCellValueFactory(new PropertyValueFactory<>("packageName"));

        //col btn for ```colBtnWinProcess```
        Callback<TableColumn<WinApp, String>, TableCell<WinApp, String>> cellFactoryBtnWinApp = new Callback<>() {
            @Override
            public TableCell<WinApp, String> call(final TableColumn<WinApp, String> param) {
                return new TableCell<>() {

                    final Button btn = new Button("Kill");

                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.setOnAction(event -> {
                                WinApp winApp = getTableView().getItems().get(getIndex());
                                System.out.println(winApp);

                                stopApp(winApp.getImageName().split("\s+")[0]);
                            });
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
            }
        };
        colBtnWinApp.setCellFactory(cellFactoryBtnWinApp);
    }

    /* ---------- PROCESS START ---------- */
    private void loadWinProcess() {
        if (API.sendReq(new Request(REQUEST_TYPE.GET_LIST_RUNNING_PROCESS))) {
            BaseResponse baseRes = API.rcvRes();
            if (baseRes == null || baseRes.getType() != REQUEST_TYPE.GET_LIST_RUNNING_PROCESS) {
                App.onError("Receive response GET_LIST_RUNNING_PROCESS failed");
            } else {
                GetListRunningProcessResponse res = (GetListRunningProcessResponse) baseRes;
                tvWinProcess.getItems().clear();
                tvWinProcess.getItems().addAll(res.getWinProcesses());
            }
        } else {
            App.onError("Send GET_LIST_RUNNING_PROCESS failed");
        }
    }

    /* ---------- APPLICATION START ---------- */
    private void loadWinApps() {
        if (API.sendReq(new Request(REQUEST_TYPE.GET_LIST_RUNNING_APP))) {
            BaseResponse baseRes = API.rcvRes();
            if (baseRes == null || baseRes.getType() != REQUEST_TYPE.GET_LIST_RUNNING_APP) {
                App.onError("Receive response GET_LIST_RUNNING_APP failed");
            } else {
                GetListRunningAppResponse res = (GetListRunningAppResponse) baseRes;
                tvWinApp.getItems().clear();
                tvWinApp.getItems().addAll(res.getWinApps());
            }
        } else {
            App.onError("Send GET_LIST_RUNNING_APP failed");
        }
    }

    private void stopProcess(int pid) {
        if (API.sendReq(new StopProcessRequest(pid))) {
            BaseResponse baseRes = API.rcvRes();
            System.out.println(baseRes);
            if (baseRes == null || baseRes.getType() != REQUEST_TYPE.STOP_PROCESS) {
                App.onError("Receive response STOP_PROCESS failed");
            } else {
                Response res = (Response) baseRes;

                if (res.getStatus() == RESPONSE_STATUS.SUCCESS) {
                    Platform.runLater(this::loadWinProcess);
                    App.onNoti(res.getMsg());
                } else {
                    App.onError(res.getMsg());
                }
            }
        } else {
            App.onError("Send STOP_PROCESS failed");
        }
    }

    private void stopApp(String appName) {
        if (API.sendReq(new StopApplicationRequest(appName))) {
            BaseResponse baseRes = API.rcvRes();
            if (baseRes == null || baseRes.getType() != REQUEST_TYPE.STOP_APP) {
                App.onError("Receive response STOP_APP failed");
            } else {
                Response res = (Response) baseRes;

                if (res.getStatus() == RESPONSE_STATUS.SUCCESS) {
                    Platform.runLater(this::loadWinApps);
                    App.onNoti(res.getMsg());
                } else {
                    App.onError(res.getMsg());
                }
            }
        } else {
            App.onError("Send STOP_APP failed");
        }
    }

    public void onBtnStartProcessClick(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnStartProcess) {
            startProcess(tfProcessName.getText());
        }
    }
    /* ---------- PROCESS END ---------- */

    private void startProcess(String processName) {
        if (API.sendReq(new StartProcessRequest(processName))) {
            BaseResponse baseRes = API.rcvRes();
            if (baseRes == null || baseRes.getType() != REQUEST_TYPE.START_PROCESS) {
                App.onError("Receive response START_PROCESS failed");
            } else {
                Response res = (Response) baseRes;

                if (res.getStatus() == RESPONSE_STATUS.SUCCESS) {
                    Platform.runLater(this::loadWinProcess);
                    App.onNoti(res.getMsg());
                } else {
                    App.onError(res.getMsg());
                }
            }
        } else {
            App.onError("Send START_PROCESS failed");
        }
    }

    public void onBtnKillProcessClick(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnKillProcess) {
            try {
                stopProcess(Integer.parseInt(tfPID.getText()));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }

    public void onBtnStartAppClick(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnStartApp) {
            startApp(tfAppNameForStart.getText());
        }
    }

    private void startApp(String appName) {
        if (API.sendReq(new StartApplicationRequest(appName))) {
            BaseResponse baseRes = API.rcvRes();
            if (baseRes == null || baseRes.getType() != REQUEST_TYPE.START_APP) {
                App.onError("Receive response START_APP failed");
            } else {
                Response res = (Response) baseRes;

                if (res.getStatus() == RESPONSE_STATUS.SUCCESS) {
                    Platform.runLater(this::loadWinApps);
                    App.onNoti(res.getMsg());
                } else {
                    App.onError(res.getMsg());
                }
            }
        } else {
            App.onError("Send START_APP failed");
        }
    }

    public void onBtnStopAppClick(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnStopApp) {
            stopApp(tfAppNameForStop.getText());
        }
    }

    /* ---------- APPLICATION END ---------- */

    /* ---------- KEYSTROKE START ---------- */
    public void onBtnKeyStrokeClick(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnKeyStroke) {
            if (btnKeyStroke.getText().equals("Hook")) {
                if (API.sendReq(new Request(REQUEST_TYPE.KEYSTROKE_HOOK))) {
                    BaseResponse baseRes = API.rcvRes();
                    if (baseRes == null || baseRes.getType() != REQUEST_TYPE.KEYSTROKE_HOOK) {
                        App.onError("Receive response KEYSTROKE_HOOK failed");
                    } else {
                        Response res = (Response) baseRes;

                        if (res.getStatus() == RESPONSE_STATUS.SUCCESS) {
                            btnKeyStroke.setText("Unhook");
                        } else {
                            App.onError(res.getMsg());
                        }
                    }
                } else {
                    App.onError("Send KEYSTROKE_HOOK failed");
                }
            } else {
                if (API.sendReq(new Request(REQUEST_TYPE.KEYSTROKE_UNHOOK))) {
                    BaseResponse baseRes = API.rcvRes();
                    if (baseRes == null || baseRes.getType() != REQUEST_TYPE.KEYSTROKE_UNHOOK) {
                        App.onError("Receive response KEYSTROKE_UNHOOK failed");
                    } else {
                        KeyStrokeUnhookResponse res = (KeyStrokeUnhookResponse) baseRes;

                        if (res.getStatus() == RESPONSE_STATUS.SUCCESS) {
                            StringBuilder str = new StringBuilder();
                            for (int keycode : res.getKeypressList()) {
                                char c = (char) keycode;
                                str.append(c);
                            }

                            taHookedKeys.setText(str.toString());
                            btnKeyStroke.setText("Hook");
                        } else {
                            App.onError(res.getMsg());
                        }
                    }
                } else {
                    App.onError("Send KEYSTROKE_UNHOOK failed");
                }
            }
        }
    }

    /* ---------- KEYSTROKE END ---------- */


    /* ---------- SCREENSHOT START ---------- */
    private void setupImageView(){
        ivScreenshotImage.imageProperty().addListener((observableValue, image, t1) -> {
            System.out.println("ivImage.imageProperty is trigger");
            Platform.runLater(() -> {
                Image img = ivScreenshotImage.getImage();
                if (img != null) {
                    double w = 0;
                    double h = 0;

                    double ratioX = ivScreenshotImage.getFitWidth() / img.getWidth();
                    double ratioY = ivScreenshotImage.getFitHeight() / img.getHeight();

                    double reducCoeff = Math.min(ratioX, ratioY);

                    w = img.getWidth() * reducCoeff;
                    h = img.getHeight() * reducCoeff;

                    ivScreenshotImage.setX((ivScreenshotImage.getFitWidth() - w) / 2);
                    ivScreenshotImage.setY((ivScreenshotImage.getFitHeight() - h) / 2);
                }
            });
        });
    }

    public void onBtnTakeScreenshotClick(ActionEvent actionEvent){
        if (actionEvent.getSource() == btnTakeScreenshot){
            if (API.sendReq(new Request(REQUEST_TYPE.TAKE_SCREENSHOT))) {
                BaseResponse baseRes = API.rcvRes();
                if (baseRes == null || baseRes.getType() != REQUEST_TYPE.TAKE_SCREENSHOT) {
                    App.onError("Receive response TAKE_SCREENSHOT failed");
                } else {
                    TakeScreenshotResponse res = (TakeScreenshotResponse) baseRes;

                    if (res.getStatus() == RESPONSE_STATUS.SUCCESS) {
                        Image img = new Image(new ByteArrayInputStream(res.getBuffer()));

                        ivScreenshotImage.setImage(img);
                    } else {
                        App.onError(res.getMsg());
                    }
                }
            } else {
                App.onError("Send TAKE_SCREENSHOT failed");
            }
        }
    }
    /* ---------- SCREENSHOT END ---------- */

    static class LineNumbersCellFactory<T, E> implements Callback<TableColumn<T, E>, TableCell<T, E>> {

        public LineNumbersCellFactory() {
        }

        @Override
        public TableCell<T, E> call(TableColumn<T, E> param) {
            return new TableCell<T, E>() {
                @Override
                protected void updateItem(E item, boolean empty) {
                    super.updateItem(item, empty);

                    if (!empty) {
                        setText(this.getTableRow().getIndex() + 1 + "");
                    } else {
                        setText("");
                    }
                }
            };
        }
    }
}
