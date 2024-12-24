package org.rmit.database;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Renter;
import org.rmit.view.Host.HostViewFactory;

import java.util.List;

public class HostDAO implements DAOInterface<Host> {

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
        try {
            Session session = DatabaseUtil.getSession();
            Host host = session.get(Host.class, id);
            DatabaseUtil.shutdown(session);
            return host;
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return null;
        }
    }

    @Override
    public List<Host> getAll() {
        try {
            Session session = DatabaseUtil.getSession();
            List<Host> hosts = session.createQuery("from Host").list();
            DatabaseUtil.shutdown(session);
            return hosts;
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return List.of();
        }
    }

    @Override
    public Host validateLogin(String usernameOrContact, String password) {
        try {
            Session session = DatabaseUtil.getSession();
            String hql = "from Host where (username = :input or contact = :input) and password = :password";
            Host user = session.createQuery(hql, Host.class)
                    .setParameter("input", usernameOrContact)
                    .setParameter("password", password)
                    .uniqueResult();
//            Hibernate.initialize(user.getPayments()); // Initialize the payments
//            Hibernate.initialize(user.getAgreementList()); // Initialize the rental agreements
            DatabaseUtil.shutdown(session);
            return user;
        } catch (Exception e) {
            System.out.println("Error: not found user");
            return null;
        }
    }

}
