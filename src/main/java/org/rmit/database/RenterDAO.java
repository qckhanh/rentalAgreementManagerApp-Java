package org.rmit.database;

import jakarta.persistence.*;
import javafx.scene.chart.PieChart;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.rmit.Helper.DatabaseUtil;
import org.rmit.Helper.EntityGraphUtils;
import org.rmit.model.Persons.Renter;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

public class RenterDAO extends DAOInterface<Renter> implements ValidateLoginDAO<Renter>, ImagesDAO<Renter> {

    @Override
    public boolean add(Renter renter) {
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            attempt++;
            try (Session session = DatabaseUtil.getSession()) {
                Transaction transaction = DatabaseUtil.getTransaction(session);
                session.persist(renter);
                DatabaseUtil.clearAll(session);
                transaction.commit();
                DatabaseUtil.shutdown(session);
                return true; // Return if successful
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed: " + e.getMessage());

                if (attempt == MAX_ATTEMPTS) {
                    System.out.println("Max retries reached. Unable to add renter.");
                    return false; // Return false if all retries fail
                }

                // Optional: Add a delay before retrying
                try {
                    Thread.sleep(1000); // 1-second delay
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return false; // Fallback return (should not reach here)
    }


    @Override
    public boolean update(Renter renter) {
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            attempt++;
            try (Session session = DatabaseUtil.getSession()) {
                Transaction transaction = DatabaseUtil.getTransaction(session);
                session.merge(renter);
                DatabaseUtil.clearAll(session);
                transaction.commit();
                DatabaseUtil.shutdown(session);
                return true; // Return if successful
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed: " + e.getMessage());

                if (attempt == MAX_ATTEMPTS) {
                    System.out.println("Max retries reached. Unable to update renter.");
                    return false; // Return false if all retries fail
                }

                // Optional: Add a delay before retrying
                try {
                    Thread.sleep(1000); // 1-second delay
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return false; // Fallback return (should not reach here)
    }


    @Override
    public boolean delete(Renter renter) {
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            attempt++;
            try (Session session = DatabaseUtil.getSession()) {
                Transaction transaction = DatabaseUtil.getTransaction(session);
                session.delete(renter);
                DatabaseUtil.clearAll(session);
                transaction.commit();
                DatabaseUtil.shutdown(session);
                return true; // Return if successful
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed: " + e.getMessage());

                if (attempt == MAX_ATTEMPTS) {
                    System.out.println("Max retries reached. Unable to delete renter.");
                    return false; // Return false if all retries fail
                }

                // Optional: Add a delay before retrying
                try {
                    Thread.sleep(1000); // 1-second delay
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return false; // Fallback return (should not reach here)
    }


    @Override
    public Renter get(int id, Function<Session, EntityGraph<Renter>> sessionEntityGraphFunction) {
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            attempt++;
            try (Session session = DatabaseUtil.getSession()) {
                String hql = String.format(GET_BY_ID_HQL, "Renter");
                Renter obj = session.createQuery(hql, Renter.class)
                        .setParameter("id", id)
                        .setHint("jakarta.persistence.fetchgraph", sessionEntityGraphFunction.apply(session))
                        .uniqueResult();
                DatabaseUtil.shutdown(session);
                return obj; // Return the object if successful
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed: " + e.getMessage());

                if (attempt == MAX_ATTEMPTS) {
                    System.out.println("Max retries reached. Unable to get renter.");
                    return null; // Return null if all retries fail
                }

                // Optional: Add a delay before retrying
                try {
                    Thread.sleep(1000); // 1-second delay
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return null; // Fallback return (should not reach here)
    }


    @Override
    public List<Renter> getAll(Function<Session, EntityGraph<Renter>> sessionEntityGraphFunction) {
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            attempt++;
            try (Session session = DatabaseUtil.getSession()) {
                String hql = String.format(GET_ALL_HQL, "Renter");
                List<Renter> list = session.createQuery(hql, Renter.class)
                        .setHint("jakarta.persistence.fetchgraph", sessionEntityGraphFunction.apply(session))  // Apply EntityGraph
                        .list();  // Fetch the list of Renters
                DatabaseUtil.shutdown(session);
                return list; // Return the list if successful
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed: " + e.getMessage());

                if (attempt == MAX_ATTEMPTS) {
                    System.out.println("Max retries reached. Unable to fetch renters.");
                    return Collections.emptyList(); // Return empty list if all retries fail
                }

                // Optional: Add a delay before retrying
                try {
                    Thread.sleep(1000); // 1-second delay
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return Collections.emptyList(); // Fallback return (should not reach here)
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
                    .setHint("javax.persistence.fetchgraph", EntityGraphUtils.RenterFULL(session))
                    .uniqueResult();
            DatabaseUtil.shutdown(session);
            return user;
        } catch (Exception e) {
            System.out.println("Error: not found user");
            return null;
        }
    }


    @Override
    public List<Renter> search(String keyword, Function<Session, EntityGraph<Renter>> sessionEntityGraphFunction) {
        List<Renter> list = Collections.emptyList();
        try{
            Session session = DatabaseUtil.getSession();
            Transaction transaction = DatabaseUtil.getTransaction(session);
            String HQL = "From Renter r where LOWER(r.name) like :nameKeyword or r.id = :idKeyword";
            list = session.createQuery(HQL, Renter.class)
                    .setParameter("nameKeyword", "%" + keyword.toLowerCase() + "%")
                    .setParameter("idKeyword", parseId(keyword))
                    .setHint("jakarta.persistence.fetchgraph", sessionEntityGraphFunction.apply(session))
                    .list();
            DatabaseUtil.shutdown(session);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return list;
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
