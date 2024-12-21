package org.rmit.database;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.rmit.model.Agreement.Payment;
import org.rmit.model.Agreement.RentalAgreement;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Property.Property;

import java.util.List;

public class PaymentDAO implements DAOInterface<Payment> {
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
            Payment payment = session.get(Payment.class, id);
            DatabaseUtil.shutdown(session);
            return payment;
        }
        catch (Exception e){
            System.out.println("Error: " + e);
            return null;
        }
    }

    @Override
    public List<Payment> getAll() {
        try{
            Session session = DatabaseUtil.getSession();
            List<Payment> payments = session.createQuery("from Payment").list();
            DatabaseUtil.shutdown(session);
            return payments;
        }
        catch (Exception e){
            System.out.println("Error: " + e);
            return List.of();
        }
    }

    @Override
    public Payment validateLogin(String usernameOrContact, String password) {
        return null;
    }

}
