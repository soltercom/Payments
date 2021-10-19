package ru.spb.altercom.payments.ui.person;

import javafx.beans.property.*;
import ru.spb.altercom.payments.model.Person;
import ru.spb.altercom.payments.repository.PersonRepository;
import ru.spb.altercom.payments.utils.Cache;
import ru.spb.altercom.payments.utils.Notifications;
import ru.spb.altercom.payments.utils.Utils;

import java.util.UUID;

public class PersonFormViewModel {

    private final ObjectProperty<UUID> id = new SimpleObjectProperty<>(Utils.newUUID());
    private final StringProperty name = new SimpleStringProperty("");
    private final BooleanProperty disable = new SimpleBooleanProperty(false);

    private final PersonConverter converter = new PersonConverter();

    private final boolean isNew;

    public PersonFormViewModel() {
        var person = (Person) Cache.getInstance().get(Cache.PERSON);
        if (person != null) {
            setId(person.getId());
            setName(person.getName());
            setDisable(person.isDisable());
        }
        isNew = person == null;
    }

    public boolean save() {
        var person = converter.toPerson(this);
        if (isNew) {
            var id = PersonRepository.add(person);
            Cache.getInstance().add(Cache.PERSON_ID, id);
            return id != null;
        } else {
            return PersonRepository.update(person);
        }
    }

    public UUID getId() {
        return id.get();
    }

    public ObjectProperty<UUID> idProperty() {
        return id;
    }

    public void setId(UUID id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public boolean isDisable() {
        return disable.get();
    }

    public BooleanProperty disableProperty() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable.set(disable);
    }

}
