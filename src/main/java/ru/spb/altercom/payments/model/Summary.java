package ru.spb.altercom.payments.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Summary {

    private final static int DAYS_SIZE = 7;

    private final Person person;

    private BigDecimal openingBalance;
    private BigDecimal chargeTurnover;
    private BigDecimal paymentTurnover;
    private BigDecimal closingBalance;

    private final DayTurnover[] days = new DayTurnover[DAYS_SIZE];

    public Summary(Person person) {
        this.person = person;
        for (int i = 0; i < days.length; i++) {
            this.days[i] = new DayTurnover();
        }
        this.openingBalance = BigDecimal.ZERO;
        this.closingBalance = BigDecimal.ZERO;
        this.chargeTurnover = BigDecimal.ZERO;
        this.paymentTurnover = BigDecimal.ZERO;
    }

    @Data
    @AllArgsConstructor
    public static class DayTurnover {
        private BigDecimal charge;
        private BigDecimal payment;

        public DayTurnover() {
            charge = BigDecimal.ZERO;
            payment = BigDecimal.ZERO;
        }
    }

}
