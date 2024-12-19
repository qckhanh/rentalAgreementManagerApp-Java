package org.rmit.database;

import java.util.List;

public interface DAOInterface<T>{
    boolean add(T t);
    boolean update(T t);
    boolean delete(T t);
    T get(int id);
    List<T> getAll();
    T validateLogin(String username, String password);
}
