package ru.spb.altercom.payments.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Objects;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Person {

    private UUID id;
    private String name;
    private boolean disable;

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return getId().equals(person.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
