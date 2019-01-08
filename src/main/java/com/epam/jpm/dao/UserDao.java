package com.epam.jpm.dao;

import com.epam.jpm.connection.ConnectionPool;
import com.epam.jpm.entity.User;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDao implements Dao<User> {
    private static final Logger LOGGER = Logger.getLogger(UserDao.class);
    private ConnectionPool pool = ConnectionPool.getInstance();

    @Override
    public void add(User user) {
        Connection collection = pool.getConnection();
        String sql = "INSERT INTO USERS(FIRSTNAME, LASTNAME, BIRTHDATE) VALUES(?, ?, ?)";
        try {
            PreparedStatement preparedStatement = collection.prepareStatement(sql);
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setDate(3, user.getBirthday());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            LOGGER.error("User adding or updating SQLException", e);
        } finally {
            if (collection != null) {
                pool.returnConnection(collection);
            }
        }
        LOGGER.info("User " + user.getFirstName() + " " + user.getLastName() + " " + user.getBirthday() + " has been wrote in DB");
    }

    @Override
    public List<User> getAll() {
        Connection collection = pool.getConnection();
        List<User> users = new ArrayList<>();
        String sql = "SELECT ID, FIRSTNAME, LASTNAME, BIRTHDATE FROM USER";
        try {
            PreparedStatement preparedStatement = collection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("ID"));
                user.setFirstName(resultSet.getString("FIRSTNAME"));
                user.setLastName(resultSet.getString("LASTNAME"));
                user.setBirthday(resultSet.getDate("BIRTHDATE"));
                users.add(user);
            }
            preparedStatement.close();
        } catch (SQLException e) {
            LOGGER.error("Get all users SQLException", e);
        } finally {
            if (collection != null) {
                pool.returnConnection(collection);
            }
        }
        return users;
    }

    @Override
    public User getById(int id) {
        Connection collection = pool.getConnection();
        User user = null;
        String sql = "SELECT ID, FIRSTNAME, LASTNAME, BIRTHDATE FROM USERS WHERE ID=?";
        try {
            PreparedStatement preparedStatement = collection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("ID"));
                user.setFirstName(resultSet.getString("FIRSTNAME"));
                user.setLastName(resultSet.getString("LASTNAME"));
                user.setBirthday(resultSet.getDate("BIRTHDATE"));
            }
            preparedStatement.close();
        } catch (SQLException e) {
            LOGGER.error("Get user by id SQLException", e);
        } finally {
            if (collection != null) {
                pool.returnConnection(collection);
            }
        }
        return user;
    }

    @Override
    public void update(User entity) {

    }

    @Override
    public void remove(User entity) {

    }
}
