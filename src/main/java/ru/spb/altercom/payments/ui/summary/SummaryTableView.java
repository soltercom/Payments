package ru.spb.altercom.payments.ui.summary;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.spb.altercom.payments.model.Person;
import ru.spb.altercom.payments.model.Summary;
import ru.spb.altercom.payments.utils.Cache;
import ru.spb.altercom.payments.utils.Notifications;
import ru.spb.altercom.payments.utils.Utils;
import ru.spb.altercom.payments.utils.ViewLoader;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SummaryTableView {

    @FXML private TableView<SummaryTableRowModel> summaryTable;
    @FXML private TableColumn<SummaryTableRowModel, Person> personColumn;
    @FXML private TableColumn<SummaryTableRowModel, BigDecimal> openingBalanceColumn;
    @FXML private TableColumn<SummaryTableRowModel, Summary.DayTurnover> day1Column;
    @FXML private TableColumn<SummaryTableRowModel, Summary.DayTurnover> day2Column;
    @FXML private TableColumn<SummaryTableRowModel, Summary.DayTurnover> day3Column;
    @FXML private TableColumn<SummaryTableRowModel, Summary.DayTurnover> day4Column;
    @FXML private TableColumn<SummaryTableRowModel, Summary.DayTurnover> day5Column;
    @FXML private TableColumn<SummaryTableRowModel, Summary.DayTurnover> day6Column;
    @FXML private TableColumn<SummaryTableRowModel, Summary.DayTurnover> day7Column;
    @FXML private TableColumn<SummaryTableRowModel, BigDecimal> closingBalanceColumn;

    private List<TableColumn<SummaryTableRowModel, Summary.DayTurnover>> dayColumns;

    @FXML
    void onAddPerson(ActionEvent event) {
        selectedColumnIndex = summaryTable.getColumns().indexOf(personColumn);
        openPersonStage(null);
    }

    @FXML
    void onMouseClicked(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY) &&
            event.getClickCount() > 1) {

            setSelectedColumnIndex();
            var selectedColumn = summaryTable.getColumns().get(selectedColumnIndex);

            var person = summaryTable.getSelectionModel().getSelectedItem().getPerson();

            if (selectedColumn.equals(personColumn)) {
                openPersonStage(person);
            } else if (selectedColumn.getId().matches("day[1-7]Column")) {
                openDayStage(person, viewModel.getDateByColumnId(selectedColumn.getId()));
            }
        }
    }

    @FXML
    void onNextWeek(ActionEvent event) {
        viewModel.nextWeek();
        updateDayColumns();
    }

    @FXML
    void onPrevWeek(ActionEvent event) {
        viewModel.prevWeek();
        updateDayColumns();
    }

    @FXML
    public void initialize() {

        dayColumns = new ArrayList<>();
        dayColumns.add(day1Column);
        dayColumns.add(day2Column);
        dayColumns.add(day3Column);
        dayColumns.add(day4Column);
        dayColumns.add(day5Column);
        dayColumns.add(day6Column);
        dayColumns.add(day7Column);

        for (var column: dayColumns) {
            var columnNum = viewModel.getDayColumnNumber(column.getId());
            column.setCellValueFactory(new PropertyValueFactory<>("day" + columnNum));
            column.setCellFactory(Utils::dayCellFactory);
        }
        updateDayColumns();

        personColumn.setCellValueFactory(new PropertyValueFactory<>("person"));
        personColumn.setCellFactory(Utils::personCellFactory);
        openingBalanceColumn.setCellValueFactory(new PropertyValueFactory<>("openingBalance"));
        openingBalanceColumn.setCellFactory(Utils::balanceCellFactory);
        closingBalanceColumn.setCellValueFactory(new PropertyValueFactory<>("closingBalance"));
        closingBalanceColumn.setCellFactory(Utils::balanceCellFactory);

        summaryTable.setItems(viewModel.getSummaryList());

        summaryTable.getSelectionModel().setCellSelectionEnabled(true);

        viewModel.selectedTableRowProperty().bind(summaryTable.getSelectionModel().selectedItemProperty());
        viewModel.setOnSelect(vm -> summaryTable.getSelectionModel().select(vm));

        Notifications.getInstance().subscribe(Notifications.SUMMARY_LIST_UPDATE, this, this::onUpdateSummaryList);
    }

    private Stage personStage;
    private Stage dayStage;

    private int selectedColumnIndex = -1;

    private final SummaryTableViewModel viewModel = new SummaryTableViewModel();

    private void updateDayColumns() {
        var days = viewModel.getDays();
        for (int i = 0; i < dayColumns.size(); i++) {
            dayColumns.get(i).setText(days[i]);
        }
    }

    private void setSelectedColumnIndex() {
        var column= summaryTable.getSelectionModel().getSelectedCells()
                .stream().map(TablePosition::getColumn).findFirst();
        if (column.isEmpty()) {
            selectedColumnIndex = -1;
        } else {
            selectedColumnIndex = column.get();
        }
    }

    private void onUpdateSummaryList(String str) {
        if (selectedColumnIndex > -1) {
            summaryTable.getSelectionModel()
                .select(summaryTable.getSelectionModel().getSelectedIndex(),
                        summaryTable.getColumns().get(selectedColumnIndex));
            summaryTable.scrollTo(summaryTable.getSelectionModel().getSelectedItem());
        }
    }

    private void onPersonStageClose(String message) {
        if (personStage != null) {
            personStage.close();
        }
        summaryTable.requestFocus();
        Notifications.getInstance().unsubscribe(Notifications.PERSON_FORM_CLOSED, this);
    }

    private void openPersonStage(Person person) {
        Notifications.getInstance()
                .subscribe(Notifications.PERSON_FORM_CLOSED, this, this::onPersonStageClose);

        Cache.getInstance().add(Cache.PERSON, person);

        var scene = new Scene(ViewLoader.load(ViewLoader.View.PERSON_FORM));

        personStage = new Stage();
        personStage.setScene(scene);
        personStage.initModality(Modality.WINDOW_MODAL);
        personStage.initOwner(summaryTable.getScene().getWindow());
        personStage.setTitle(person == null ? "New person": "Edit person");
        personStage.showAndWait();
    }

    private void openDayStage(Person person, LocalDate date) {
        Notifications.getInstance()
                .subscribe(Notifications.DAY_FORM_CLOSED, this, this::onDayStageClose);

        Cache.getInstance().add(Cache.PERSON, person);
        Cache.getInstance().add(Cache.DATE, date);

        var scene = new Scene(ViewLoader.load(ViewLoader.View.DAY_FORM));

        dayStage = new Stage();
        dayStage.setScene(scene);
        dayStage.initModality(Modality.WINDOW_MODAL);
        dayStage.initOwner(summaryTable.getScene().getWindow());
        dayStage.setTitle("Edit day");
        dayStage.showAndWait();

    }

    private void onDayStageClose(String message) {
        if (dayStage != null) {
            dayStage.close();
        }
        summaryTable.requestFocus();
        Notifications.getInstance().unsubscribe(Notifications.DAY_FORM_CLOSED, this);
    }

}
