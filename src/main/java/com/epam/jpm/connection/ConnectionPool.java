package com.epam.jpm.connection;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class ConnectionPool {
    private static final Logger LOGGER = Logger.getLogger(ConnectionPool.class);
    BlockingQueue<Connection> connections = new ArrayBlockingQueue<>(9);
    private static final ConnectionPool instance = new ConnectionPool();

    public ConnectionPool() {
        initializeConnectionPool();
    }

    public static ConnectionPool getInstance()  {
        return instance;
    }

    public void initializeConnectionPool() {
        while ((connections.size() < 9)) {
            LOGGER.info("***** MySQL JDBC Connection Testing *****");
            try {
                Class.forName("com.mysql.jdbc.Driver").newInstance();
            } catch (ClassNotFoundException e) {
                LOGGER.warn("Exception to getting JDBC driver", e);
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
            Connection connection = null;
            try {
                String userName = "root";
                String password = "root";
                String url = "jdbc:MySQL://localhost/jmp_db";
                connection = DriverManager.getConnection(url, userName, password);
                LOGGER.info("Database Connection Established...");
            } catch (SQLException e) {
                LOGGER.error("SQLException in initializeConnectionPool", e);
            }
            LOGGER.info("Filling Connection Pool With Connections");
            connections.add(connection);
        }
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = connections.take();
        } catch (InterruptedException e) {
            LOGGER.warn("InterruptedException in getConnection", e);
        }
        return connection;
    }

    public synchronized void returnConnection(Connection connection) {
        connections.add(connection);
    }

    public synchronized void closeConnections() {
        for (Connection connection : connections) {
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.error("SQLException in closeConnections", e);
            }
        }
    }
}
