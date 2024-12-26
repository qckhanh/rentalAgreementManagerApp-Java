package org.rmit.database;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.Subgraph;
import org.hibernate.Session;
import org.rmit.model.Agreement.Payment;
import org.rmit.model.Agreement.RentalAgreement;
import org.rmit.model.Persons.Owner;
import org.rmit.model.Persons.Person;
import org.rmit.model.Property.Property;

import java.util.List;

public abstract class DAOInterface<T>{

    protected final String GET_ALL_HQL = "SELECT t FROM %s t";
    protected final String GET_BY_ID_HQL = "SELECT t FROM %s t WHERE t.id = :id";
    protected final String VALIDATE_LOGIN_HQL = "from %s where (username = :input or contact = :input) and password = :password";
    protected final String SEARCH_PROPERTY = "SELECT t FROM %s t WHERE CAST(t.id as String) LIKE :keyword";

    public abstract boolean add(T t);
    public abstract boolean update(T t);
    public abstract boolean delete(T t);
    public abstract T get(int id);
    public abstract List<T> getAll();
    public abstract T validateLogin(String usernameOrContact, String password);
    public abstract EntityGraph<T> createEntityGraph(Session session);
    public abstract List<T> search(String keyword);


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

        Subgraph<Owner> ownerSubgraph = graph.addSubgraph("owner"); // Assuming `owner` is the name of the relationship in Property
        ownerSubgraph.addAttributeNodes("name"); // Add only the name of the Owner
    }

    protected <T extends Person> void personSubgraph(Subgraph<T> graph){
        graph.addAttributeNodes("id", "name","dateOfBirth", "contact", "username", "password");
    }

    protected Long parseId(String keyword) {
        try {
            return Long.parseLong(keyword); // Try converting to Long for ID comparison
        } catch (NumberFormatException e) {
            return -1L; // Return an invalid ID if parsing fails
        }
    }

}
