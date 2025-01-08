package org.rmit.database;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Subgraph;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.rmit.Helper.DatabaseUtil;
import org.rmit.Helper.EntityGraphUtils;
import org.rmit.model.Persons.Owner;
import org.rmit.model.Property.Property;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class OwnerDAO extends DAOInterface<Owner> implements ValidateLoginDAO<Owner> {
    @Override
    public boolean add(Owner owner) {
        try{
            Session session = DatabaseUtil.getSession();
            Transaction transaction = session.beginTransaction();
            session.persist(owner);
            DatabaseUtil.clearAll(session);
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
            Transaction transaction = DatabaseUtil.getTransaction(session);
            session.merge(owner);
            DatabaseUtil.clearAll(session);
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
    public boolean delete(Owner owner) {
        try{
            Session session = DatabaseUtil.getSession();
            Transaction transaction = DatabaseUtil.getTransaction(session);
            session.delete(owner);
            DatabaseUtil.clearAll(session);
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
    public Owner get(int id, Function<Session, EntityGraph<Owner>> entityGraphFunction) {
        try{
            Session session = DatabaseUtil.getSession();
            String hql = String.format(GET_BY_ID_HQL, "Owner");
            Owner obj = session.createQuery(hql, Owner.class)
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
    public List<Owner> getAll(Function<Session, EntityGraph<Owner>> entityGraphFunction) {
        try{
            Session session = DatabaseUtil.getSession();
            String hql = String.format(GET_ALL_HQL, "Owner");
            List<Owner> list = session.createQuery(hql, Owner.class)
                    .setHint("jakarta.persistence.fetchgraph", entityGraphFunction.apply(session))  // Apply EntityGraph
                    .list();  // Fetch the list of Renters
            DatabaseUtil.shutdown(session);
            return list;
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public Owner validateLogin(String usernameOrContact, String password) {
        try {
            Session session = DatabaseUtil.getSession();
            String hql = String.format(VALIDATE_LOGIN_HQL, "Owner");
            Owner user = session.createQuery(hql, Owner.class)
                    .setParameter("input", usernameOrContact)
                    .setParameter("password", password)
                    .setHint("javax.persistence.fetchgraph", EntityGraphUtils.OwnerFULL(session))
                    .uniqueResult();
            DatabaseUtil.shutdown(session);
            System.out.println("Session closed");
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: not found user");
            return null;
        }
    }

    public EntityGraph<Owner> createEntityGraph(Session session) {
        EntityManager emf = session.unwrap(EntityManager.class);
        EntityGraph<Owner> entityGraph = emf.createEntityGraph(Owner.class);
        propertySubgraph(entityGraph.addSubgraph("propertiesOwned"));
        personSubgraph(entityGraph.addSubgraph("hosts"));
        notificationGraph(entityGraph.addSubgraph("sentNotifications"));
        notificationGraph(entityGraph.addSubgraph("receivedNotifications"));

        return entityGraph;
    }

    @Override
    protected void propertySubgraph(Subgraph<Property> graph ){
        graph.addAttributeNodes("address", "price", "type", "id");
    }

    @Override
    public List<Owner> search(String keyword, Function<Session, EntityGraph<Owner>> entityGraphFunction) {
        Session session = DatabaseUtil.getSession();
        List<Owner> result = Collections.emptyList(); // Properly initialized

        try {
            // JPQL to search by address (partial match) or ID (exact match)
            String jpql = "SELECT c FROM Owner c " +
                    "WHERE LOWER(c.name) LIKE :nameKeyword " +
                    "OR c.id = :idKeyword " +
                    "OR LOWER(c.username) LIKE :usernameKeyword";

            result = session.createQuery(jpql, Owner.class)
                    .setMaxResults(10) // Limit results
                    .setParameter("nameKeyword", "%" + keyword.toLowerCase() + "%")
                    .setParameter("idKeyword", parseId(keyword))
                    .setParameter("usernameKeyword", "%" + keyword.toLowerCase() + "%")
                    .setHint("jakarta.persistence.fetchgraph", entityGraphFunction.apply(session))
                    .list();
            DatabaseUtil.shutdown(session);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


}
