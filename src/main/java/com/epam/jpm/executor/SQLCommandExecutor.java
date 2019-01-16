package com.epam.jpm.executor;

import com.epam.jpm.connection.MyConnection;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLCommandExecutor {
    private static final Logger LOGGER = Logger.getLogger(SQLCommandExecutor.class);
    private MyConnection connection = MyConnection.getInstance();

    public void createAndDropProcedureInDB(String sql) {
        try {
            PreparedStatement preparedStatement = connection.getConnection().prepareStatement(sql);
            preparedStatement.execute();
        } catch (SQLException e) {
            LOGGER.error("Call car adding or updating stored procedure SQLException", e);
        }
    }
}
