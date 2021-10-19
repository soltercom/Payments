package ru.spb.altercom.payments.repository;

import ru.spb.altercom.payments.model.Person;
import ru.spb.altercom.payments.model.Summary;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class SummaryRepository {

    public static Map<UUID, BigDecimal> getAllBalance(LocalDate atDate) {
        var sql = "SELECT PERSON, SUM(AMOUNT) AS BALANCE FROM " +
                "(SELECT PERSON, AMOUNT FROM CHARGES WHERE DATE < ? " +
                "UNION " +
                "SELECT PERSON, -AMOUNT FROM PAYMENTS WHERE DATE < ?) " +
                "GROUP BY PERSON";

        var map = new HashMap<UUID, BigDecimal>();

        try (var connection = DBConnection.getConnection();
             var stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, atDate);
            stmt.setObject(2, atDate);

            var resultSet = stmt.executeQuery();
            while (resultSet.next()) {
                map.put((UUID) resultSet.getObject("person"),
                        (BigDecimal) resultSet.getObject("balance"));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return map;
    }

    public static List<Summary> getAll(LocalDate startDate, LocalDate endDate) {
        var sql = "SELECT PEOPLE.ID, PEOPLE.NAME, PEOPLE.DISABLE, TURNOVERS.DATE, TURNOVERS.CHARGE, TURNOVERS.PAYMENT " +
            "FROM PEOPLE " +
            "LEFT JOIN (SELECT PERSON, DATE, SUM(CHARGE) AS CHARGE, SUM(PAYMENT) AS PAYMENT FROM ( " +
            "SELECT CHARGES.PERSON, CHARGES.DATE, CHARGES.AMOUNT AS CHARGE, 0 AS PAYMENT FROM CHARGES " +
            "WHERE CHARGES.DATE >= ? AND CHARGES.DATE <= ? " +
            "UNION " +
            "SELECT PAYMENTS.PERSON, PAYMENTS.DATE, 0, PAYMENTS.AMOUNT FROM PAYMENTS " +
            "WHERE PAYMENTS.DATE >= ? AND PAYMENTS.DATE <= ? " +
            ") " +
            "GROUP BY PERSON, DATE) AS TURNOVERS " +
            "ON PEOPLE.ID = TURNOVERS.PERSON " +
            "ORDER BY NAME";

        var list = new ArrayList<Summary>();

        var startBalance = getAllBalance(startDate);
        var endBalance = getAllBalance(endDate.plusDays(1));

        try (var connection = DBConnection.getConnection();
             var stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, startDate);
            stmt.setObject(2, endDate);
            stmt.setObject(3, startDate);
            stmt.setObject(4, endDate);

            var resultSet = stmt.executeQuery();

            var map = new HashMap<Person, Summary>();

            var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            while (resultSet.next()) {

                var person = new Person(
                    (UUID) resultSet.getObject("id"),
                    resultSet.getString("name"),
                    resultSet.getBoolean("disable"));

                Summary summary = null;
                if (map.containsKey(person)) {
                    summary = map.get(person);
                } else {
                    summary = new Summary(person);
                    list.add(summary);
                    map.put(person, summary);
                }

                var openingBalance = startBalance
                        .getOrDefault(person.getId(), BigDecimal.ZERO);
                summary.setOpeningBalance(openingBalance);
                var closingBalance = endBalance
                        .getOrDefault(person.getId(), BigDecimal.ZERO);
                summary.setClosingBalance(closingBalance);

                if (resultSet.getObject("date") != null) {
                    var date = LocalDate.parse(resultSet.getString("date"), formatter);
                    var charge = (BigDecimal) resultSet.getObject("charge");
                    var payment = (BigDecimal) resultSet.getObject("payment");
                    summary.getDays()[date.getDayOfWeek().getValue() - 1] = new Summary.DayTurnover(charge, payment);
                }

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

}
