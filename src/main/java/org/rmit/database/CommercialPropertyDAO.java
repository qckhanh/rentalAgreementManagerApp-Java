package org.rmit.database;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.rmit.model.Property.CommercialProperty;

import java.util.List;

public class CommercialPropertyDAO implements DAOInterface<CommercialProperty> {

    @Override
    public boolean add(CommercialProperty commercialProperty) {
        try{
            Session session = DatabaseUtil.getSession();
            Transaction transaction = session.beginTransaction();
            session.merge(commercialProperty);
            transaction.commit();
            DatabaseUtil.shutdown(session);
            return true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(CommercialProperty commercialProperty) {
        try{
            Session session = DatabaseUtil.getSession();
            Transaction transaction = session.beginTransaction();
            session.update(commercialProperty);
            transaction.commit();
            DatabaseUtil.shutdown(session);
            return true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(CommercialProperty commercialProperty) {
        try{
            Session session = DatabaseUtil.getSession();
            Transaction transaction = session.beginTransaction();
            session.delete(commercialProperty);
            transaction.commit();
            DatabaseUtil.shutdown(session);
            return true;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public CommercialProperty get(int id) {
        try{
            Session session = DatabaseUtil.getSession();
            CommercialProperty commercialProperty = session.get(CommercialProperty.class, id);
            DatabaseUtil.shutdown(session);
            return commercialProperty;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<CommercialProperty> getAll() {
        try{
            Session session = DatabaseUtil.getSession();
            List<CommercialProperty> commercialProperties = session.createQuery("from CommercialProperty").list();
            DatabaseUtil.shutdown(session);
            return commercialProperties;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return List.of();
        }
    }

    @Override
    public CommercialProperty validateLogin(String username, String password) {
        return null;
    }

}
