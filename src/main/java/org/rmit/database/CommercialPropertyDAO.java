package org.rmit.database;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Subgraph;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Owner;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Property.CommercialProperty;

import java.util.Collections;
import java.util.List;

public class CommercialPropertyDAO extends DAOInterface<CommercialProperty> {

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
            String hql = String.format(GET_BY_ID_HQL, "CommercialProperty");
            CommercialProperty obj = session.createQuery(hql, CommercialProperty.class)
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
    public List<CommercialProperty> getAll() {
        try{
            Session session = DatabaseUtil.getSession();
            String hql = String.format(GET_ALL_HQL, "CommercialProperty");
            List<CommercialProperty> list = session.createQuery(hql, CommercialProperty.class)
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
    public CommercialProperty validateLogin(String username, String password) {
        return null;
    }

    @Override
    public EntityGraph<CommercialProperty> createEntityGraph(Session session) {
        EntityManager emf = session.unwrap(EntityManager.class);
        EntityGraph<CommercialProperty> entityGraph = emf.createEntityGraph(CommercialProperty.class);
        entityGraph.addAttributeNodes("address", "price", "type", "id", "businessType", "totalParkingSpace", "squareMeters"); // Add only the name of the Owner

        Subgraph<Owner> ownerSubgraph = entityGraph.addSubgraph("owner"); // Assuming `owner` is the name of the relationship in Property
        ownerSubgraph.addAttributeNodes("name"); // Add only the name of the Owner

        rentalAgreementSubgraph(entityGraph.addSubgraph("agreementList"));
        return entityGraph;
    }

    @Override
    public List<CommercialProperty> search(String keyword) {
        Session session = DatabaseUtil.getSession();
        List<CommercialProperty> result = Collections.emptyList(); // Properly initialized

        try {
            // JPQL to search by address (partial match) or ID (exact match)
            String jpql = "SELECT c FROM CommercialProperty c " +
                    "WHERE LOWER(c.address) LIKE :addressKeyword " +
                    "OR c.id = :idKeyword";

            result = session.createQuery(jpql, CommercialProperty.class)
                    .setMaxResults(10) // Limit results
                    .setParameter("addressKeyword", "%" + keyword.toLowerCase() + "%")
                    .setParameter("idKeyword", parseId(keyword)) // Handle ID as a long
                    .setHint("jakarta.persistence.fetchgraph", createEntityGraph(session))
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.shutdown(session);
        }
        return result;
    }

    // Helper method to parse the ID from the keyword


}
