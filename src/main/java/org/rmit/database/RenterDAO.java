package org.rmit.database;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.rmit.model.Persons.Renter;

import java.util.List;

public class RenterDAO implements DAOInterface<Renter> {

    @Override
    public boolean add(Renter renter) {
        try{
            Session session = DatabaseUtil.getSession();
            Transaction transaction = DatabaseUtil.getTransaction(session);
            session.persist(renter);
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
    public boolean update(Renter renter) {
        try{
            Session session = DatabaseUtil.getSession();
            Transaction transaction = DatabaseUtil.getTransaction(session);
            session.update(renter);
            transaction.commit();
            DatabaseUtil.shutdown(session);
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Renter renter) {
        try{
            Session session = DatabaseUtil.getSession();
            Transaction transaction = DatabaseUtil.getTransaction(session);
            session.delete(renter);
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
    public Renter get(int id) {
        try{
            Session session = DatabaseUtil.getSession();
            Renter renter = session.get(Renter.class, id);
            DatabaseUtil.shutdown(session);
            return renter;
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Renter> getAll() {
        try{
            Session session = DatabaseUtil.getSession();
            List<Renter> renters = session.createQuery("from Renter").list();
            DatabaseUtil.shutdown(session);
            return renters;
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

}
