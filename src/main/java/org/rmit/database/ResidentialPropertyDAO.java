package org.rmit.database;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Subgraph;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.rmit.Helper.DatabaseUtil;
import org.rmit.model.Persons.Person;
import org.rmit.model.Property.ResidentialProperty;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

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
            ResidentialProperty obj = session.merge(residentialProperty);
            session.delete(obj);
            transaction.commit();
            return true;
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        finally {
            DatabaseUtil.shutdown(DatabaseUtil.getSession());
        }
    }

    @Override
    public ResidentialProperty get(int id, Function<Session, EntityGraph<ResidentialProperty>> entityGraphFunction) {
        try{
            Session session = DatabaseUtil.getSession();
            String hql = String.format(GET_BY_ID_HQL, "ResidentialProperty");
            ResidentialProperty obj = session.createQuery(hql, ResidentialProperty.class)
                    .setParameter("id", id)
                    .setHint("jakarta.persistence.fetchgraph", entityGraphFunction.apply(session))
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
    public List<ResidentialProperty> getAll(Function<Session, EntityGraph<ResidentialProperty>> entityGraphFunction) {
        try{
            Session session = DatabaseUtil.getSession();
            String hql = String.format(GET_ALL_HQL, "ResidentialProperty");
            List<ResidentialProperty> list = session.createQuery(hql, ResidentialProperty.class)
                    .setHint("jakarta.persistence.fetchgraph", entityGraphFunction.apply(session))  // Apply EntityGraph
                    .list();  // Fetch the list of Renters
            return list;
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }


    public EntityGraph<ResidentialProperty> createEntityGraph(Session session) {
        EntityManager emf = session.unwrap(EntityManager.class);
        EntityGraph<ResidentialProperty> entityGraph = emf.createEntityGraph(ResidentialProperty.class);
        entityGraph.addAttributeNodes("address", "price", "type", "id", "totalRoom", "totalBedroom", "isPetAllowed","hasGarden"); // Add only the name of the Owner

        personSubgraph(entityGraph.addSubgraph("owner"));
        personSubgraph(entityGraph.addSubgraph("hosts"));


        return entityGraph;
    }

    @Override
    protected <T extends Person> void personSubgraph(Subgraph<T> graph){
        super.personSubgraph(graph);
        notificationGraph(graph.addSubgraph("receivedNotifications"));
    }

    @Override
    public List<ResidentialProperty> search(String keyword, Function<Session, EntityGraph<ResidentialProperty>> entityGraphFunction) {
        Session session = DatabaseUtil.getSession();
        List<ResidentialProperty> result = Collections.emptyList(); // Properly initialized

        try {
            // JPQL to search by address (partial match) or ID (exact match)
            String jpql = "SELECT c FROM ResidentialProperty c " +
                    "WHERE LOWER(c.address) LIKE :addressKeyword " +
                    "OR c.id = :idKeyword";

            result = session.createQuery(jpql, ResidentialProperty.class)
                    .setMaxResults(10) // Limit results
                    .setParameter("addressKeyword", "%" + keyword.toLowerCase() + "%")
                    .setParameter("idKeyword", parseId(keyword)) // Handle ID as a long
                    .setHint("jakarta.persistence.fetchgraph", entityGraphFunction.apply(session))  // Apply EntityGraph
                    .list();
        } catch (Exception e) {
            e.printStackTrace(); // Replace with proper logging
        } finally {
            DatabaseUtil.shutdown(session); // Ensure session is closed
        }
        return result;
    }

}
