package main.java.javafx.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/employeedata";
    private static final String USER = "root";
    private static final String password = "joshua123";

    public static Connection getConnection(){
        Connection conn = null;

        try{
            conn = DriverManager.getConnection(URL, USER, password);
        } catch(SQLException e) {
            System.out.println("Connection to Database Failed");
            e.printStackTrace();
        }

        return conn;
    }
}