package com.onlineBankingSystem.Servlets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {
	private static final String URL = "jdbc:mysql://localhost:3306/bankdb";
    private static final String USER = "root";  // replace with your MySQL username
    private static final String PASSWORD = "root";  // replace with your MySQL password

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connection successful!");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
