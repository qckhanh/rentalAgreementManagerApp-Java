package org.rmit.database;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.rmit.Helper.DatabaseUtil;
import org.rmit.Notification.Notification;
import org.rmit.model.Agreement.RentalAgreement;

import java.util.List;
import java.util.function.Function;

public class RentalAgreementDAO extends DAOInterface<RentalAgreement>{
    @Override
    public boolean add(RentalAgreement rentalAgreement) {
        try{
            Session session = DatabaseUtil.getSession();
            Transaction transaction = DatabaseUtil.getTransaction(session);
            session.persist(rentalAgreement);
            DatabaseUtil.clearAll(session);
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
            session.merge(rentalAgreement);
            DatabaseUtil.clearAll(session);
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
            DatabaseUtil.clearAll(session);
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
    public RentalAgreement get(int id, Function<Session, EntityGraph<RentalAgreement>> entityGraphFunction) {
        try{
            Session session = DatabaseUtil.getSession();
            String hql = String.format(GET_BY_ID_HQL, "RentalAgreement");
            RentalAgreement obj = session.createQuery(hql, RentalAgreement.class)
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
    public List<RentalAgreement> getAll(Function<Session, EntityGraph<RentalAgreement>> sessionEntityGraphFunction) {
        try{
            Session session = DatabaseUtil.getSession();
            String hql = String.format(GET_ALL_HQL, "RentalAgreement");
            List<RentalAgreement> list = session.createQuery(hql, RentalAgreement.class)
                    .setHint("jakarta.persistence.fetchgraph", sessionEntityGraphFunction.apply(session))  // Apply EntityGraph
                    .list();  // Fetch the list of Renters
            DatabaseUtil.shutdown(session);
            return list;
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }


    public EntityGraph<RentalAgreement> createEntityGraph(Session session) {
        EntityManager emf = session.unwrap(EntityManager.class);
        EntityGraph<RentalAgreement> entityGraph = emf.createEntityGraph(RentalAgreement.class);
        personSubgraph(entityGraph.addSubgraph("host"));
        return entityGraph;
    }

    @Override
    public List<RentalAgreement> search(String keyword, Function<Session, EntityGraph<RentalAgreement>> entityGraphFunction) {
        return null;
    }

}
