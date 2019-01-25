package com.epam.jpm.dao;

import com.epam.jpm.connection.ConnectionPool;
import com.epam.jpm.entity.Friendship;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FriendshipDao implements Dao<Friendship> {
    private static final Logger LOGGER = Logger.getLogger(FriendshipDao.class);
    private ConnectionPool pool = ConnectionPool.getInstance();

    @Override
    public void add(Friendship friendship) {
        Connection collection = pool.getConnection();
        String sql = "INSERT INTO FRIENDSHIPS(USER_ID1, USER_ID2, TIMESTAMP) VALUES(?, ?, ?)";
        try {
            PreparedStatement preparedStatement = collection.prepareStatement(sql);
            preparedStatement.setInt(1, friendship.getUserId1());
            preparedStatement.setInt(2, friendship.getUserId2());
            preparedStatement.setDate(3, friendship.getTimestamp());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            LOGGER.error("Friendship adding or updating SQLException", e);
        } finally {
            if (collection != null) {
                pool.returnConnection(collection);
            }
        }
        LOGGER.info("Friendship between userId1: " + friendship.getUserId1() + " and userId2: " + friendship.getUserId2() + " has been created and wrote in DB in " + friendship.getTimestamp());
    }

    @Override
    public List<Friendship> getAll() {
        Connection collection = pool.getConnection();
        List<Friendship> friendships = new ArrayList<>();
        String sql = "SELECT ID, USER_ID1, USER_ID2, TIMESTAMP FROM FRIENDSHIPS";
        try {
            PreparedStatement preparedStatement = collection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Friendship friendship = getFriendship(resultSet);
                friendships.add(friendship);
            }
            preparedStatement.close();
        } catch (SQLException e) {
            LOGGER.error("Get all friendships SQLException", e);
        } finally {
            if (collection != null) {
                pool.returnConnection(collection);
            }
        }
        return friendships;
    }


    @Override
    public Friendship getById(int id) {
        return null;
    }

    public Friendship getFriendshipByUserIds(int userId1, int userId2) {
        Connection collection = pool.getConnection();
        Friendship friendship = null;
        String sql = "SELECT ID, USER_ID1, USER_ID2, TIMESTAMP FROM FRIENDSHIPS WHERE USER_ID1=? AND USER_ID2=?";
        try {
            PreparedStatement preparedStatement = collection.prepareStatement(sql);
            preparedStatement.setInt(1, userId1);
            preparedStatement.setInt(2, userId2);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                friendship = getFriendship(resultSet);
            }
            preparedStatement.close();
        } catch (SQLException e) {
            LOGGER.error("Get friendship by two ids SQLException", e);
        } finally {
            if (collection != null) {
                pool.returnConnection(collection);
            }
        }
        return friendship;
    }

    public List<Integer> getAllFriendshipsMultipleParams(String sqlParam2, String sqlParam1, int userId, Date timestamp) {
        Connection collection = pool.getConnection();
        List<Integer> friendIds = new ArrayList<>();
        String sql = "SELECT " + sqlParam2 + " FROM FRIENDSHIPS WHERE " + sqlParam1 + "=? AND TIMESTAMP>?";
        try {
            PreparedStatement preparedStatement = collection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setDate(2, timestamp);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                friendIds.add(resultSet.getInt(sqlParam2));
            }
            preparedStatement.close();
        } catch (SQLException e) {
            LOGGER.error("Get friend ids by multiple params SQLException", e);
        } finally {
            if (collection != null) {
                pool.returnConnection(collection);
            }
        }
        return friendIds;
    }

    @Override
    public void update(Friendship entity) {
    }

    @Override
    public void remove(Friendship entity) {

    }

    private Friendship getFriendship(ResultSet resultSet) throws SQLException {
        Friendship friendship = new Friendship();
        friendship.setId(resultSet.getInt("ID"));
        friendship.setUserId1(resultSet.getInt("USER_ID1"));
        friendship.setUserId2(resultSet.getInt("USER_ID2"));
        friendship.setTimestamp(resultSet.getDate("TIMESTAMP"));
        return friendship;
    }
}
