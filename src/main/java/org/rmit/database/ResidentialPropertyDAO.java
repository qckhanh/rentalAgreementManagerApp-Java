package org.rmit.database;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Subgraph;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.rmit.Helper.DatabaseUtil;
import org.rmit.model.Persons.Person;
import org.rmit.model.Property.CommercialProperty;
import org.rmit.model.Property.ResidentialProperty;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class ResidentialPropertyDAO extends DAOInterface<ResidentialProperty> {

    @Override
    public boolean add(ResidentialProperty residentialProperty) {
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            attempt++;
            try (Session session = DatabaseUtil.getSession()) {
                Transaction transaction = DatabaseUtil.getTransaction(session);
                session.merge(residentialProperty); // Merge the residential property
                DatabaseUtil.clearAll(session);
                transaction.commit();
                DatabaseUtil.shutdown(session);
                return true; // Return true if the operation is successful
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed: " + e.getMessage());

                if (attempt == MAX_ATTEMPTS) {
                    System.out.println("Max retries reached. Unable to add residential property.");
                    return false; // Return false if all retries fail
                }

                // Optional: Add a delay before retrying
                try {
                    Thread.sleep(1000); // 1-second delay before retrying
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return false; // Fallback return (should not reach here)
    }


    @Override
    public boolean update(ResidentialProperty residentialProperty) {
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            attempt++;
            try (Session session = DatabaseUtil.getSession()) {
                Transaction transaction = DatabaseUtil.getTransaction(session);
                session.merge(residentialProperty); // Merge the residential property
                DatabaseUtil.clearAll(session);
                transaction.commit();
                DatabaseUtil.shutdown(session);
                return true; // Return true if the update is successful
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed: " + e.getMessage());

                if (attempt == MAX_ATTEMPTS) {
                    System.out.println("Max retries reached. Unable to update residential property.");
                    return false; // Return false if all retries fail
                }

                // Optional: Add a delay before retrying
                try {
                    Thread.sleep(1000); // 1-second delay before retrying
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return false; // Fallback return (should not reach here)
    }


    @Override
    public boolean delete(ResidentialProperty residentialProperty) {
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            attempt++;
            try (Session session = DatabaseUtil.getSession()) {
                Transaction transaction = DatabaseUtil.getTransaction(session);
                ResidentialProperty obj = session.merge(residentialProperty); // Merge the residential property
                session.delete(obj); // Delete the residential property
                DatabaseUtil.clearAll(session);
                transaction.commit();
                return true; // Return true if deletion is successful
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed: " + e.getMessage());

                if (attempt == MAX_ATTEMPTS) {
                    System.out.println("Max retries reached. Unable to delete residential property.");
                    return false; // Return false if all retries fail
                }

                // Optional: Add a delay before retrying
                try {
                    Thread.sleep(1000); // 1-second delay before retrying
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return false; // Fallback return (should not reach here)
    }


    @Override
    public ResidentialProperty get(int id, Function<Session, EntityGraph<ResidentialProperty>> entityGraphFunction) {
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            attempt++;
            try (Session session = DatabaseUtil.getSession()) {
                String hql = String.format(GET_BY_ID_HQL, "ResidentialProperty");
                ResidentialProperty obj = session.createQuery(hql, ResidentialProperty.class)
                        .setParameter("id", id)
                        .setHint("jakarta.persistence.fetchgraph", entityGraphFunction.apply(session))
                        .uniqueResult();
                DatabaseUtil.shutdown(session);
                return obj; // Return the object if retrieval is successful
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed: " + e.getMessage());

                if (attempt == MAX_ATTEMPTS) {
                    System.out.println("Max retries reached. Unable to fetch residential property.");
                    return null; // Return null if all retries fail
                }

                // Optional: Add a delay before retrying
                try {
                    Thread.sleep(1000); // 1-second delay before retrying
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return null; // Fallback return (should not reach here)
    }


    @Override
    public List<ResidentialProperty> getAll(Function<Session, EntityGraph<ResidentialProperty>> entityGraphFunction) {
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            attempt++;
            try (Session session = DatabaseUtil.getSession()) {
                String hql = String.format(GET_ALL_HQL, "ResidentialProperty");
                List<ResidentialProperty> list = session.createQuery(hql, ResidentialProperty.class)
                        .setHint("jakarta.persistence.fetchgraph", entityGraphFunction.apply(session))  // Apply EntityGraph
                        .list();  // Fetch the list of ResidentialProperties
                DatabaseUtil.shutdown(session);
                return list; // Return the list if successful
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed: " + e.getMessage());

                if (attempt == MAX_ATTEMPTS) {
                    System.out.println("Max retries reached. Unable to fetch list of residential properties.");
                    return Collections.emptyList(); // Return empty list if all retries fail
                }

                // Optional: Add a delay before retrying
                try {
                    Thread.sleep(1000); // 1-second delay before retrying
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return Collections.emptyList(); // Fallback return (should not reach here)
    }

    @Override
    public List<ResidentialProperty> search(String keyword, Function<Session, EntityGraph<ResidentialProperty>> entityGraphFunction) {
        Session session = DatabaseUtil.getSession();
        List<ResidentialProperty> result = Collections.emptyList();

        try {
            String cleanKeyword = keyword.trim().toLowerCase();

            // First try exact ID match (faster)
            Long id = parseId(cleanKeyword);
            if (id != -1) {
                ResidentialProperty property = get(Integer.parseInt(cleanKeyword), entityGraphFunction);
                if (property != null) {
                    result = Collections.singletonList(property);
                    return result;
                }
            }

            // If no ID match, then search by address
            String jpql = "SELECT DISTINCT c FROM ResidentialProperty c " +
                    "WHERE LOWER(c.address) LIKE :addressKeyword";

            result = session.createQuery(jpql, ResidentialProperty.class)
                    .setMaxResults(5)
                    .setParameter("addressKeyword", "%" + cleanKeyword + "%")
                    .setHint("jakarta.persistence.fetchgraph", entityGraphFunction.apply(session))
                    .list();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseUtil.shutdown(session);
        }
        return result;
    }


}
