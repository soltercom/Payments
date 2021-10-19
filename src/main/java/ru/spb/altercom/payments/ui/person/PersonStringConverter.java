package ru.spb.altercom.payments.ui.person;

import javafx.util.StringConverter;
import ru.spb.altercom.payments.model.Person;

public class PersonStringConverter extends StringConverter<Person> {
    @Override
    public String toString(Person object) {
        return object.getName();
    }

    @Override
    public Person fromString(String string) {
        return null;
    }
}
