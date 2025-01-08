package org.rmit.database;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.rmit.Helper.DatabaseUtil;
import org.rmit.model.Agreement.Payment;
import org.rmit.model.Agreement.RentalAgreement;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class PaymentDAO extends DAOInterface<Payment> {

    @Override
    public boolean add(Payment payment) {
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            attempt++;
            try (Session session = DatabaseUtil.getSession()) {
                Transaction transaction = session.beginTransaction();
                RentalAgreement rentalAgreement = session.merge(payment.getRentalAgreement());  // Merge RentalAgreement
                rentalAgreement.getPayments().add(payment);  // Add payment to the rental agreement
                session.merge(payment);  // Persist the payment itself
//            session.persist(payment);
                DatabaseUtil.clearAll(session);
                transaction.commit();
                DatabaseUtil.shutdown(session);
                return true; // Return if successful
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed: " + e);

                if (attempt == MAX_ATTEMPTS) {
                    System.out.println("Max retries reached. Unable to add payment.");
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
    public boolean update(Payment payment) {
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            attempt++;
            try (Session session = DatabaseUtil.getSession()) {
                Transaction transaction = session.beginTransaction();
                session.merge(payment);
                DatabaseUtil.clearAll(session);
                transaction.commit();
                DatabaseUtil.shutdown(session);
                return true; // Return if successful
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed: " + e);

                if (attempt == MAX_ATTEMPTS) {
                    System.out.println("Max retries reached. Unable to update payment.");
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
    public boolean delete(Payment payment) {
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            attempt++;
            try (Session session = DatabaseUtil.getSession()) {
                Transaction transaction = session.beginTransaction();
                session.delete(payment);
                DatabaseUtil.clearAll(session);
                transaction.commit();
                DatabaseUtil.shutdown(session);
                return true; // Return if successful
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed: " + e);

                if (attempt == MAX_ATTEMPTS) {
                    System.out.println("Max retries reached. Unable to delete payment.");
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
    public Payment get(int id, Function<Session, EntityGraph<Payment>> entityGraphFunction) {
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            attempt++;
            try (Session session = DatabaseUtil.getSession()) {
                String hql = String.format(GET_BY_ID_HQL, "Payment");
                Payment obj = session.createQuery(hql, Payment.class)
                        .setParameter("id", id)
                        .setHint("jakarta.persistence.fetchgraph", entityGraphFunction.apply(session))
                        .uniqueResult();
                DatabaseUtil.shutdown(session);
                return obj; // Return the result if successful
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed: " + e);

                if (attempt == MAX_ATTEMPTS) {
                    System.out.println("Max retries reached. Unable to get payment.");
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
    public List<Payment> getAll(Function<Session, EntityGraph<Payment>> entityGraphFunction) {
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            attempt++;
            try (Session session = DatabaseUtil.getSession()) {
                String hql = String.format(GET_ALL_HQL, "Payment");
                List<Payment> list = session.createQuery(hql, Payment.class)
                        .setHint("jakarta.persistence.fetchgraph", entityGraphFunction.apply(session))  // Apply EntityGraph
                        .list();  // Fetch the list of Payments
                DatabaseUtil.shutdown(session);
                return list; // Return the list if successful
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed: " + e);

                if (attempt == MAX_ATTEMPTS) {
                    System.out.println("Max retries reached. Unable to get all payments.");
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


    public EntityGraph<Payment> createEntityGraph(Session session) {
        EntityManager emf = session.unwrap(EntityManager.class);
        EntityGraph<Payment> entityGraph = emf.createEntityGraph(Payment.class);
        return entityGraph;
    }

    @Override
    public List<Payment> search(String keyword, Function<Session, EntityGraph<Payment>> entityGraphFunction) {
        return null;
    }


}
