package org.rmit.database;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Renter;
import org.rmit.view.Host.HostViewFactory;

import java.util.List;

public class HostDAO extends DAOInterface<Host> {

    @Override
    public boolean add(Host host) {
        try{
            Session session = DatabaseUtil.getSession();
            Transaction transaction = session.beginTransaction();
            session.persist(host);
            session.getTransaction().commit();
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
            session.update(host);
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
            session.getTransaction().commit();
            DatabaseUtil.shutdown(session);
            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return false;
        }
    }

    @Override
    public Host get(int id) {
        try{
            Session session = DatabaseUtil.getSession();
            String hql = String.format(GET_BY_ID_HQL, "Host");
            Host obj = session.createQuery(hql, Host.class)
                    .setParameter("id", id)
                    .setHint("jakarta.persistence.fetchgraph", createEntityGraph(session))
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
    public List<Host> getAll() {
        try{
            Session session = DatabaseUtil.getSession();
            String hql = String.format(GET_ALL_HQL, "Host");
            List<Host> list = session.createQuery(hql, Host.class)
                    .setHint("jakarta.persistence.fetchgraph", createEntityGraph(session))  // Apply EntityGraph
                    .list();  // Fetch the list of Renters
            return list;
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return null;
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
                    .setHint("javax.persistence.fetchgraph", createEntityGraph(session))
                    .uniqueResult();
            DatabaseUtil.shutdown(session);
            return user;
        } catch (Exception e) {
            System.out.println("Error: not found user");
            return null;
        }
    }

    @Override
    public EntityGraph<Host> createEntityGraph(Session session) {
        EntityManager emf = session.unwrap(EntityManager.class);
        EntityGraph<Host> entityGraph = emf.createEntityGraph(Host.class);

        propertySubgraph(entityGraph.addSubgraph("propertiesManaged"));

        return entityGraph;
    }

    @Override
    public Host search(String keyword) {
        return null;
    }

}
