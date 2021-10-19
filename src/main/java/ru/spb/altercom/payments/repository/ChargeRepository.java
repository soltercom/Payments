package ru.spb.altercom.payments.repository;

import ru.spb.altercom.payments.model.Charge;
import ru.spb.altercom.payments.model.Person;
import ru.spb.altercom.payments.utils.Utils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

public class ChargeRepository {

    private static boolean delete(Connection connection, Charge charge) throws SQLException {
        var sql = "DELETE FROM charges WHERE person = ? AND date = ?";

        try (var stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, charge.getPerson().getId());
            stmt.setObject(2, charge.getDate());

            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            connection.rollback();
        }

        return false;
    }

    private static boolean add(Connection connection, Charge charge) throws SQLException {
        var sql = "INSERT INTO charges(person, amount, date) VALUES(?, ?, ?)";

        try (var stmt = connection.prepareStatement(sql)) {

            stmt.setObject(1, charge.getPerson().getId());
            stmt.setObject(2, charge.getAmount());
            stmt.setObject(3, charge.getDate());

            stmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            connection.rollback();
        }
        return false;
    }

    public static boolean update(Charge charge) {
        try (var connection = DBConnection.getConnection()) {
            connection.setAutoCommit(false);
            var result = delete(connection, charge);
            if (Utils.isNotZero(charge.getAmount())) {
                result = add(connection, charge) & result;
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
        var sql = "SELECT amount FROM charges WHERE person = ? AND date = ?";

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
