package ru.spb.altercom.payments.ui.day;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import ru.spb.altercom.payments.model.Person;
import ru.spb.altercom.payments.repository.ChargeRepository;
import ru.spb.altercom.payments.repository.PaymentRepository;
import ru.spb.altercom.payments.utils.Cache;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DayFormViewModel {

    private final ObjectProperty<Person> person = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private final ObjectProperty<BigDecimal> charge = new SimpleObjectProperty<>(BigDecimal.ZERO);
    private final ObjectProperty<BigDecimal> payment = new SimpleObjectProperty<>(BigDecimal.ZERO);

    private final ChargeConverter chargeConverter = new ChargeConverter();
    private final PaymentConverter paymentConverter = new PaymentConverter();

    public DayFormViewModel() {
        person.set((Person) Cache.getInstance().get(Cache.PERSON));
        date.set((LocalDate) Cache.getInstance().get(Cache.DATE));
        charge.set(ChargeRepository.getAmountByPersonAndDate(person.getValue(), date.getValue()));
        payment.set(PaymentRepository.getAmountByPersonAndDate(person.getValue(), date.getValue()));
    }

    public Person getPerson() {
        return person.get();
    }

    public ObjectProperty<Person> personProperty() {
        return person;
    }

    public void setPerson(Person person) {
        this.person.set(person);
    }

    public LocalDate getDate() {
        return date.get();
    }

    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public BigDecimal getCharge() {
        return charge.get();
    }

    public ObjectProperty<BigDecimal> chargeProperty() {
        return charge;
    }

    public BigDecimal getPayment() {
        return payment.get();
    }

    public ObjectProperty<BigDecimal> paymentProperty() {
        return payment;
    }

    public boolean save() {
        var charge = chargeConverter.toCharge(this);
        var payment = paymentConverter.toPayment(this);
        var result = ChargeRepository.update(charge);
        return PaymentRepository.update(payment) & result;
    }
}
