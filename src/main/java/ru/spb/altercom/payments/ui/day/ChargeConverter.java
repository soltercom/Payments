package ru.spb.altercom.payments.ui.day;

import ru.spb.altercom.payments.model.Charge;

public class ChargeConverter {

    public Charge toCharge(DayFormViewModel model) {
        return new Charge(model.getPerson(), model.getCharge(), model.getDate());
    }

}
