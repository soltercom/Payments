package ru.spb.altercom.payments.ui.person;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import ru.spb.altercom.payments.utils.Notifications;
import ru.spb.altercom.payments.utils.Utils;

public class PersonFormView {

    @FXML
    private AnchorPane rootPane;

    @FXML
    private GridPane propertiesGrid;

    @FXML
    private CheckBox disable;

    @FXML
    private TextField id;

    @FXML
    private TextField name;

    @FXML
    private Button btnOK;

    @FXML
    private Button btnCancel;

    @FXML
    void onCancel(ActionEvent event) {
        Notifications.getInstance().publish(Notifications.PERSON_FORM_CLOSED);
    }

    @FXML
    void onOK(ActionEvent event) {
        if (viewModel.save()) {
            Notifications.getInstance().publish(Notifications.PERSON_FORM_CLOSED);
            Notifications.getInstance().publish(Notifications.SUMMARY_LIST_UPDATE);
        }
    }

    private final PersonFormViewModel viewModel = new PersonFormViewModel();

    @FXML
    public void initialize() {
        bindViewModel();
    }

    private void bindViewModel() {
        btnOK.disableProperty().bind(name.textProperty().isEmpty());

        Bindings.bindBidirectional(id.textProperty(), viewModel.idProperty(),
                Utils.UUIDStringConverter());

        name.textProperty().bindBidirectional(viewModel.nameProperty());
        disable.selectedProperty().bindBidirectional(viewModel.disableProperty());
    }
}
