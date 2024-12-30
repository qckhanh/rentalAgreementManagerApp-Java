package org.rmit.database;

import jakarta.persistence.EntityGraph;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.rmit.Notification.Notification;

import java.util.List;

public class NotificationDAO extends DAOInterface<Notification> {
    @Override
    public boolean add(Notification notification) {
        return false;
    }

    @Override
    public boolean update(Notification notification) {
        try{
            Session session = DatabaseUtil.getSession();
            Transaction transaction = DatabaseUtil.getTransaction(session);
            session.merge(notification);
            transaction.commit();
            DatabaseUtil.shutdown(session);
            return true;
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return false;
        }
        finally {
            DatabaseUtil.shutdown(DatabaseUtil.getSession());
        }
    }

    @Override
    public boolean delete(Notification notification) {
        return false;
    }

    @Override
    public Notification get(int id) {
        try{
            Session  session = DatabaseUtil.getSession();
            Transaction transaction = DatabaseUtil.getTransaction(session);
            Notification notification = session.get(Notification.class, id);
            DatabaseUtil.shutdown(session);
            return notification;
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
            return null;
        }
        finally {
            DatabaseUtil.shutdown(DatabaseUtil.getSession());
        }

    }

    @Override
    public List<Notification> getAll() {
        return List.of();
    }

    @Override
    public EntityGraph<Notification> createEntityGraph(Session session) {
        return null;
    }

    @Override
    public List<Notification> search(String keyword) {
        return List.of();
    }
}
