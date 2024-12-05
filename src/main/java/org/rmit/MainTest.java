package org.rmit;

import org.rmit.entity.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.rmit.entity.Renter;

public class MainTest {
    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        configuration.addAnnotatedClass(Person.class);

        try (SessionFactory sessionFactory = configuration.buildSessionFactory()) {
            try(Session session = sessionFactory.openSession()) {
                Transaction transaction = session.beginTransaction();

                try {
//                    Person person = new Person("asasdasdasasdass", 34);

                    Renter renter = new Renter("asdasd", 34, 3, 4);
                    session.persist(renter);
//                    session.persist(person);

                    transaction.commit();

                }
                catch (Exception e) {
                    if (transaction != null) {
                        transaction.rollback();
                    }
                    e.printStackTrace();
                }
            }
        }
    }
}