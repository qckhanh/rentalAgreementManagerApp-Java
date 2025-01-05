package org.rmit.database;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.rmit.Helper.DatabaseUtil;
import org.rmit.model.Persons.Admin;
import org.rmit.model.Persons.Renter;

import java.util.List;
import java.util.function.Function;

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
            DatabaseUtil.clearAll(session);
            transaction.commit();
            DatabaseUtil.shutdown(session);
            return true;
        } catch (Exception e){
            System.out.println("Error: " + e);
            return false;
        }
    }

    @Override
    public Admin get(int id, Function<Session, EntityGraph<Admin>> entityGraphFunction) {
        return null;
    }

    @Override
    public List<Admin> getAll(Function<Session, EntityGraph<Admin>> entityGraph) {
        try{
            Session session = DatabaseUtil.getSession();
            String hql = String.format(GET_ALL_HQL, "Admin");
            List<Admin> list = session.createQuery(hql, Admin.class)
                    .setHint("jakarta.persistence.fetchgraph", entityGraph.apply(session))  // Apply EntityGraph
                    .list();  // Fetch the list of Renters
            DatabaseUtil.shutdown(session);
            return list;
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public EntityGraph<Admin> createEntityGraph(Session session) {
        EntityManager emf = session.unwrap(EntityManager.class);
        EntityGraph<Admin> entityGraph = emf.createEntityGraph(Admin.class);
        return entityGraph;
    }

    @Override
    public List<Admin> search(String keyword, Function<Session, EntityGraph<Admin>> entityGraph) {
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

            DatabaseUtil.shutdown(session);
            return admin;
        }
        catch (Exception e) {
            System.out.println("Error user not found");
            return null;
        }
    }
}
