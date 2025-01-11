package org.rmit.database;

import jakarta.persistence.EntityGraph;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.rmit.Helper.DatabaseUtil;
import org.rmit.model.Persons.Person;
import java.util.List;
import java.util.function.Function;

public abstract class DAOInterface<T>{

    protected final String GET_ALL_HQL = "SELECT t FROM %s t";
    protected final String GET_BY_ID_HQL = "SELECT t FROM %s t WHERE t.id = :id";
    protected final String VALIDATE_LOGIN_HQL = "from %s where (username = :input or contact = :input) and password = :password";
    protected final int MAX_ATTEMPTS = 3;

    public abstract boolean add(T t);
    public abstract boolean update(T t);
    public abstract boolean delete(T t);
    public abstract T get(int id, Function<Session, EntityGraph<T>> entityGraphFunction);
    public abstract List<T> getAll(Function<Session, EntityGraph<T>> entityGraphFunction);
    public abstract List<T> search(String keyword, Function<Session, EntityGraph<T>> entityGraphFunction);

    //Helper methods
    public static boolean isExistedUsername(Class<? extends Person> clazz, String username){
        Session session = DatabaseUtil.getSession();
        String hql = "SELECT 1 FROM " + clazz.getSimpleName()  + " u WHERE u.username = :username";
        Query<Integer> query = session.createQuery(hql, Integer.class);
        query.setParameter("username", username);
        query.setMaxResults(1);
        List<Integer> result = query.list();
        return result.isEmpty();
    }

    protected Long parseId(String keyword) {
        try {
            return Long.parseLong(keyword); // Try converting to Long for ID comparison
        } catch (NumberFormatException e) {
            return -1L; // Return an invalid ID if parsing fails
        }
    }

}
