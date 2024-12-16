package org.rmit.database;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.rmit.model.RentalAgreement;

import java.util.List;

public class RentalAgreementDAO implements DAOInterface<RentalAgreement>{
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
            RentalAgreement rentalAgreement = session.get(RentalAgreement.class, id);
            DatabaseUtil.shutdown(session);
            return rentalAgreement;
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
            List<RentalAgreement> rentalAgreements = session.createQuery("from RentalAgreement").list();
            DatabaseUtil.shutdown(session);
            return rentalAgreements;
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return List.of();
        }
    }

}
