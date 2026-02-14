package com.example.laba2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Утилита для подключения к MySQL через JDBC.
 * Сервер: localhost:3306, база: db1, пользователь: root, пароль: 1111.
 */
public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/db1";
    private static final String USER = "root";
    private static final String PASSWORD = "1111";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Драйвер MySQL не найден", e);
        }
    }

    /**
     * Возвращает новое соединение с базой данных.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
