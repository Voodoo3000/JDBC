package com.epam.jpm.connection;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyConnection {
    private static final Logger LOGGER = Logger.getLogger(MyConnection.class);
    private static MyConnection connection = new MyConnection();

    public static MyConnection getInstance() {
        return connection;
    }

    public Connection getConnection() {
        LOGGER.info("***** MySQL JDBC Connection Testing *****");
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            LOGGER.warn("Exception to getting JDBC driver");
            ex.printStackTrace();
        }
        try {
            String userName = "root";
            String password = "root";
            String url = "jdbc:MySQL://localhost/jmp_db";
            conn = DriverManager.getConnection(url, userName, password);
            LOGGER.info("Database Connection Established...");
        } catch (SQLException ex) {
            LOGGER.error("Cannot connect to database server");
            ex.printStackTrace();
        }
        return conn;
    }
}
