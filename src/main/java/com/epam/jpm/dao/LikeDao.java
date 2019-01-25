package com.epam.jpm.dao;

import com.epam.jpm.connection.ConnectionPool;
import com.epam.jpm.entity.Like;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LikeDao implements Dao<Like> {
    private static final Logger LOGGER = Logger.getLogger(LikeDao.class);
    private ConnectionPool pool = ConnectionPool.getInstance();

    @Override
    public void add(Like like) {
        Connection collection = pool.getConnection();
        String sql = "INSERT INTO LIKES(POST_ID, USER_ID, TIMESTAMP) VALUES(?, ?, ?)";
        try {
            PreparedStatement preparedStatement = collection.prepareStatement(sql);
            preparedStatement.setInt(1, like.getPostId());
            preparedStatement.setInt(2, like.getUserId());
            preparedStatement.setDate(3, like.getTimestamp());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            LOGGER.error("Like adding or updating SQLException", e);
        } finally {
            if (collection != null) {
                pool.returnConnection(collection);
            }
        }
        LOGGER.info("User: " + like.getUserId() + " has liked post: " + like.getPostId() + " in " + like.getTimestamp() + " it has been created and wrote in DB");
    }

    public Like getLikeByPostAndUserId(int postId, int userId) {
        Connection collection = pool.getConnection();
        Like like = null;
        String sql = "SELECT ID, POST_ID, USER_ID, TIMESTAMP FROM LIKES WHERE POST_ID=? AND USER_ID=?";
        try {
            PreparedStatement preparedStatement = collection.prepareStatement(sql);
            preparedStatement.setInt(1, postId);
            preparedStatement.setInt(2, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                like = new Like();
                like.setId(resultSet.getInt("ID"));
                like.setPostId(resultSet.getInt("POST_ID"));
                like.setUserId(resultSet.getInt("USER_ID"));
                like.setTimestamp(resultSet.getDate("TIMESTAMP"));
            }
            preparedStatement.close();
        } catch (SQLException e) {
            LOGGER.error("Get like by two params SQLException", e);
        } finally {
            if (collection != null) {
                pool.returnConnection(collection);
            }
        }
        return like;
    }

    public List<Integer> getLikedPostIdsByLikesQuantity(int quantityOfLikes) {
        Connection collection = pool.getConnection();
        List<Integer> likedPostsIds = new ArrayList<>();
        String sql = "SELECT POST_ID, COUNT(*) COUNT FROM LIKES GROUP BY POST_ID HAVING COUNT(*)>=" + quantityOfLikes;
        try {
            PreparedStatement preparedStatement = collection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                likedPostsIds.add(resultSet.getInt("POST_ID"));
            }
            preparedStatement.close();
        } catch (SQLException e) {
            LOGGER.error("Get liked post ids by quantity of likes SQLException", e);
        } finally {
            if (collection != null) {
                pool.returnConnection(collection);
            }
        }
        return likedPostsIds;
    }

    @Override
    public List<Like> getAll() {
        return null;
    }

    @Override
    public Like getById(int id) {
        return null;
    }

    @Override
    public void update(Like entity) { }

    @Override
    public void remove(Like entity) { }
}
