package org.rmit.database;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.rmit.model.Agreement.Payment;
import org.rmit.model.Agreement.RentalAgreement;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Property.CommercialProperty;
import org.rmit.model.Property.Property;

import java.util.List;

public class PaymentDAO extends DAOInterface<Payment> {
    @Override
    public boolean add(Payment payment) {
        Session session = DatabaseUtil.getSession();
        try {
            Transaction transaction = session.beginTransaction();

            // Merge related entities to ensure they are attached to the session
            RentalAgreement rentalAgreement = session.merge(payment.getRentalAgreement());  // Merge RentalAgreement
            rentalAgreement.getPayments().add(payment);  // Add payment to the rental agreement

            // Persist the payment entity as it's a new entity
            session.merge(payment);  // Persist the payment itself

            // Commit transaction
            transaction.commit();
            DatabaseUtil.shutdown(session);
            return true;
        } catch (Exception e) {
            DatabaseUtil.shutdown(session);
            System.out.println("Error: " + e);
            return false;
        }
    }

    @Override
    public boolean update(Payment payment) {
        try{
            Session session = DatabaseUtil.getSession();
            Transaction transaction = session.beginTransaction();
            session.update(payment);
            transaction.commit();
            DatabaseUtil.shutdown(session);
            return true;
        }
        catch (Exception e){
            System.out.println("Error: " + e);
            return false;
        }
    }

    @Override
    public boolean delete(Payment payment) {
        try{
            Session session = DatabaseUtil.getSession();
            Transaction transaction = session.beginTransaction();
            session.delete(payment);
            transaction.commit();
            DatabaseUtil.shutdown(session);
            return true;
        }
        catch (Exception e){
            System.out.println("Error: " + e);
            return false;
        }
    }

    @Override
    public Payment get(int id) {
        try{
            Session session = DatabaseUtil.getSession();
            String hql = String.format(GET_BY_ID_HQL, "Payment");
            Payment obj = session.createQuery(hql, Payment.class)
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
    public List<Payment> getAll() {
        try{
            Session session = DatabaseUtil.getSession();
            String hql = String.format(GET_ALL_HQL, "Payment");
            List<Payment> list = session.createQuery(hql, Payment.class)
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
    public Payment validateLogin(String usernameOrContact, String password) {
        return null;
    }

    @Override
    public EntityGraph<Payment> createEntityGraph(Session session) {
        EntityManager emf = session.unwrap(EntityManager.class);
        EntityGraph<Payment> entityGraph = emf.createEntityGraph(Payment.class);
        return entityGraph;
    }

    @Override
    public Payment search(String keyword) {
        return null;
    }


}
