module pck.rcclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires AnimateFX;

    opens pck.rcclient.controller.screen to javafx.fxml;
    exports pck.rcclient.controller.screen;

    exports pck.rcclient.model;
    exports pck.rcclient;
}