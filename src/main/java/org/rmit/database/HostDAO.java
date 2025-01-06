package org.rmit.database;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.rmit.Helper.DatabaseUtil;
import org.rmit.Helper.EntityGraphUtils;
import org.rmit.model.Persons.Host;


import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class HostDAO extends DAOInterface<Host> implements ValidateLoginDAO<Host> {

    @Override
    public boolean add(Host host) {
        try (Session session = DatabaseUtil.getSession()) {
            Transaction transaction = DatabaseUtil.getTransaction(session);
            session.persist(host);
            DatabaseUtil.clearAll(session);
            transaction.commit();
            DatabaseUtil.shutdown(session);
            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return false;
        }
    }

    @Override
    public boolean update(Host host) {
        try{
            Session session = DatabaseUtil.getSession();
            Transaction transaction = session.beginTransaction();

            session.merge(host);
            DatabaseUtil.clearAll(session);

            session.getTransaction().commit();
            DatabaseUtil.shutdown(session);
            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return false;
        }
    }

    @Override
    public boolean delete(Host host) {
        try {
            Session session = DatabaseUtil.getSession();
            Transaction transaction = session.beginTransaction();
            session.delete(host);
            DatabaseUtil.clearAll(session);
            session.getTransaction().commit();
            DatabaseUtil.shutdown(session);
            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return false;
        }
    }

    @Override
    public Host get(int id, Function<Session, EntityGraph<Host>> entityGraphFunction) {
        try{
            Session session = DatabaseUtil.getSession();
            String hql = String.format(GET_BY_ID_HQL, "Host");
            Host obj = session.createQuery(hql, Host.class)
                    .setParameter("id", id)
                    .setHint("jakarta.persistence.fetchgraph", entityGraphFunction.apply(session))
                    .uniqueResult();
            DatabaseUtil.shutdown(session);
            return obj;
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Host> getAll(Function<Session, EntityGraph<Host>> entityGraphFunction) {
        try{
            Session session = DatabaseUtil.getSession();

            String hql = String.format(GET_ALL_HQL, "Host");
            List<Host> list = session.createQuery(hql, Host.class)
                    .setHint("jakarta.persistence.fetchgraph", entityGraphFunction.apply(session))  // Apply EntityGraph
                    .list();  // Fetch the list of Renters
            DatabaseUtil.shutdown(session);
            return list;
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    // Custom Exception class
    public class DatabaseException extends Exception {
        public DatabaseException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    @Override
    public Host validateLogin(String usernameOrContact, String password) {
        try {
            Session session = DatabaseUtil.getSession();
            String hql = String.format(VALIDATE_LOGIN_HQL, "Host");
            Host user = session.createQuery(hql, Host.class)
                    .setParameter("input", usernameOrContact)
                    .setParameter("password", password)
                    .setHint("javax.persistence.fetchgraph", EntityGraphUtils.HostFULL(session))
                    .uniqueResult();
            DatabaseUtil.shutdown(session);
            return user;
        } catch (Exception e) {
            System.out.println("Error: not found user");
            return null;
        }
    }

    public EntityGraph<Host> createEntityGraph(Session session) {
        EntityManager emf = session.unwrap(EntityManager.class);
        EntityGraph<Host> entityGraph = emf.createEntityGraph(Host.class);

        propertySubgraph(entityGraph.addSubgraph("propertiesManaged"));
        personSubgraph(entityGraph.addSubgraph("cooperatingOwners"));
        rentalAgreementSubgraph(entityGraph.addSubgraph("rentalAgreements"));
        notificationGraph(entityGraph.addSubgraph("sentNotifications"));
        notificationGraph(entityGraph.addSubgraph("receivedNotifications"));

        return entityGraph;
    }

    @Override
    public List<Host> search(String keyword, Function<Session, EntityGraph<Host>> entityGraphFunction) {
        Session session = DatabaseUtil.getSession();
        List<Host> result = Collections.emptyList(); // Properly initialized

        try {
            // JPQL to search by address (partial match) or ID (exact match)
            String jpql = "SELECT c FROM Host c " +
                    "WHERE LOWER(c.name) LIKE :nameKeyword " +
                    "OR c.id = :idKeyword " +
                    "OR LOWER(c.username) LIKE :usernameKeyword";

            result = session.createQuery(jpql, Host.class)
                    .setMaxResults(10) // Limit results
                    .setParameter("nameKeyword", "%" + keyword.toLowerCase() + "%")
                    .setParameter("idKeyword", parseId(keyword))
                    .setParameter("usernameKeyword", "%" + keyword.toLowerCase() + "%")
                    .setHint("jakarta.persistence.fetchgraph", entityGraphFunction.apply(session))
                    .list();
            DatabaseUtil.shutdown(session);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return result;
    }

}
