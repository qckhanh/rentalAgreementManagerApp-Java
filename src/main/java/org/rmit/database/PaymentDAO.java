package org.rmit.database;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.rmit.model.Agreement.Payment;

import java.util.List;

public class PaymentDAO implements DAOInterface<Payment> {
    @Override
    public boolean add(Payment payment) {
        try{
            Session session = DatabaseUtil.getSession();
            Transaction transaction = session.beginTransaction();
            session.merge(payment);
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
