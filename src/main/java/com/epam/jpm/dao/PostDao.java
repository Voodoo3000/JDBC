package com.epam.jpm.dao;

import com.epam.jpm.connection.ConnectionPool;
import com.epam.jpm.entity.Post;
import com.epam.jpm.entity.User;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PostDao implements Dao<Post> {
    private static final Logger LOGGER = Logger.getLogger(PostDao.class);
    private ConnectionPool pool = ConnectionPool.getInstance();

    @Override
    public void add(Post post) {
        Connection collection = pool.getConnection();
        String sql = "INSERT INTO POSTS(USER_ID, TEXT, TIMESTAMP) VALUES(?, ?, ?)";
        try {
            PreparedStatement preparedStatement = collection.prepareStatement(sql);
            preparedStatement.setInt(1, post.getUserId());
            preparedStatement.setString(2, post.getText());
            preparedStatement.setDate(3, post.getTimestamp());
            preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            LOGGER.error("Post adding or updating SQLException", e);
        } finally {
            if (collection != null) {
                pool.returnConnection(collection);
            }
        }
        LOGGER.info("Post: " + post.getText() + " has been created by user " + post.getUserId() + " and wrote in DB in " + post.getTimestamp());
    }

    @Override
    public List<Post> getAll() {
        return null;
    }

    @Override
    public Post getById(int id) {
        Connection collection = pool.getConnection();
        Post post = null;
        String sql = "SELECT ID, USER_ID, TEXT, TIMESTAMP FROM POSTS WHERE ID=?";
        try {
            PreparedStatement preparedStatement = collection.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                post = new Post();
                post.setId(resultSet.getInt("ID"));
                post.setUserId(resultSet.getInt("USER_ID"));
                post.setText(resultSet.getString("TEXT"));
                post.setTimestamp(resultSet.getDate("TIMESTAMP"));
            }
            preparedStatement.close();
        } catch (SQLException e) {
            LOGGER.error("Get post by id SQLException", e);
        } finally {
            if (collection != null) {
                pool.returnConnection(collection);
            }
        }
        return post;
    }

    @Override
    public void update(Post entity) {

    }

    @Override
    public void remove(Post entity) {

    }
}
