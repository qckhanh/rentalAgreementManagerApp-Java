package org.rmit.database;

public interface ValidateLoginDAO<T> {

    T validateLogin(String username, String password);
}
