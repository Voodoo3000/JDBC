package com.epam.jpm.data;

import com.epam.jpm.connection.ConnectionPool;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class MetadataProvider {
    private static final Logger LOGGER = Logger.getLogger(MetadataProvider.class);
    private ConnectionPool pool = ConnectionPool.getInstance();

    /*
    * It returns list of randomly created table names
    */
    public List<String> getTableNames() {
        Connection collection = pool.getConnection();
        List<String> tableNameList = new ArrayList<>();
        String sql = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES where TABLE_NAME LIKE 'random_table%'";
        try {
            PreparedStatement preparedStatement = collection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                tableNameList.add(resultSet.getString("TABLE_NAME"));
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            LOGGER.error("Get table names SQLException", e);
        } finally {
            if (collection != null) {
                pool.returnConnection(collection);
            }
        }
        return tableNameList;
    }

    /*
    * It returns column names and column types except id column
    */
    public Map<String, String> getTableColumnMetadataMap(String tableName) {
        Connection collection = pool.getConnection();
        Map<String, String> tableColumnMetadata = new LinkedHashMap<>();
        String sql = "SHOW COLUMNS FROM jmp_db." + tableName;
        try {
            PreparedStatement preparedStatement = collection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                tableColumnMetadata.put(resultSet.getString("Field"), resultSet.getString("Type"));
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            LOGGER.error("Get table column metadata SQLException", e);
        } finally {
            if (collection != null) {
                pool.returnConnection(collection);
            }
        }
        tableColumnMetadata.remove("ID");
        return tableColumnMetadata;
    }
}
