package ru.spb.altercom.payments.ui.summary;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.spb.altercom.payments.repository.SummaryRepository;
import ru.spb.altercom.payments.utils.Cache;
import ru.spb.altercom.payments.utils.Notifications;
import ru.spb.altercom.payments.utils.Utils;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class SummaryTableViewModel {

    private final ObservableList<SummaryTableRowModel> summaryList = FXCollections.observableArrayList();
    private final ObjectProperty<SummaryTableRowModel> selectedTableRow = new SimpleObjectProperty<>();

    private LocalDate startDate;

    private Consumer<SummaryTableRowModel> onSelect;

    public SummaryTableViewModel() {
        startDate = Utils.getStartOfTheWeek();
        updateSummaryList(null);
        Notifications.getInstance().subscribe(Notifications.SUMMARY_LIST_UPDATE, this, this::updateSummaryList);
    }

    public ObservableList<SummaryTableRowModel> getSummaryList() {
        return summaryList;
    }

    public ObjectProperty<SummaryTableRowModel> selectedTableRowProperty() {
        return selectedTableRow;
    }

    public void setOnSelect(Consumer<SummaryTableRowModel> consumer) {
        onSelect = consumer;
    }

    private void updateSummaryList(String str) {

        var id = (UUID) Cache.getInstance().get(Cache.PERSON_ID); // if new Person created
        if (id == null) {
            id = (selectedTableRow.get() == null) ?
                null : selectedTableRow.get().getPerson().getId();
        }
        final var selectedPersonId = id;

        var startDate = getStartDate();
        var endDate = startDate.plusDays(6);
        var list = SummaryRepository.getAll(startDate, endDate);
        summaryList.clear();
        list.forEach(summary -> summaryList.add(new SummaryTableRowModel(summary)));

        if (selectedPersonId != null) {
            var selectedRow = summaryList.stream()
                    .filter(row -> row.getPerson().getId().equals(selectedPersonId))
                    .findFirst();

            Optional.of(onSelect).ifPresent(consumer -> consumer.accept(selectedRow.orElse(null)));
        }
    }

    private LocalDate getStartDate() {
        return startDate;
    }

    public int getDayColumnNumber(String id) {
        return Integer.parseInt(id
                .replace("day", "")
                .replace("Column", ""));
    }

    public LocalDate getDateByColumnId(String id) {
        return startDate.plusDays(getDayColumnNumber(id) - 1);
    }

    public String[] getDays() {
        return Utils.getDaysOfTheWeek(getStartDate());
    }

    public void nextWeek() {
        startDate = startDate.plusDays(7);
        updateSummaryList("");
    }

    public void prevWeek() {
        startDate = startDate.minusDays(7);
        updateSummaryList("");
    }

}
