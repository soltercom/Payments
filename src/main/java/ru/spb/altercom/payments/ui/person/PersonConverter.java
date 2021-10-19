package ru.spb.altercom.payments.ui.person;

import ru.spb.altercom.payments.model.Person;

public class PersonConverter {

    public Person toPerson(PersonFormViewModel model) {
        return new Person(model.getId(), model.getName(), model.isDisable());
    }

}
