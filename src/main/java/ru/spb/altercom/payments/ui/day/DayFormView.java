package ru.spb.altercom.payments.ui.day;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ru.spb.altercom.payments.ui.person.PersonStringConverter;
import ru.spb.altercom.payments.utils.Notifications;
import ru.spb.altercom.payments.utils.Utils;

public class DayFormView {

    @FXML
    private Label labelPerson;

    @FXML
    private Label labelDate;

    @FXML
    private TextField chargeField;

    @FXML
    private TextField paymentField;

    @FXML
    void onCancel(ActionEvent event) {
        Notifications.getInstance().publish(Notifications.DAY_FORM_CLOSED);
    }

    @FXML
    void onOK(ActionEvent event) {
        if (viewModel.save()) {
            Notifications.getInstance().publish(Notifications.DAY_FORM_CLOSED);
            Notifications.getInstance().publish(Notifications.SUMMARY_LIST_UPDATE);
        }
    }

    private final DayFormViewModel viewModel = new DayFormViewModel();

    @FXML
    public void initialize() {
        initUI();
        bindViewModel();
    }

    private void initUI() {
        // TODO: more correct text formatter for money
        chargeField.setTextFormatter(Utils.getMoneyTextFormatter());
        paymentField.setTextFormatter(Utils.getMoneyTextFormatter());
    }

    private void bindViewModel() {
        Bindings.bindBidirectional(labelPerson.textProperty(),
                viewModel.personProperty(),
                new PersonStringConverter());

        Bindings.bindBidirectional(labelDate.textProperty(),
                viewModel.dateProperty(),
                Utils.LocalDateStringConverter());

        Bindings.bindBidirectional(chargeField.textProperty(),
                viewModel.chargeProperty(),
                Utils.BigDecimalStringConverter("0.00"));

        Bindings.bindBidirectional(paymentField.textProperty(),
                viewModel.paymentProperty(),
                Utils.BigDecimalStringConverter("0.00"));
    }

}
