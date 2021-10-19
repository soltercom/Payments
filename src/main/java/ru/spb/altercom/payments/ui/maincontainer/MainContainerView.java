package ru.spb.altercom.payments.ui.maincontainer;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import ru.spb.altercom.payments.utils.Utils;

public class MainContainerView {

    @FXML
    private Pane summaryTableView;

    @FXML
    public void initialize() {
        Utils.anchorPane(summaryTableView);
    }

}
