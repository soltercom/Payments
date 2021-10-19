package ru.spb.altercom.payments.repository;

import ru.spb.altercom.payments.model.Charge;
import ru.spb.altercom.payments.model.Payment;
import ru.spb.altercom.payments.model.Person;
import ru.spb.altercom.payments.utils.Utils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

public class PaymentRepository {

    private static boolean delete(Connection connection, Payment payment) throws SQLException {
        var sql = "DELETE FROM PAYMENTS WHERE PERSON = ? AND DATE = ?";

        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, payment.getPerson().getId());
            stmt.setObject(2, payment.getDate());

            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            connection.rollback();
        }
        return false;
    }

    private static boolean add(Connection connection, Payment payment) throws SQLException {
        var sql = "INSERT INTO PAYMENTS(PERSON, AMOUNT, DATE) VALUES(?, ?, ?)";

        try (var stmt = connection.prepareStatement(sql)) {

            stmt.setObject(1, payment.getPerson().getId());
            stmt.setObject(2, payment.getAmount());
            stmt.setObject(3, payment.getDate());

            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            connection.rollback();
        }
        return false;
    }

    public static boolean update(Payment payment) {
        try (var connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            var result = delete(connection, payment);
            if (Utils.isNotZero(payment.getAmount())) {
                result = add(connection, payment) & result;
            }
            connection.commit();
            connection.setAutoCommit(true);
            return result;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static BigDecimal getAmountByPersonAndDate(Person person, LocalDate date) {
        var sql = "SELECT amount FROM PAYMENTS WHERE person = ? AND date = ?";

        try (var connection = DBConnection.getConnection();
             var stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, person.getId());
            stmt.setObject(2, date);
            var resultSet = stmt.executeQuery();
            var result = 0D;
            while (resultSet.next()) {
                result = resultSet.getDouble(1);
            }
            return BigDecimal.valueOf(result);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return BigDecimal.ZERO;
    }

}
