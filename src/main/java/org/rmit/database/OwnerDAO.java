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
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            attempt++;
            try (Session session = DatabaseUtil.getSession()) {
                Transaction transaction = session.beginTransaction();
                session.persist(owner);
                DatabaseUtil.clearAll(session);
                transaction.commit();
                DatabaseUtil.shutdown(session);
                return true; // Return if successful
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed: " + e);

                if (attempt == MAX_ATTEMPTS) {
                    System.out.println("Max retries reached. Unable to add owner.");
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
    public boolean update(Owner owner) {
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            attempt++;
            try (Session session = DatabaseUtil.getSession()) {
                Transaction transaction = DatabaseUtil.getTransaction(session);
                session.merge(owner);
                DatabaseUtil.clearAll(session);
                transaction.commit();
                DatabaseUtil.shutdown(session);
                return true; // Return if successful
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed: " + e);

                if (attempt == MAX_ATTEMPTS) {
                    System.out.println("Max retries reached. Unable to update owner.");
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
    public boolean delete(Owner owner) {
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            attempt++;
            try (Session session = DatabaseUtil.getSession()) {
                Transaction transaction = DatabaseUtil.getTransaction(session);
                session.delete(owner);
                DatabaseUtil.clearAll(session);
                transaction.commit();
                DatabaseUtil.shutdown(session);
                return true; // Return if successful
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed: " + e);

                if (attempt == MAX_ATTEMPTS) {
                    System.out.println("Max retries reached. Unable to delete owner.");
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
    public Owner get(int id, Function<Session, EntityGraph<Owner>> entityGraphFunction) {
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            attempt++;
            try (Session session = DatabaseUtil.getSession()) {
                String hql = String.format(GET_BY_ID_HQL, "Owner");
                Owner obj = session.createQuery(hql, Owner.class)
                        .setParameter("id", id)
                        .setHint("jakarta.persistence.fetchgraph", entityGraphFunction.apply(session))
                        .uniqueResult();
                DatabaseUtil.shutdown(session);
                return obj; // Return the result if successful
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed: " + e);

                if (attempt == MAX_ATTEMPTS) {
                    System.out.println("Max retries reached. Unable to get owner.");
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
    public List<Owner> getAll(Function<Session, EntityGraph<Owner>> entityGraphFunction) {
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            attempt++;
            try (Session session = DatabaseUtil.getSession()) {
                String hql = String.format(GET_ALL_HQL, "Owner");
                List<Owner> list = session.createQuery(hql, Owner.class)
                        .setHint("jakarta.persistence.fetchgraph", entityGraphFunction.apply(session))  // Apply EntityGraph
                        .list();  // Fetch the list of Owners
                DatabaseUtil.shutdown(session);
                return list; // Return the list if successful
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed: " + e);

                if (attempt == MAX_ATTEMPTS) {
                    System.out.println("Max retries reached. Unable to get all owners.");
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
