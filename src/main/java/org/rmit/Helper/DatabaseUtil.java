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

    public static Session getCurrentSession() {
        return currentSession;
    }
}
