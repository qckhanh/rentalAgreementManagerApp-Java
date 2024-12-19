package org.rmit.database;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.rmit.model.Property.ResidentialProperty;

import java.util.List;

public class ResidentialPropertyDAO implements DAOInterface<ResidentialProperty> {

    @Override
    public boolean add(ResidentialProperty residentialProperty) {
        try{
            Session session = DatabaseUtil.getSession();
            Transaction transaction = DatabaseUtil.getTransaction(session);
            session.merge(residentialProperty);
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
    public boolean update(ResidentialProperty residentialProperty) {
        try{
            Session session = DatabaseUtil.getSession();
            Transaction transaction = DatabaseUtil.getTransaction(session);
            session.update(residentialProperty);
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
    public boolean delete(ResidentialProperty residentialProperty) {
        try{
            Session session = DatabaseUtil.getSession();
            Transaction transaction = DatabaseUtil.getTransaction(session);
            session.delete(residentialProperty);
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
    public ResidentialProperty get(int id) {
        try{
            Session session = DatabaseUtil.getSession();
            ResidentialProperty residentialProperty = session.get(ResidentialProperty.class, id);
            DatabaseUtil.shutdown(session);
            return residentialProperty;
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<ResidentialProperty> getAll() {
        try{
            Session session = DatabaseUtil.getSession();
            List<ResidentialProperty> residentialProperties = session.createQuery("from ResidentialProperty").list();
            DatabaseUtil.shutdown(session);
            return residentialProperties;
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public ResidentialProperty validateLogin(String usernameOrContact, String password) {
        return null;
    }

}
