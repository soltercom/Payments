package ru.spb.altercom.payments;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.spb.altercom.payments.utils.ViewLoader;

import java.io.IOException;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(ViewLoader.load(ViewLoader.View.MAIN_CONTAINER));
        stage.setTitle("Payments");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
