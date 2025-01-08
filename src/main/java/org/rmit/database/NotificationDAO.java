package org.rmit.database;

import jakarta.persistence.EntityGraph;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.rmit.Helper.DatabaseUtil;
import org.rmit.Notification.Notification;

import java.util.List;
import java.util.function.Function;

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

            DatabaseUtil.clearAll(session);

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
        int attempt = 0;
        boolean isDeleted = false;

        while (attempt < MAX_ATTEMPTS) {
            attempt++;
            try (Session session = DatabaseUtil.getSession()) {
                Transaction transaction = session.beginTransaction(); // Start a transaction

                // Execute the delete query for notification_receivers
                String deleteReceiversQuery = "DELETE FROM notification_receivers WHERE notification_id = :notificationId";
                session.createNativeQuery(deleteReceiversQuery)
                        .setParameter("notificationId", notification.getId())
                        .executeUpdate();

                // Execute the delete query for notification
                String deleteNotificationQuery = "DELETE FROM notification WHERE id = :notificationId";
                session.createNativeQuery(deleteNotificationQuery)
                        .setParameter("notificationId", notification.getId())
                        .executeUpdate();

                // Commit the transaction
                transaction.commit();
                isDeleted = true;
                break; // Exit loop if successful
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed: " + e.getMessage());
                e.printStackTrace();

                if (attempt == MAX_ATTEMPTS) {
                    System.out.println("Max retries reached. Unable to delete notification.");
                    break; // Exit loop after max attempts
                }

                // Optional: Add a short delay before retrying
                try {
                    Thread.sleep(1000); // 1-second delay before retry
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Retry interrupted during delete operation.", ie);
                }
            }
        }

        return isDeleted;
    }


    @Override
    public Notification get(int id, Function<Session, EntityGraph<Notification>> entityGraphFunction) {
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
    public List<Notification> getAll(Function<Session, EntityGraph<Notification>> entityGraphFunction) {
        return List.of();
    }

    public EntityGraph<Notification> createEntityGraph(Session session) {
        return null;
    }

    @Override
    public List<Notification> search(String keyword, Function<Session, EntityGraph<Notification>> entityGraphFunction) {
        return List.of();
    }
}
