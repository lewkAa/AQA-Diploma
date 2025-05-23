package ru.netology.data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLHelper {
    private static final QueryRunner runner = new QueryRunner();

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/app", "app", "pass");
    }

    @SneakyThrows
    public static String getStatus() {
        var countSQL = "SELECT status FROM payment_entity;";
        try (var conn = getConnection()) {
            return runner.query(conn, countSQL, new ScalarHandler<>());
        }
    }

    @SneakyThrows
    public static void cleanDB() {
        try (var conn = getConnection()) {
            runner.update(conn, "SET FOREIGN_KEY_CHECKS = 0");
            runner.update(conn, "TRUNCATE TABLE payment_entity");
            runner.update(conn, "TRUNCATE TABLE order_entity");
            runner.update(conn, "TRUNCATE TABLE credit_request_entity");
            runner.update(conn, "SET FOREIGN_KEY_CHECKS = 1");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to clean database", e);
        }
    }
}
