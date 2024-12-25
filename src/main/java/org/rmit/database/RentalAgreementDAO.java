package org.rmit.database;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.rmit.model.Agreement.RentalAgreement;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Property.CommercialProperty;

import java.util.List;

public class RentalAgreementDAO extends DAOInterface<RentalAgreement>{
    @Override
    public boolean add(RentalAgreement rentalAgreement) {
        try{
            Session session = DatabaseUtil.getSession();
            Transaction transaction = DatabaseUtil.getTransaction(session);
            session.persist(rentalAgreement);
            transaction.commit();
            DatabaseUtil.shutdown(session);
            return true;
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(RentalAgreement rentalAgreement) {
        try{
            Session session = DatabaseUtil.getSession();
            Transaction transaction = DatabaseUtil.getTransaction(session);
            session.update(rentalAgreement);
            transaction.commit();
            DatabaseUtil.shutdown(session);
            return true;
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(RentalAgreement rentalAgreement) {
        try{
            Session session = DatabaseUtil.getSession();
            Transaction transaction = DatabaseUtil.getTransaction(session);
            session.delete(rentalAgreement);
            transaction.commit();
            DatabaseUtil.shutdown(session);
            return true;
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    @Override
    public RentalAgreement get(int id) {
        try{
            Session session = DatabaseUtil.getSession();
            String hql = String.format(GET_BY_ID_HQL, "RentalAgreement");
            RentalAgreement obj = session.createQuery(hql, RentalAgreement.class)
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
    public List<RentalAgreement> getAll() {
        try{
            Session session = DatabaseUtil.getSession();
            String hql = String.format(GET_ALL_HQL, "RentalAgreement");
            List<RentalAgreement> list = session.createQuery(hql, RentalAgreement.class)
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
    public RentalAgreement validateLogin(String usernameOrContact, String password) {
        return null;
    }

    @Override
    public EntityGraph<RentalAgreement> createEntityGraph(Session session) {
        EntityManager emf = session.unwrap(EntityManager.class);
        EntityGraph<RentalAgreement> entityGraph = emf.createEntityGraph(RentalAgreement.class);
        return entityGraph;
    }

}
