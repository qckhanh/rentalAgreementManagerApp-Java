package org.rmit.database;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.rmit.model.Persons.Admin;

import java.util.Date;
import java.util.List;

public class AdminDAO extends DAOInterface<Admin> implements ValidateLoginDAO<Admin> {
    @Override
    public boolean add(Admin admin) {
        return false;
    }

    @Override
    public boolean update(Admin admin) {
        return false;
    }

    @Override
    public boolean delete(Admin admin) {
        try {
            Session session = DatabaseUtil.getSession();
            Transaction transaction = session.beginTransaction();
            session.delete(admin);
            session.getTransaction().commit();
            DatabaseUtil.shutdown(session);
            return true;
        } catch (Exception e){
            System.out.println("Error: " + e);
            return false;
        }
    }

    @Override
    public Admin get(int id) {
        return null;
    }

    @Override
    public List<Admin> getAll() {
        try {
            Session session = DatabaseUtil.getSession();
            String hql = String.format(GET_ALL_HQL, "Admin");
            List<Admin> list = session.createQuery(hql, Admin.class)
                    .setHint("jakarta.persistence.fetchgraph", createEntityGraph(session))
                    .list();
            return list;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public EntityGraph<Admin> createEntityGraph(Session session) {
        EntityManager emf = session.unwrap(EntityManager.class);
        EntityGraph<Admin> entityGraph = emf.createEntityGraph(Admin.class);
        return entityGraph;
    }

    @Override
    public List<Admin> search(String keyword) {
        return List.of();
    }

    @Override
    public Admin validateLogin(String username, String password) {
        try {
            Session session = DatabaseUtil.getSession();
            String hql = String.format(VALIDATE_LOGIN_HQL, "Admin");
            Admin admin = session.createQuery(hql, Admin.class)
                    .setParameter("input", username)
                    .setParameter("password", password)
                    .setHint("jakarta.persistence.fetchgraph", createEntityGraph(session))
                    .uniqueResult();
            return admin;
        }
        catch (Exception e) {
            System.out.println("Error user not found");
            return null;
        }
    }
}
