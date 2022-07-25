package pck.rcclient.controller.screen;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import org.apache.commons.io.FileUtils;
import pck.rcclient.App;
import pck.rcclient.api.API;
import pck.rcclient.api.request.*;
import pck.rcclient.api.response.*;
import pck.rcclient.model.WinApp;
import pck.rcclient.model.WinProcess;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class HomeScreenController implements Initializable {
    public TabPane tabPane;

    //tab process
    public Tab tabWinProcess;
    public TextField tfProcessName;
    public Button btnStartProcess;
    public TextField tfPID;
    public Button btnKillProcess;
    public Button btnShutdown;

    public TableView<WinProcess> tvWinProcess;
    public TableColumn<Object, Object> colProcessNo;
    public TableColumn<WinProcess, String> colProcessName;
    public TableColumn<WinProcess, Integer> colProcessPID;
    public TableColumn<WinProcess, Double> colProcessMemUsage;
    public TableColumn<WinProcess, String> colProcessBtn;

    // tab app
    public Tab tabWinApp;
    public TextField tfAppNameForStart;
    public Button btnStartApp;
    public TextField tfAppNameForStop;
    public Button btnStopApp;

    public TableView<WinApp> tvWinApp;
    public TableColumn<Object, Object> colAppNo;
    public TableColumn<WinApp, String> colAppName;
    public TableColumn<WinApp, Integer> colAppPID;
    public TableColumn<WinApp, Double> colAppMemUsage;
    public TableColumn<WinApp, String> colAppBtn;

    // tab keystroke
    public Tab tabKeyStroke;
    public Button btnKeyStroke;
    public Label lblHookNoti;
    public TextArea taHookedKeys;

    // tab screenshot
    public Tab tabScreenshot;
    public Button btnTakeScreenshot;
    public ImageView ivScreenshotImage;
    public Button btnDownload;
    public byte[] buffer = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setupTabPane();
        setupTableWinProcess();
        setupTableWinApp();
        setupImageView();
    }

    private void setupTabPane() {
        Platform.runLater(this::loadWinProcess);
        Platform.runLater(this::loadWinApps);
        tabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            // System.out.println("Tab Selection changed from " + oldTab.getText() + " to " + newTab.getText());

            if (newTab.equals(tabWinProcess)) {
                System.out.println("switch to tab win process");

                Platform.runLater(this::loadWinProcess);
            } else if (newTab.equals(tabWinApp)) {
                System.out.println("switch to tab win app");

                Platform.runLater(this::loadWinApps);
            } else if (newTab.equals(tabKeyStroke)) {
                System.out.println("switch to tab keystroke");
            } else if (newTab.equals(tabScreenshot)) {
                System.out.println("switch to tab keystroke");
            }

        });
    }

    /* ---------- PROCESS START ---------- */
    private void setupTableWinProcess() {
        //* setup for table win processes
        colProcessNo.setCellFactory(new LineNumbersCellFactory<>());
        colProcessName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colProcessPID.setCellValueFactory(new PropertyValueFactory<>("pid"));
        colProcessMemUsage.setCellValueFactory(new PropertyValueFactory<>("memUsage"));

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
        colProcessBtn.setCellFactory(cellFactoryBtnWinProcess);
    }

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

    public void onBtnStartProcessClick(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnStartProcess) {
            startProcess(tfProcessName.getText());
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

    public void onBtnShutdownClick(ActionEvent actionEvent) {
        if(actionEvent.getSource() == btnShutdown) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Cảnh báo!");
            alert.setHeaderText("Tắt máy tính");
            alert.setContentText("Bạn có chắc muốn tắt máy tính phía remote?");

            // option != null.
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get() == ButtonType.OK) {
                if (API.sendReq(new Request(REQUEST_TYPE.SHUTDOWN))) {
                    BaseResponse baseRes = API.rcvRes();
                    System.out.println(baseRes);
                    if (baseRes == null || baseRes.getType() != REQUEST_TYPE.SHUTDOWN) {
                        App.onError("Receive response SHUTDOWN failed");
                    } else {
                        Response res = (Response) baseRes;

                        if (res.getStatus() == RESPONSE_STATUS.SUCCESS) {
                            App.onNoti(res.getMsg());
                        } else {
                            App.onError(res.getMsg());
                        }
                    }
                } else {
                    App.onError("Send SHUTDOWN failed");
                }
            }
        }
    }
    /* ---------- PROCESS END ---------- */

    /* ---------- APPLICATION START ---------- */
    private void setupTableWinApp() {
        //* setup for table win processes
        colAppNo.setCellFactory(new LineNumbersCellFactory<>());
        colAppName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAppPID.setCellValueFactory(new PropertyValueFactory<>("pid"));
        colAppMemUsage.setCellValueFactory(new PropertyValueFactory<>("memUsage"));

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

                                stopApp(winApp.getPid());
                            });
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
            }
        };
        colAppBtn.setCellFactory(cellFactoryBtnWinApp);
    }

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

    private void stopApp(int pid) {
        if (API.sendReq(new StopApplicationRequest(pid))) {
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

    public void onBtnStartAppClick(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnStartApp) {
            startApp(tfAppNameForStart.getText());
        }
    }

    public void onBtnStopAppClick(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnStopApp) {
            stopApp(Integer.parseInt(tfAppNameForStop.getText()));
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
                            lblHookNoti.setVisible(true);
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
                            lblHookNoti.setVisible(false);
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
    private void setupImageView() {
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

    public void onBtnTakeScreenshotClick(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnTakeScreenshot) {
            if (API.sendReq(new Request(REQUEST_TYPE.TAKE_SCREENSHOT))) {
                BaseResponse baseRes = API.rcvRes();
                if (baseRes == null || baseRes.getType() != REQUEST_TYPE.TAKE_SCREENSHOT) {
                    App.onError("Receive response TAKE_SCREENSHOT failed");
                } else {
                    TakeScreenshotResponse res = (TakeScreenshotResponse) baseRes;

                    if (res.getStatus() == RESPONSE_STATUS.SUCCESS) {
                        buffer = res.getBuffer();
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

    public void onBtnDownloadClick(ActionEvent actionEvent) {
        if (actionEvent.getSource() == btnDownload) {
            FileChooser fileChooser = new FileChooser();

            String param2 = "*.png";
            String param1 = "PNG file";

            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(param1, param2);
            fileChooser.getExtensionFilters().add(extFilter);

            //Opening a dialog box
            File file = fileChooser.showSaveDialog(App.getStage());

            if (file != null) {
                try {
                    FileUtils.copyInputStreamToFile(new ByteArrayInputStream(buffer), file);
                } catch (IOException e) {
                    e.printStackTrace();
                    App.onError(e.getMessage());
                }

                System.out.println(file.getName() + " , saved to " + file.getAbsolutePath());

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Tải xuống thành công");
                alert.setHeaderText(file.getName() + " lưu tại " + file.getAbsolutePath());
                alert.showAndWait();
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
