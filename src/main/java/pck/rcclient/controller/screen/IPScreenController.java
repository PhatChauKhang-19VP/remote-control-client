package pck.rcclient.controller.screen;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import pck.rcclient.App;
import pck.rcclient.api.API;
import pck.rcclient.be.model.Server;

import java.net.URL;
import java.util.ResourceBundle;

public class IPScreenController implements Initializable {
    public TextField ipField;
    public Text ipWarningField;

    public TextField portField;
    public Text portWarningField;

    public Button connectButton;
    public Label statusAlert;
    public ImageView ivPortIcon;
    public ImageView ivIPIcon;

    protected
    String successMessage = "-fx-text-fill: GREEN;";
    String errorMessage = "-fx-text-fill: RED;";
    String errorStyle = "-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;";
    String successStyle = "-fx-border-color: #A9A9A9; -fx-border-width: 2; -fx-border-radius: 5;";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ipField.setText(Server.getInstance().getIP());
        portField.setText(String.valueOf(Server.getInstance().getPort()));
        ivIPIcon.setImage(new Image("static/icons/ip-address.png"));
        ivPortIcon.setImage(new Image("static/icons/ethernet.png"));

        ipField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches(".*\\s")) {
                newValue = oldValue;
                ipField.setText(newValue);
            } else {
                checkIPValid(newValue);
                statusAlert.setVisible(false);
            }
        });

        portField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches(".*\\s")) {
                newValue = oldValue;
                portField.setText(newValue);
            } else {
                checkPortValid(newValue);
                statusAlert.setVisible(false);
            }
        });

        ipWarningField.setWrappingWidth(250);
        portWarningField.setWrappingWidth(250);
    }

    public boolean checkIPValid(String ip) {
        if (ip.isBlank()) {
            ipField.setStyle(errorStyle);
            ipWarningField.setText("Please enter IP!");
            ipWarningField.setStyle(errorMessage);

            return false;
        }

        String regex = "(\\b25[0-5]|\\b2[0-4][0-9]|\\b[01]?[0-9][0-9]?)(\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}";

        if (!ip.matches(regex)) {
            ipField.setStyle(errorStyle);
            ipWarningField.setText("IP must match format (0-255).(0-255).(0.255).(0-255) !");
            ipWarningField.setStyle(errorMessage);

            return false;
        }

        ipField.setStyle(successStyle);
        ipWarningField.setText("");
        ipWarningField.setStyle(successMessage);

        ipWarningField.setWrappingWidth(250);
        return true;
    }

    public boolean checkPortValid(String port) {
        if (port.isBlank()) {
            portField.setStyle(errorStyle);
            portWarningField.setText("Please enter port number !");
            portWarningField.setStyle(errorMessage);

            return false;
        }

        String regex = "^((6553[0-5])|(655[0-2][0-9])|(65[0-4][0-9]{2})|(6[0-4][0-9]{3})|([1-5][0-9]{4})|([0-5]{0,5})|([0-9]{1,4}))$";

        if (!port.matches(regex)) {
            portField.setStyle(errorStyle);
            portWarningField.setText("Port must only contain 0-9 and in range from 0 to 65535 !");
            portWarningField.setStyle(errorMessage);

            return false;
        }

        portField.setStyle(successStyle);
        portWarningField.setText("");
        portWarningField.setStyle(successMessage);

        portWarningField.setWrappingWidth(250);

        return true;
    }

    public void onConnectButtonClicked(ActionEvent ae) {
        if (ae.getSource() == connectButton) {
            if (checkIPValid(ipField.getText()) && checkPortValid(portField.getText())) {
                Server.getInstance().setIP(ipField.getText());
                Server.getInstance().setPort(Integer.parseInt(portField.getText()));
                if (API.connectToServer()) {
                    App.gotoHomeScreen();
                } else {
                    statusAlert.setStyle(errorStyle);
                    statusAlert.setText("Cannot connect to Server !");
                    statusAlert.setStyle(errorMessage);
                    statusAlert.setVisible(true);
                    if (!checkIPValid("")) {
                        new animatefx.animation.Shake(ipField).play();
                    }

                    if (!checkPortValid("")) {
                        new animatefx.animation.Shake(portField).play();
                    }
                    System.out.println("Cannot connect to Server !");
                }
            }
        }

        if (!checkIPValid(ipField.getText())) {
            new animatefx.animation.Shake(ipField).play();
        }

        if (!checkPortValid(portField.getText())) {
            new animatefx.animation.Shake(portField).play();
        }
    }
}
