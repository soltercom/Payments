package ru.spb.altercom.payments.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Payment {

    private Person person;
    private BigDecimal amount;
    private LocalDate date;

}
