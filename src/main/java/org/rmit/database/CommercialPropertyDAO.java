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

public class CommercialPropertyDAO extends DAOInterface<CommercialProperty> {

    @Override
    public boolean add(CommercialProperty commercialProperty) {
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            attempt++;
            try (Session session = DatabaseUtil.getSession()) {
                Transaction transaction = session.beginTransaction();
                session.merge(commercialProperty); // Merge the commercial property
                DatabaseUtil.clearAll(session);
                transaction.commit();
                DatabaseUtil.shutdown(session);
                return true; // Return if successful
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed: " + e.getMessage());

                if (attempt == MAX_ATTEMPTS) {
                    System.out.println("Max retries reached. Unable to add commercial property.");
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
    public boolean update(CommercialProperty commercialProperty) {
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            attempt++;
            try (Session session = DatabaseUtil.getSession()) {
                Transaction transaction = session.beginTransaction();
                session.merge(commercialProperty); // Merge the commercial property
                // session.update(commercialProperty); // Alternative update method
                DatabaseUtil.clearAll(session);
                transaction.commit();
                DatabaseUtil.shutdown(session);
                return true; // Return if successful
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed: " + e.getMessage());

                if (attempt == MAX_ATTEMPTS) {
                    System.out.println("Max retries reached. Unable to update commercial property.");
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
    public boolean delete(CommercialProperty commercialProperty) {
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            attempt++;
            try (Session session = DatabaseUtil.getSession()) {
                Transaction transaction = session.beginTransaction();
                CommercialProperty obj = session.merge(commercialProperty); // Merge the commercial property
                session.delete(obj); // Delete the object
                DatabaseUtil.clearAll(session);
                transaction.commit();
                DatabaseUtil.shutdown(session);
                return true; // Return if successful
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed: " + e.getMessage());

                if (attempt == MAX_ATTEMPTS) {
                    System.out.println("Max retries reached. Unable to delete commercial property.");
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
    public CommercialProperty get(int id, Function<Session, EntityGraph<CommercialProperty>> entityGraphFunction) {
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            attempt++;
            try (Session session = DatabaseUtil.getSession()) {
                String hql = String.format(GET_BY_ID_HQL, "CommercialProperty");
                CommercialProperty obj = session.createQuery(hql, CommercialProperty.class)
                        .setParameter("id", id)
                        .setHint("jakarta.persistence.fetchgraph", entityGraphFunction.apply(session))
                        .uniqueResult();
                DatabaseUtil.shutdown(session);
                return obj; // Return the result if successful
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed: " + e.getMessage());

                if (attempt == MAX_ATTEMPTS) {
                    System.out.println("Max retries reached. Unable to fetch commercial property.");
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
    public List<CommercialProperty> getAll(Function<Session, EntityGraph<CommercialProperty>> entityGraphFunction) {
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            attempt++;
            try (Session session = DatabaseUtil.getSession()) {
                String hql = String.format(GET_ALL_HQL, "CommercialProperty");
                List<CommercialProperty> list = session.createQuery(hql, CommercialProperty.class)
                        .setHint("jakarta.persistence.fetchgraph", entityGraphFunction.apply(session))  // Apply EntityGraph
                        .list();  // Fetch the list of CommercialProperties

                DatabaseUtil.shutdown(session);
                return list; // Return the list if successful
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed: " + e.getMessage());

                if (attempt == MAX_ATTEMPTS) {
                    System.out.println("Max retries reached. Unable to fetch commercial properties.");
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
    public List<CommercialProperty> search(String keyword, Function<Session, EntityGraph<CommercialProperty>> entityGraphFunction) {
        Session session = DatabaseUtil.getSession();
        List<CommercialProperty> result = Collections.emptyList();

        try {
            // Trim and clean the keyword
            String cleanKeyword = keyword.trim().toLowerCase();

            // First try exact ID match (faster)
            Long id = parseId(cleanKeyword);
            if (id != -1) {
                CommercialProperty property = get(Integer.parseInt(cleanKeyword), entityGraphFunction);
                if (property != null) {
                    result = Collections.singletonList(property);
                    return result;
                }
            }

            // If no ID match, then search by address
            String jpql = "SELECT DISTINCT  c FROM CommercialProperty c " +
                    "WHERE LOWER(c.address) LIKE :addressKeyword";

            result = session.createQuery(jpql, CommercialProperty.class)
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
