package com.epam.jpm.dao;

import com.epam.jpm.entity.Entity;

import java.util.List;

public interface Dao<T extends Entity> {
    //create
    void add(T entity);

    //read
    List<T> getAll();

    T getById(int id);

    //update
    void update(T entity);

    //delete
    void remove(T entity);
}
