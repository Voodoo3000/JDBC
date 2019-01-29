package com.epam.jpm.tool;

import com.epam.jpm.connection.ConnectionPool;
import com.epam.jpm.data.MetadataProvider;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class TableCopyingTool {
    private static final Logger LOGGER = Logger.getLogger(TableCopyingTool.class);
    private ConnectionPool pool = ConnectionPool.getInstance();
    private MetadataProvider provider = new MetadataProvider();

    public void copyTable(String tableName) {
        Connection connection = pool.getConnection();
        String createTableSql = buildCreateTableScript(tableName);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(createTableSql);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            LOGGER.error("Get table column metadata SQLException", e);
        } finally {
            if (connection != null) {
                pool.returnConnection(connection);
            }
        }
        copyTableContent(tableName);
    }

    private void copyTableContent(String tableName) {
        Connection connection = pool.getConnection();
        PreparedStatement preparedStatement;
        String insertIntoTableSql = "INSERT INTO " + tableName +" SELECT * FROM jmp_db." + tableName;
        try {
            preparedStatement = connection.prepareStatement(insertIntoTableSql);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            LOGGER.error("Get table column metadata SQLException", e);
        } finally {
            if (connection != null) {
                pool.returnConnection(connection);
            }
        }
    }

    public String buildCreateTableScript(String tableName) {
        StringBuilder sb = new StringBuilder();
        Map<String, String> tableColumnMetadata = provider.getTableColumnMetadataMap(tableName);
        sb.append("CREATE TABLE ").append(tableName).append(" (ID INT PRIMARY KEY AUTO_INCREMENT, ");
        for (String columnName : tableColumnMetadata.keySet()) {
            sb.append(columnName)
                    .append(" ")
                    .append(tableColumnMetadata.get(columnName))
                    .append(", ");
        }
        return sb.substring(0, sb.length() - 2) + ");";
    }
}
