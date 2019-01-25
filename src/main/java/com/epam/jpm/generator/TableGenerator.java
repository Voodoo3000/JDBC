package com.epam.jpm.generator;

import com.epam.jpm.connection.ConnectionPool;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TableGenerator {
    private static final Logger LOGGER = Logger.getLogger(TableGenerator.class);
    private ConnectionPool pool = ConnectionPool.getInstance();

    /*
    * Generates random tables with random number of columns and types.
    */
    public void createRandomTable(int tableNum, String column) {
        Connection collection = pool.getConnection();
        String sql = "CREATE TABLE RANDOM_TABLE" + tableNum + " (ID INT PRIMARY KEY AUTO_INCREMENT, " + column + ");";
        try {
            PreparedStatement preparedStatement = collection.prepareStatement(sql);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            LOGGER.error("Creating random table SQLException", e);
        } finally {
            if (collection != null) {
                pool.returnConnection(collection);
            }
        }
    }
}