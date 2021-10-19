package ru.spb.altercom.payments.utils;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.StringConverter;
import javafx.util.converter.BigDecimalStringConverter;
import ru.spb.altercom.payments.model.Person;
import ru.spb.altercom.payments.model.Summary;
import ru.spb.altercom.payments.ui.person.PersonStringConverter;
import ru.spb.altercom.payments.ui.summary.SummaryTableRowModel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.UUID;

public class Utils {

    public static void anchorPane(Pane pane) {
        AnchorPane.setBottomAnchor(pane, 0.0);
        AnchorPane.setTopAnchor(pane, 0.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);
    }

    public static UUID newUUID() {
        return UUID.fromString("00000000-0000-0000-0000-000000000000");
    }

    public static StringConverter<UUID> UUIDStringConverter() {
        return new StringConverter<>() {
            @Override
            public String toString(UUID object) {
                return object.toString();
            }

            @Override
            public UUID fromString(String string) {
                return UUID.fromString(string);
            }
        };
    }

    public static StringConverter<LocalDate> LocalDateStringConverter() {
        return new StringConverter<>() {

            private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            @Override
            public String toString(LocalDate object) {
                if (object == null) {
                    return "";
                }
                return formatter.format(object);
            }

            @Override
            public LocalDate fromString(String string) {
                return LocalDate.parse(string, formatter);
            }
        };
    }

    public static StringConverter<BigDecimal> BigDecimalStringConverter(String zero) {
        return new StringConverter<>() {

            @Override
            public String toString(BigDecimal object) {
                if (object == null || isZero(object)) {
                    return zero;
                }
                return object.toPlainString();
            }

            @Override
            public BigDecimal fromString(String string) {
                try {
                    return new BigDecimal(string);
                } catch (NumberFormatException ignored) {}
                return BigDecimal.ZERO;
            }
        };
    }

    public static TableCell<SummaryTableRowModel, Summary.DayTurnover> dayCellFactory(TableColumn<SummaryTableRowModel, Summary.DayTurnover> tableColumn) {
        return new TableCell<>() {
            @Override
            protected void updateItem(Summary.DayTurnover item, boolean empty) {
                super.updateItem(item, empty);

                this.setText(null);
                this.setGraphic(null);

                if (!empty) {
                    var converter = BigDecimalStringConverter("");
                    var chargeValue = converter.toString(item.getCharge());
                    var paymentValue = converter.toString(item.getPayment());

                    var chargeLabel = new Label(chargeValue);
                    chargeLabel.setPrefWidth(100);
                    chargeLabel.setAlignment(Pos.CENTER);
                    //chargeLabel.setStyle("-fx-text-fill: navy;");
                    var separator = new Separator(Orientation.HORIZONTAL);
                    var paymentLabel = new Label(paymentValue);
                    paymentLabel.setPrefWidth(100);
                    paymentLabel.setAlignment(Pos.CENTER);
                    //paymentLabel.setStyle("-fx-text-fill: forestgreen");

                    var gp = new GridPane();
                    gp.setPadding(new Insets(2));
                    gp.setVgap(2);

                    gp.add(chargeLabel, 0, 0);
                    gp.add(separator, 0, 1);
                    gp.add(paymentLabel, 0, 2);

                    this.setGraphic(gp);

                    this.setAlignment(Pos.CENTER);
                }
            }
        };
    }

    public static TableCell<SummaryTableRowModel, BigDecimal> balanceCellFactory(TableColumn<SummaryTableRowModel, BigDecimal> tableColumn) {
        return new TableCell<>() {
            @Override
            protected void updateItem(BigDecimal item, boolean empty) {
                super.updateItem(item, empty);

                this.setText(null);
                this.setGraphic(null);

                if (!empty) {
                    var converter = BigDecimalStringConverter("");
                    var pane = new StackPane();
                    var label = new Label(converter.toString(item));
                    label.setPadding(new Insets(0, 4, 0, 0));
                    label.setFont(Font.font("System", FontWeight.BOLD, 12));
                    pane.getChildren().add(label);
                    pane.setAlignment(Pos.CENTER_RIGHT);
                    this.setGraphic(pane);
                }
            }
        };
    }

    public static TableCell<SummaryTableRowModel, Person> personCellFactory(TableColumn<SummaryTableRowModel, Person> tableColumn) {
        return new TableCell<>() {
            @Override
            protected void updateItem(Person item, boolean empty) {
                super.updateItem(item, empty);

                this.setText(null);
                this.setGraphic(null);

                if (!empty) {
                    var converter = new PersonStringConverter();
                    var pane = new StackPane();
                    var label = new Label(converter.toString(item));
                    label.setPadding(new Insets(0, 0, 0, 4));
                    label.setFont(Font.font("System", FontWeight.BOLD, 12));
                    pane.getChildren().add(label);
                    pane.setAlignment(Pos.CENTER_LEFT);
                    this.setGraphic(pane);
                }
            }
        };
    }

    public static LocalDate getStartOfTheWeek() {
        var date = LocalDateTime.now();
        return date.with(ChronoField.DAY_OF_WEEK, 1).toLocalDate();
    }

    public static String[] getDaysOfTheWeek(LocalDate startDate) {
        var dateConverter = Utils.LocalDateStringConverter();
        var daysOfTheWeek = new String[7];
        for (int i = 0; i < 7; i++) {
            daysOfTheWeek[i] = dateConverter.toString(startDate.plusDays(i));
        }
        return daysOfTheWeek;
    }

    public static TextFormatter<BigDecimal> getMoneyTextFormatter() {
        return new TextFormatter<BigDecimal>(change -> {
            if (change.getControlNewText().isEmpty()) {
                return change;
            }
            if (change.getControlNewText().matches("(0?|[1-9][0-9]*)[.]([0-9][0-9]|[0-9]?)")) {
                return change;
            } else {
                return null;
            }
        });
    }

    public static boolean isNotZero(BigDecimal number) {
        return BigDecimal.ZERO.compareTo(number) != 0;
    }

    public static boolean isZero(BigDecimal number) {
        return BigDecimal.ZERO.compareTo(number) == 0;
    }

}
