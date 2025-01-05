package org.rmit.database;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.rmit.Helper.DatabaseUtil;
import org.rmit.model.Agreement.Payment;
import org.rmit.model.Agreement.RentalAgreement;

import java.util.List;
import java.util.function.Function;

public class PaymentDAO extends DAOInterface<Payment> {
    @Override
    public boolean add(Payment payment) {
        try {
            Session session = DatabaseUtil.getSession();
            Transaction transaction = session.beginTransaction();
            RentalAgreement rentalAgreement = session.merge(payment.getRentalAgreement());  // Merge RentalAgreement
            rentalAgreement.getPayments().add(payment);  // Add payment to the rental agreement
            session.merge(payment);  // Persist the payment itself
//            session.persist(payment);
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
    public boolean update(Payment payment) {
        try{
            Session session = DatabaseUtil.getSession();
            Transaction transaction = session.beginTransaction();
            session.merge(payment);
            DatabaseUtil.clearAll(session);
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
            DatabaseUtil.clearAll(session);
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
    public Payment get(int id, Function<Session, EntityGraph<Payment>> entityGraphFunction) {
        try{
            Session session = DatabaseUtil.getSession();
            String hql = String.format(GET_BY_ID_HQL, "Payment");
            Payment obj = session.createQuery(hql, Payment.class)
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
    public List<Payment> getAll(Function<Session, EntityGraph<Payment>> entityGraphFunction) {
        try{
            Session session = DatabaseUtil.getSession();
            String hql = String.format(GET_ALL_HQL, "Payment");
            List<Payment> list = session.createQuery(hql, Payment.class)
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

    public EntityGraph<Payment> createEntityGraph(Session session) {
        EntityManager emf = session.unwrap(EntityManager.class);
        EntityGraph<Payment> entityGraph = emf.createEntityGraph(Payment.class);
        return entityGraph;
    }

    @Override
    public List<Payment> search(String keyword, Function<Session, EntityGraph<Payment>> entityGraphFunction) {
        return null;
    }


}
