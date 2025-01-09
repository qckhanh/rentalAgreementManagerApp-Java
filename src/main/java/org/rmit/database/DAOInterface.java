package org.rmit.database;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.Subgraph;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.rmit.Helper.DatabaseUtil;
import org.rmit.Notification.Notification;
import org.rmit.Notification.Request;
import org.rmit.model.Agreement.Payment;
import org.rmit.model.Agreement.RentalAgreement;
import org.rmit.model.Persons.Owner;
import org.rmit.model.Persons.Person;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Property.Property;

import java.util.List;
import java.util.function.Function;

public abstract class DAOInterface<T>{

    protected final String GET_ALL_HQL = "SELECT t FROM %s t";
    protected final String GET_BY_ID_HQL = "SELECT t FROM %s t WHERE t.id = :id";
    protected final String VALIDATE_LOGIN_HQL = "from %s where (username = :input or contact = :input) and password = :password";
    protected final String SEARCH_PROPERTY = "SELECT t FROM %s t WHERE CAST(t.id as String) LIKE :keyword";
    protected final int MAX_ATTEMPTS = 3;


    public abstract boolean add(T t);
    public abstract boolean update(T t);
    public abstract boolean delete(T t);
    public abstract T get(int id, Function<Session, EntityGraph<T>> entityGraphFunction);
    public abstract List<T> getAll(Function<Session, EntityGraph<T>> entityGraphFunction);
    public abstract List<T> search(String keyword, Function<Session, EntityGraph<T>> entityGraphFunction);

    //Helper methods
    public static boolean isValidUsername(Class<? extends Person> clazz,  String username){
        Session session = DatabaseUtil.getSession();
        String hql = "SELECT 1 FROM " + clazz.getSimpleName()  + " u WHERE u.username = :username";
        Query<Integer> query = session.createQuery(hql, Integer.class);
        query.setParameter("username", username);
        query.setMaxResults(1);
        List<Integer> result = query.list();
        return result.isEmpty();
    }
    public static boolean isValidContact(Class<? extends Person> clazz,  String contact){
        Session session = DatabaseUtil.getSession();
        String hql = "SELECT 1 FROM " + clazz.getSimpleName()  + " u WHERE u.contact = :contact";
        Query<Integer> query = session.createQuery(hql, Integer.class);
        query.setParameter("username", contact);
        query.setMaxResults(1);
        List<Integer> result = query.list();
        return result.isEmpty();
    }
    public static Notification getNotification(int id){
        Session  session = DatabaseUtil.getSession();
        Transaction transaction = DatabaseUtil.getTransaction(session);
        Notification notification = session.get(Notification.class, id);
        DatabaseUtil.shutdown(session);
        return notification;
    }

    // Subgraph methods
    protected void paymentGraph(Subgraph<Payment> graph){
        graph.addAttributeNodes("paymentId", "amount", "date", "paymentMethod");
        personSubgraph(graph.addSubgraph("mainRenter"));
        propertySubgraph(graph.addSubgraph("property"));
        rentalAgreementSubgraph(graph.addSubgraph("rentalAgreement"));
    }

    protected void rentalAgreementSubgraph(Subgraph<RentalAgreement> graph){
        graph.addAttributeNodes("id", "period", "contractDate", "rentingFee", "status");

        personSubgraph(graph.addSubgraph("mainTenant"));
        personSubgraph(graph.addSubgraph("host"));
        personSubgraph(graph.addSubgraph("subTenants"));
        propertySubgraph(graph.addSubgraph("property"));
    }

    protected void propertySubgraph(Subgraph<Property> graph ){
        graph.addAttributeNodes("address", "price", "type", "id");

//        Subgraph<Owner> ownerSubgraph = graph.addSubgraph("owner"); // Assuming `owner` is the name of the relationship in Property
//        ownerSubgraph.addAttributeNodes("name"); // Add only the name of the Owner

        personSubgraph(graph.addSubgraph("owner"));
    }

    protected void notificationGraph(Subgraph<Notification> graph){
        graph.addAttributeNodes("id", "content", "timestamp", "header");
        minPersonSubgraph(graph.addSubgraph("sender"));
        minPersonSubgraph(graph.addSubgraph("receivers"));
    }

    protected void requestGraph(Subgraph<Request> graph){
        graph.addAttributeNodes("draftObject", "approvalStatus");
    }

    protected void minPersonSubgraph(Subgraph<Person> graph){
        graph.addAttributeNodes("id", "name", "contact");
    }

    protected <T extends Person> void personSubgraph(Subgraph<T> graph){
        graph.addAttributeNodes("id", "name","dateOfBirth", "contact", "username", "password");
//        notificationGraph(graph.addSubgraph("sentNotifications"));
//        notificationGraph(graph.addSubgraph("receivedNotifications"));
    }

    protected Long parseId(String keyword) {
        try {
            return Long.parseLong(keyword); // Try converting to Long for ID comparison
        } catch (NumberFormatException e) {
            return -1L; // Return an invalid ID if parsing fails
        }
    }

    public static boolean isUsernameExists(String clazzName, String username) {
        Session session = DatabaseUtil.getSession();
        String hql = "SELECT 1 FROM " + clazzName + " u WHERE u.username = :username";
        // Check if a match exists using setMaxResults(1)
        Object result = session.createQuery(hql)
                .setParameter("username", username)
                .setMaxResults(1) // Limit the query to a single result
                .uniqueResult(); // Retrieve a single result or null
        return result != null; // If result is not null, the username exists
    }
}
