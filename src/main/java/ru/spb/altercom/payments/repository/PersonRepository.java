package ru.spb.altercom.payments.repository;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.spb.altercom.payments.model.Person;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class PersonRepository {

    public static UUID add(Person person) {
        var sql = "INSERT INTO people(name, disable) VALUES(?, ?)";

        try (var connection = DBConnection.getConnection();
             var stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, person.getName());
            stmt.setBoolean(2, person.isDisable());
            stmt.executeUpdate();

            var keysResultSet = stmt.getGeneratedKeys();
            if (keysResultSet.next()) {
                return (UUID) keysResultSet.getObject(1);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static boolean update(Person person) {
        var sql = "UPDATE people SET name = ?, disable = ? WHERE id = ?";

        try (var connection = DBConnection.getConnection();
             var stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, person.getName());
            stmt.setBoolean(2, person.isDisable());
            stmt.setObject(3, person.getId());

            var result = stmt.executeUpdate();
            return result > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static void getAll() {
        var sql = "SELECT id, name, disable FROM people";

        try (var connection = DBConnection.getConnection();
             var stmt = connection.prepareStatement(sql)) {
            var resultSet = stmt.executeQuery();
            while (resultSet.next()) {
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public static boolean delete(Person person) {
        var sql = "DELETE FROM people WHERE id = ?";

        try (var connection = DBConnection.getConnection();
             var stmt = connection.prepareStatement(sql)) {
            stmt.setObject(1, person.getId());

            var result = stmt.executeUpdate();
            if (result > 0) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

}
