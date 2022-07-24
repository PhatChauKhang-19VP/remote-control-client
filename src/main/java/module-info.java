module pck.rcclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires AnimateFX;
    requires org.apache.commons.io;

    opens pck.rcclient.controller.screen to javafx.fxml;
    exports pck.rcclient.controller.screen;

    exports pck.rcclient.model;
    exports pck.rcclient;
}