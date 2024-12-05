package org.rmit;

import org.rmit.entity.Person;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class MainTest {
    public static void main(String[] args) {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        configuration.addAnnotatedClass(Person.class);

        try (SessionFactory sessionFactory = configuration.buildSessionFactory()) {
            try(Session session = sessionFactory.openSession()) {
                Transaction transaction = session.beginTransaction();

                try {
                    Person person = new Person("asdas", 34);

                    session.persist(person);

                    transaction.commit();

                    System.out.println("added person with id" + person.getId());
                }
                catch (Exception e) {
                    if (transaction != null) {
                        transaction.rollback();
                    }
                    System.out.println("Error occurred");
                }
            }
        }
    }
}