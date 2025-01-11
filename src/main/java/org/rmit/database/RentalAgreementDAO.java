package org.rmit.database;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.rmit.Helper.DatabaseUtil;
import org.rmit.Notification.Notification;
import org.rmit.model.Agreement.RentalAgreement;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class RentalAgreementDAO extends DAOInterface<RentalAgreement>{
    @Override
    public boolean add(RentalAgreement rentalAgreement) {
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            attempt++;
            try (Session session = DatabaseUtil.getSession()) {
                Transaction transaction = DatabaseUtil.getTransaction(session);
                session.persist(rentalAgreement);
                DatabaseUtil.clearAll(session);
                transaction.commit();
                DatabaseUtil.shutdown(session);
                return true; // Return if successful
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed: " + e.getMessage());

                if (attempt == MAX_ATTEMPTS) {
                    System.out.println("Max retries reached. Unable to add rental agreement.");
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
    public boolean update(RentalAgreement rentalAgreement) {
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            attempt++;
            try (Session session = DatabaseUtil.getSession()) {
                Transaction transaction = DatabaseUtil.getTransaction(session);
                session.merge(rentalAgreement);
                DatabaseUtil.clearAll(session);
                transaction.commit();
                DatabaseUtil.shutdown(session);
                return true; // Return if successful
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed: " + e.getMessage());

                if (attempt == MAX_ATTEMPTS) {
                    System.out.println("Max retries reached. Unable to update rental agreement.");
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
    public boolean delete(RentalAgreement rentalAgreement) {
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            attempt++;
//            delete from host_rentalagreement where rentalagreements_agreementid = 1002;
//            delete from payment where agreement_id = 1002;
//            delete from rental_agreement_subtenants where agreement_id = 1002;
//            delete from rentalagreement where agreementid = 1002;
            try (Session session = DatabaseUtil.getSession()) {
                Transaction transaction = DatabaseUtil.getTransaction(session);
                String deleteHostRentalAgreementQuery = "DELETE FROM host_rentalagreement WHERE rentalagreements_agreementid = :agreementId";
                session.createNativeQuery(deleteHostRentalAgreementQuery)
                        .setParameter("agreementId", rentalAgreement.getAgreementId())
                        .executeUpdate();
                String deletePaymentQuery = "DELETE FROM payment WHERE agreement_id = :agreementId";
                session.createNativeQuery(deletePaymentQuery)
                        .setParameter("agreementId", rentalAgreement.getAgreementId())
                        .executeUpdate();
                String deleteRentalAgreementSubtenantsQuery = "DELETE FROM rental_agreement_subtenants WHERE agreement_id = :agreementId";
                session.createNativeQuery(deleteRentalAgreementSubtenantsQuery)
                        .setParameter("agreementId", rentalAgreement.getAgreementId())
                        .executeUpdate();
                String deleteRentalAgreementQuery = "DELETE FROM rentalagreement WHERE agreementid = :agreementId";
                session.createNativeQuery(deleteRentalAgreementQuery)
                        .setParameter("agreementId", rentalAgreement.getAgreementId())
                        .executeUpdate();
                transaction.commit();
                return true;
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed: " + e.getMessage());

                if (attempt == MAX_ATTEMPTS) {
                    System.out.println("Max retries reached. Unable to delete rental agreement.");
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
    public RentalAgreement get(int id, Function<Session, EntityGraph<RentalAgreement>> entityGraphFunction) {
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            attempt++;
            try (Session session = DatabaseUtil.getSession()) {
                String hql = String.format(GET_BY_ID_HQL, "RentalAgreement");
                RentalAgreement obj = session.createQuery(hql, RentalAgreement.class)
                        .setParameter("id", id)
                        .setHint("jakarta.persistence.fetchgraph", entityGraphFunction.apply(session))
                        .uniqueResult();
                DatabaseUtil.shutdown(session);
                return obj; // Return the result if successful
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed: " + e.getMessage());

                if (attempt == MAX_ATTEMPTS) {
                    System.out.println("Max retries reached. Unable to get rental agreement.");
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
    public List<RentalAgreement> getAll(Function<Session, EntityGraph<RentalAgreement>> sessionEntityGraphFunction) {
        int attempt = 0;
        while (attempt < MAX_ATTEMPTS) {
            attempt++;
            try (Session session = DatabaseUtil.getSession()) {
                String hql = String.format(GET_ALL_HQL, "RentalAgreement");
                List<RentalAgreement> list = session.createQuery(hql, RentalAgreement.class)
                        .setHint("jakarta.persistence.fetchgraph", sessionEntityGraphFunction.apply(session))  // Apply EntityGraph
                        .list();  // Fetch the list of Rental Agreements
                DatabaseUtil.shutdown(session);
                return list; // Return the list if successful
            } catch (Exception e) {
                System.out.println("Attempt " + attempt + " failed: " + e.getMessage());

                if (attempt == MAX_ATTEMPTS) {
                    System.out.println("Max retries reached. Unable to get all rental agreements.");
                    return Collections.emptyList(); // Return an empty list if all retries fail
                }

                // Optional: Add a delay before retrying
                try {
                    Thread.sleep(1000); // 1-second delay
                } catch (InterruptedException ie) {
//                    Thread.currentThread().interrupt();
                }
            }
        }
        return Collections.emptyList(); // Fallback return (should not reach here)
    }


    @Override
    public List<RentalAgreement> search(String keyword, Function<Session, EntityGraph<RentalAgreement>> entityGraphFunction) {
        return null;
    }

}
