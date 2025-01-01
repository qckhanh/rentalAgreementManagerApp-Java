package org.rmit.database;

import jakarta.persistence.*;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.rmit.model.Persons.Renter;

import java.util.Collections;
import java.util.List;

public class RenterDAO extends DAOInterface<Renter> implements ValidateLoginDAO<Renter>, ImagesDAO<Renter> {

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
            session.merge(renter);

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
            String hql = String.format(GET_BY_ID_HQL, "Renter");
            Renter obj = session.createQuery(hql, Renter.class)
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
    public List<Renter> getAll() {
        try{
            Session session = DatabaseUtil.getSession();
            String hql = String.format(GET_ALL_HQL, "Renter");
            List<Renter> list = session.createQuery(hql, Renter.class)
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
    public Renter validateLogin(String usernameOrContact, String password) {
        try {
            Session session = DatabaseUtil.getSession();
            // Query to search for a Renter based on username or contact and password
            String hql = String.format(VALIDATE_LOGIN_HQL, "Renter");
            Renter user = session.createQuery(hql, Renter.class)
                    .setParameter("input", usernameOrContact)
                    .setParameter("password", password)
                    .setHint("javax.persistence.fetchgraph", createEntityGraph(session))
                    .uniqueResult();
            DatabaseUtil.shutdown(session);
            return user;
        } catch (Exception e) {
            System.out.println("Error: not found user");
            return null;
        }
    }

    @Override
    public EntityGraph<Renter> createEntityGraph(Session session) {
        EntityManager emf = session.unwrap(EntityManager.class);
        EntityGraph<Renter> entityGraph = emf.createEntityGraph(Renter.class);
        entityGraph.addAttributeNodes("id", "name","dateOfBirth", "contact", "username", "password", "profileAvatar");

        rentalAgreementSubgraph(entityGraph.addSubgraph("agreementList"));
        paymentGraph(entityGraph.addSubgraph("payments"));
        notificationGraph(entityGraph.addSubgraph("sentNotifications"));
        notificationGraph(entityGraph.addSubgraph("receivedNotifications"));
        return entityGraph;
    }

    @Override
    public List<Renter> search(String keyword) {
        List<Renter> list = Collections.emptyList();
        try{
            Session session = DatabaseUtil.getSession();
            Transaction transaction = DatabaseUtil.getTransaction(session);
            String HQL = "From Renter r where LOWER(r.name) like :nameKeyword or r.id = :idKeyword";
            list = session.createQuery(HQL, Renter.class)
                    .setParameter("nameKeyword", "%" + keyword.toLowerCase() + "%")
                    .setParameter("idKeyword", parseId(keyword))
                    .setHint("jakarta.persistence.fetchgraph", createEntityGraph(session))
                    .list();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            DatabaseUtil.shutdown(DatabaseUtil.getSession());
            return list;
        }
    }


    @Override
    public List<byte[]> getImageByID(int id) {
        List<byte[]> list = Collections.emptyList();
        Session session = DatabaseUtil.getSession();
        try{
            String hql = "SELECT r.profileAvatar FROM Renter r WHERE id = :id";
            list = session.createQuery(hql, byte[].class)
                    .setParameter("id", id)
                    .list();
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
        DatabaseUtil.shutdown(session);
        return list;
    }
}
