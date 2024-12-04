package database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory(){
        try{
            return new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

        }
        catch (Exception e){
            System.out.println("Initial SessionFactory creation failed." + e);
            return null;
        }
    }

    public static SessionFactory getSessionFactory(){
        return sessionFactory;
    }

    public static void shutdown(Session session){
        if(session != null) session.close();
    }
}
