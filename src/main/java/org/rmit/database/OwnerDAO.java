package org.rmit.database;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Owner;
import org.rmit.model.Persons.Renter;

import java.util.List;

public class OwnerDAO extends DAOInterface<Owner>{
    @Override
    public boolean add(Owner owner) {
        try{
            Session session = DatabaseUtil.getSession();
            Transaction transaction = session.beginTransaction();

            session.persist(owner);

            transaction.commit();
            DatabaseUtil.shutdown(session);
            return true;
        }
        catch (Exception e){
            System.out.println("Error in adding owner: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Owner owner) {
        try{
            Session session = DatabaseUtil.getSession();
            Transaction transaction = session.beginTransaction();

            session.update(owner);

            transaction.commit();
            DatabaseUtil.shutdown(session);
            return true;
        }
        catch (Exception e){
            System.out.println("Error in updating owner: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Owner owner) {
        try{
            Session session = DatabaseUtil.getSession();
            Transaction transaction = session.beginTransaction();

            session.delete(owner);

            transaction.commit();
            DatabaseUtil.shutdown(session);
            return true;
        }
        catch (Exception e){
            System.out.println("Error in deleting owner: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Owner get(int id) {
        try{
            Session session = DatabaseUtil.getSession();
            String hql = String.format(GET_BY_ID_HQL, "Owner");
            Owner obj = session.createQuery(hql, Owner.class)
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
    public List<Owner> getAll() {
        try{
            Session session = DatabaseUtil.getSession();
            String hql = String.format(GET_ALL_HQL, "Owner");
            List<Owner> list = session.createQuery(hql, Owner.class)
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
    public Owner validateLogin(String usernameOrContact, String password) {
        try {
            Session session = DatabaseUtil.getSession();
            String hql = String.format(VALIDATE_LOGIN_HQL, "Owner");
            Owner user = session.createQuery(hql, Owner.class)
                    .setParameter("input", usernameOrContact)
                    .setParameter("password", password)
                    .setHint("javax.persistence.fetchgraph", createEntityGraph(session))
                    .uniqueResult();
            return user;
        } catch (Exception e) {
            System.out.println("Error: not found user");
            return null;
        }
    }

    @Override
    public EntityGraph<Owner> createEntityGraph(Session session) {
        EntityManager emf = session.unwrap(EntityManager.class);
        EntityGraph<Owner> entityGraph = emf.createEntityGraph(Owner.class);

        return entityGraph;
    }


}
