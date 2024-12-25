package org.rmit.database;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Subgraph;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.rmit.model.Persons.Owner;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Property.CommercialProperty;
import org.rmit.model.Property.ResidentialProperty;

import java.util.List;

public class ResidentialPropertyDAO extends DAOInterface<ResidentialProperty> {

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
            String hql = String.format(GET_BY_ID_HQL, "ResidentialProperty");
            ResidentialProperty obj = session.createQuery(hql, ResidentialProperty.class)
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
    public List<ResidentialProperty> getAll() {
        try{
            Session session = DatabaseUtil.getSession();
            String hql = String.format(GET_ALL_HQL, "ResidentialProperty");
            List<ResidentialProperty> list = session.createQuery(hql, ResidentialProperty.class)
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
    public ResidentialProperty validateLogin(String usernameOrContact, String password) {
        return null;
    }

    @Override
    public EntityGraph<ResidentialProperty> createEntityGraph(Session session) {
        EntityManager emf = session.unwrap(EntityManager.class);
        EntityGraph<ResidentialProperty> entityGraph = emf.createEntityGraph(ResidentialProperty.class);
        entityGraph.addAttributeNodes("address", "price", "type", "id", "totalRoom", "totalBedroom", "isPetAllowed","hasGarden"); // Add only the name of the Owner

        Subgraph<Owner> ownerSubgraph = entityGraph.addSubgraph("owner"); // Assuming `owner` is the name of the relationship in Property
        ownerSubgraph.addAttributeNodes("name"); // Add only the name of the Owner
        rentalAgreementSubgraph(entityGraph.addSubgraph("agreementList"));

        return entityGraph;
    }

    @Override
    public ResidentialProperty search(String keyword) {
        return null;
    }

}
