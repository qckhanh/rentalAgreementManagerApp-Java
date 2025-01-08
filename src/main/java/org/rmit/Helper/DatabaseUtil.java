package org.rmit.Helper;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class DatabaseUtil {
    private static SessionFactory sessionFactory;
    private static Session currentSession;

    private DatabaseUtil(){
        sessionFactory = buildSessionFactory();
    }

    private static SessionFactory buildSessionFactory() {
        try {
            return new org.hibernate.cfg.Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            System.out.println("Initial SessionFactory creation failed." + ex);
            return null;
        }
    }

    public static Session getSession() {
        if(sessionFactory == null) {
            sessionFactory = buildSessionFactory();
        }
        currentSession = sessionFactory.openSession();
        return currentSession;
    }

    public static Transaction getTransaction(Session session) {
        return session.beginTransaction();
    }

    public static void shutdown(Session session) {
        if(session != null) {
            session.close();
            System.out.println("Session closed");
        }
    }

    public static void clearAll(Session session){
        session.flush();
        session.clear();
    }

    public static void warmUp() {
        System.out.println("Warming up the database...");
        try (org.hibernate.Session session = DatabaseUtil.getSession()) {
            // Run a simple query to check if the database is responsive
            session.clear();
            session.flush();
            session.beginTransaction();
            session.createNativeQuery("SELECT 1").getSingleResult();  // Simple query to test connection
            session.getTransaction().commit();  // Commit to confirm transaction is successful

            // Add a 1-second delay
            Thread.sleep(1000);

            System.out.println("Database connection is ready.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Warm-up was interrupted", e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Database connection failed during warm-up", e);
        }
    }

    public static Session getCurrentSession() {
        return currentSession;
    }
}
