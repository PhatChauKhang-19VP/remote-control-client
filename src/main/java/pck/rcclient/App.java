package pck.rcclient;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import static javafx.application.Platform.exit;

public class App extends Application {
    public static Stage stage = null;

    public static FXMLLoader loader = null;

    public static Stage getStage() {
        return stage;
    }

    public static FXMLLoader getLoader() {
        return loader;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Parent replaceSceneContent(String fxml, int width, int height) throws Exception {
        FXMLLoader loader = new FXMLLoader(App.class.getResource(fxml), null, new JavaFXBuilderFactory());

        Parent page = loader.load();

        App.loader = loader;

        Scene scene = new Scene(page, width, height);
        stage.setScene(scene);
        stage.setTitle("Remote control client");
        stage.getIcons().clear();
        stage.getIcons().add(new Image("static/images/PCK-logo.png"));
        stage.sizeToScene();
        stage.show();
        return page;
    }

    @Override
    public void start(Stage primaryStage) {
        Platform.runLater(() -> {
            stage = primaryStage;
            try {
                stage.setTitle("Remote control client");
                stage.setResizable(false);
                stage.setFullScreen(false);

                gotoIPScreen();
            } catch (Exception e) {
                e.printStackTrace();
                onError(e.getMessage());
                exit();
            }
        });
    }

    public static void gotoIPScreen(){
        try {
            replaceSceneContent("screen/IP.fxml", 600, 600);
        } catch (Exception e) {
            e.printStackTrace();
            onError(e.getMessage());
        }
    }

    public static void gotoHomeScreen(){
        try {
            replaceSceneContent("screen/Home.fxml", 800, 600);
        } catch (Exception e) {
            e.printStackTrace();
            onError(e.getMessage());
        }
    }

    public static void onNoti(String noti){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notification");
        alert.setHeaderText(noti);
        alert.showAndWait();
    }

    public static void onError(String errMsg){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(errMsg);
        alert.showAndWait();
    }
}
