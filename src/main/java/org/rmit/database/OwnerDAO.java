package org.rmit.database;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.rmit.model.Owner;

import java.util.List;

public class OwnerDAO implements DAOInterface<Owner>{
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
            Owner owner = session.get(Owner.class, id);
            DatabaseUtil.shutdown(session);
            return owner;
        }
        catch (Exception e){
            System.out.println("Error in getting owner: " + e.getMessage());
            return null;
        }
    }

    @Override
    public List<Owner> getAll() {
        try{
            Session session = DatabaseUtil.getSession();
            List<Owner> owners = session.createQuery("from Owner").list();
            DatabaseUtil.shutdown(session);
            return owners;
        }
        catch (Exception e){
            System.out.println("Error in getting all owners: " + e.getMessage());
            return List.of();
        }
    }

}
