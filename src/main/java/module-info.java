module pck.rcclient {
    requires javafx.controls;
    requires javafx.fxml;


    opens pck.rcclient to javafx.fxml;
    exports pck.rcclient;
}