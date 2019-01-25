package com.epam.jpm.generator;

import com.epam.jpm.connection.ConnectionPool;
import com.epam.jpm.data.MetadataProvider;
import com.epam.jpm.data.ValueType;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class RowGenerator {
    private static final Logger LOGGER = Logger.getLogger(RowGenerator.class);
    private ConnectionPool pool = ConnectionPool.getInstance();
    private RandomGenerator randomGenerator = new RandomGenerator();
    private MetadataProvider provider = new MetadataProvider();

    /*
    * It creates array which has size equals number of random tables
    * and where elements are random number of rows.
    * Iterates array in table list iteration and calls FillAndUpdateRowSQL
    * which fill and execute built SQL
    */
    public void createRandomRowsInTables() {
        int[] tableRowsArray = new int[provider.getTableNames().size()];
        for (String tableName : provider.getTableNames()) {
            tableRowsArray[provider.getTableNames().indexOf(tableName)] = randomGenerator.randBetween(10, 20);
            LOGGER.info(tableRowsArray[provider.getTableNames().indexOf(tableName)]);
            for (int i = 0; i < tableRowsArray[provider.getTableNames().indexOf(tableName)]; i++) {
                fillAndUpdateRowSql(tableName);
            }
        }
    }

    /*
    * Iterates column metadata and fills each cell in row.
    * The choice of filled value depends on column type which is detected by switch operator.
    * After filling query is executed.
    */
    private void fillAndUpdateRowSql(String tableName) {
        Connection collection = pool.getConnection();
        Map<String, String> tableColumnMetadata = provider.getTableColumnMetadataMap(tableName);
        int i = 0;
        String sql = "INSERT INTO " + tableName + "(" + buildInsertColumnsAndValuesSql(tableName) + ")";
        try {
            LOGGER.info("Insert in " + tableName);
            PreparedStatement preparedStatement = collection.prepareStatement(sql);
            for (String columnName : tableColumnMetadata.keySet()) {
                i++;
                switch (ValueType.valueOf(tableColumnMetadata.get(columnName))) {
                    case VARCHAR:
                        preparedStatement.setString(i, "DATA_BASE_STRESS_TEST");
                        break;
                    case INT:
                        preparedStatement.setInt(i, 9);
                        break;
                    case DOUBLE:
                        preparedStatement.setDouble(i, 3.14);
                        break;
                    case BOOLEAN:
                        preparedStatement.setBoolean(i, true);
                        break;
                }
            }
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            LOGGER.error("Get table column metadata SQLException", e);
        } finally {
            if (collection != null) {
                pool.returnConnection(collection);
            }
        }
    }

    /*
    * It puts necessary quantity of columns and values for each table.
    */
    private String buildInsertColumnsAndValuesSql(String tableName) {
        StringBuilder sb = new StringBuilder();
        Map<String, String> tableColumnMetadata = provider.getTableColumnMetadataMap(tableName);
        for (String columnName : tableColumnMetadata.keySet()) {
            sb.append(columnName).append(", ");
        }
        String str = sb.substring(0, sb.length() - 2) + ") VALUES(";
        StringBuilder sb1 = new StringBuilder();
        sb1.append(str);
        for (int i = 0; i < tableColumnMetadata.size(); i++) {
            sb1.append("?, ");
        }
        return sb1.substring(0, sb1.length() - 2);
    }
}
