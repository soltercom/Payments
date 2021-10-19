package ru.spb.altercom.payments.ui.summary;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.spb.altercom.payments.model.Person;
import ru.spb.altercom.payments.model.Summary;

import java.math.BigDecimal;
import java.util.Arrays;

public class SummaryTableRowModel {

    private final ObjectProperty<Person> person = new SimpleObjectProperty<>();

    private final ObjectProperty<BigDecimal> openingBalance = new SimpleObjectProperty<>();
    private final ObjectProperty<BigDecimal> closingBalance = new SimpleObjectProperty<>();

    private final ObservableList<Summary.DayTurnover> daysList;

    private final Summary summary;

    public SummaryTableRowModel(Summary summary) {
        this.summary = summary;
        person.set(summary.getPerson());
        openingBalance.set(summary.getOpeningBalance());
        closingBalance.set(summary.getClosingBalance());
        daysList = FXCollections.observableArrayList();
        daysList.addAll(Arrays.asList(summary.getDays()));
    }

    public ObjectProperty<Person> personProperty() {
        return person;
    }

    public Person getPerson() {
        return person.get();
    }

    public ObjectProperty<BigDecimal> openingBalanceProperty() {
        return openingBalance;
    }

    public ObjectProperty<BigDecimal> closingBalanceProperty() {
        return closingBalance;
    }

    public ObjectProperty<Summary.DayTurnover> day1Property() {
        return new SimpleObjectProperty<>(daysList.get(0));
    }

    public ObjectProperty<Summary.DayTurnover> day2Property() {
        return new SimpleObjectProperty<>(daysList.get(1));
    }

    public ObjectProperty<Summary.DayTurnover> day3Property() {
        return new SimpleObjectProperty<>(daysList.get(2));
    }

    public ObjectProperty<Summary.DayTurnover> day4Property() {
        return new SimpleObjectProperty<>(daysList.get(3));
    }

    public ObjectProperty<Summary.DayTurnover> day5Property() {
        return new SimpleObjectProperty<>(daysList.get(4));
    }

    public ObjectProperty<Summary.DayTurnover> day6Property() {
        return new SimpleObjectProperty<>(daysList.get(5));
    }

    public ObjectProperty<Summary.DayTurnover> day7Property() {
        return new SimpleObjectProperty<>(daysList.get(6));
    }

}
