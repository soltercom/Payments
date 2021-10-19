package ru.spb.altercom.payments.repository;

import com.zaxxer.hikari.HikariDataSource;
import ru.spb.altercom.payments.MainApp;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {

    private static final HikariDataSource datasource;

    static {
        datasource = new HikariDataSource();
        datasource.setJdbcUrl("jdbc:h2:~/payments;");
        datasource.setUsername("sa");
        datasource.setPassword("password");
    }

    public static Connection getConnection() throws SQLException {
        return datasource.getConnection();
    }

}
