package ru.spb.altercom.payments.ui.day;

import ru.spb.altercom.payments.model.Payment;

public class PaymentConverter {

    public Payment toPayment(DayFormViewModel model) {
        return new Payment(model.getPerson(), model.getPayment(), model.getDate());
    }

}
