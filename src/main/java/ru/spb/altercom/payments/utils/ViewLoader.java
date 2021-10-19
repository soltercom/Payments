package ru.spb.altercom.payments.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import ru.spb.altercom.payments.MainApp;

import java.io.IOException;

public class ViewLoader {

    public enum View {

        MAIN_CONTAINER("MainContainerView.fxml"),
        PERSON_FORM("PersonFormView.fxml"),
        DAY_FORM("DayFormView.fxml");

        // TODO: MAIN_CONTAINER -> MainContainerView.fxml transformation

        private final String path;

        View (String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }

    }

    public static Parent load(View view) {
        var loader = new FXMLLoader(MainApp.class.getClassLoader().getResource("view/" + view.getPath()));
        try {
            return loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


}
