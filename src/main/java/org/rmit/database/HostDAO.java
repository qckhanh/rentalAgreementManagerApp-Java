package org.rmit.database;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.rmit.Helper.DatabaseUtil;
import org.rmit.Helper.EntityGraphUtils;
import org.rmit.model.Persons.Host;


import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class HostDAO extends DAOInterface<Host> implements ValidateLoginDAO<Host> {

    @Override
    public boolean add(Host host) {
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            attempt++;
            try (Session session = DatabaseUtil.getSession()) {

                Transaction transaction = DatabaseUtil.getTransaction(session);
                session.persist(host);
                DatabaseUtil.clearAll(session);
                transaction.commit();
                DatabaseUtil.shutdown(session);

                return true; // Return if successful
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Attempt " + attempt + " failed: " + e);

                if (attempt == MAX_ATTEMPTS) {
                    System.out.println("Max retries reached. Unable to add host.");
                    return false; // Return false if all retries fail
                }

                // Optional: Add a delay before retrying
                try {
                    Thread.sleep(1000); // 1 second delay
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return false; //Lets pray we dont reach here!
    }


    @Override
    public boolean update(Host host) {
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            attempt++;
            try (Session session = DatabaseUtil.getSession()) {
                Transaction transaction = session.beginTransaction();

                session.merge(host);
                DatabaseUtil.clearAll(session);

                transaction.commit();
                DatabaseUtil.shutdown(session);
                return true; // Return if successful
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed: " + e);

                if (attempt == MAX_ATTEMPTS) {
                    System.out.println("Max retries reached. Unable to update host.");
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
    public boolean delete(Host host) {
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            attempt++;
            try (Session session = DatabaseUtil.getSession()) {

                Transaction transaction = session.beginTransaction();
                session.delete(host);
                DatabaseUtil.clearAll(session);
                transaction.commit();
                DatabaseUtil.shutdown(session);

                return true; // Return if successful
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed: " + e);

                if (attempt == MAX_ATTEMPTS) {
                    System.out.println("Max retries reached. Unable to delete host.");
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
    public Host get(int id, Function<Session, EntityGraph<Host>> entityGraphFunction) {
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            attempt++;
            try (Session session = DatabaseUtil.getSession()) {
                String hql = String.format(GET_BY_ID_HQL, "Host");
                Host obj = session.createQuery(hql, Host.class)
                        .setParameter("id", id)
                        .setHint("jakarta.persistence.fetchgraph", entityGraphFunction.apply(session))
                        .uniqueResult();
                DatabaseUtil.shutdown(session);
                return obj; // Return the result if successful
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed: " + e);

                if (attempt == MAX_ATTEMPTS) {
                    System.out.println("Max retries reached. Unable to get host.");
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
        return null; // lets pray we dont reach here!
    }


    @Override
    public List<Host> getAll(Function<Session, EntityGraph<Host>> entityGraphFunction) {
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            attempt++;
            try (Session session = DatabaseUtil.getSession()) {
                String hql = String.format(GET_ALL_HQL, "Host");
                List<Host> list = session.createQuery(hql, Host.class)
                        .setHint("jakarta.persistence.fetchgraph", entityGraphFunction.apply(session))  // Apply EntityGraph
                        .list();  // Fetch the list of Hosts
                DatabaseUtil.shutdown(session);
                return list; // Return the list if successful
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed: " + e);

                if (attempt == MAX_ATTEMPTS) {
                    System.out.println("Max retries reached. Unable to get all hosts.");
                    return Collections.emptyList(); // Return an empty list if all retries fail
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


    // Custom Exception class
    public class DatabaseException extends Exception {
        public DatabaseException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    @Override
    public Host validateLogin(String usernameOrContact, String password) {
        try {
            Session session = DatabaseUtil.getSession();
            String hql = String.format(VALIDATE_LOGIN_HQL, "Host");
            Host user = session.createQuery(hql, Host.class)
                    .setParameter("input", usernameOrContact)
                    .setParameter("password", password)
                    .setHint("javax.persistence.fetchgraph", EntityGraphUtils.HostFULL(session))
                    .uniqueResult();
            DatabaseUtil.shutdown(session);
            return user;
        } catch (Exception e) {
            System.out.println("Error: not found user");
            return null;
        }
    }

    @Override
    public List<Host> search(String keyword, Function<Session, EntityGraph<Host>> entityGraphFunction) {
        Session session = DatabaseUtil.getSession();
        List<Host> result = Collections.emptyList(); // Properly initialized

        try {
            // JPQL to search by address (partial match) or ID (exact match)
            String jpql = "SELECT c FROM Host c " +
                    "WHERE LOWER(c.name) LIKE :nameKeyword " +
                    "OR c.id = :idKeyword " +
                    "OR LOWER(c.username) LIKE :usernameKeyword";

            result = session.createQuery(jpql, Host.class)
                    .setMaxResults(10) // Limit results
                    .setParameter("nameKeyword", "%" + keyword.toLowerCase() + "%")
                    .setParameter("idKeyword", parseId(keyword))
                    .setParameter("usernameKeyword", "%" + keyword.toLowerCase() + "%")
                    .setHint("jakarta.persistence.fetchgraph", entityGraphFunction.apply(session))
                    .list();
            DatabaseUtil.shutdown(session);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return result;
    }

}
