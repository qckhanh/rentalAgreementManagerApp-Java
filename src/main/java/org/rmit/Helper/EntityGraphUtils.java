package org.rmit.Helper;

import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Subgraph;
import org.hibernate.Session;
import org.rmit.Notification.Notification;
import org.rmit.model.Agreement.Payment;
import org.rmit.model.Agreement.RentalAgreement;
import org.rmit.model.Persons.*;
import org.rmit.model.Property.CommercialProperty;
import org.rmit.model.Property.Property;
import org.rmit.model.Property.ResidentialProperty;

public class EntityGraphUtils {
    //for searching
    public static EntityGraph<CommercialProperty> commercialPropertyForSearching(Session session){

        EntityManager emf = session.unwrap(EntityManager.class);
        EntityGraph<CommercialProperty> entityGraph = emf.createEntityGraph(CommercialProperty.class);
        entityGraph.addAttributeNodes("address", "price", "type", "id", "businessType", "totalParkingSpace", "squareMeters", "images"); // Add only the name of the Owner

        Subgraph<Owner> ownerSubgraph = entityGraph.addSubgraph("owner");
        ownerSubgraph.addAttributeNodes("id", "name");
        ownerSubgraph.addSubgraph("receivedNotifications");


        Subgraph<Host> hostSubgraph = entityGraph.addSubgraph("hosts");
        hostSubgraph.addAttributeNodes("id", "name");
        hostSubgraph.addSubgraph("receivedNotifications");

        return entityGraph;
    }

    public static EntityGraph<ResidentialProperty> residentalPropertyForSearching(Session session){

        EntityManager emf = session.unwrap(EntityManager.class);
        EntityGraph<ResidentialProperty> entityGraph = emf.createEntityGraph(ResidentialProperty.class);
        entityGraph.addAttributeNodes("address", "price", "type", "id", "totalRoom", "totalBedroom", "isPetAllowed","hasGarden", "images"); // Add only the name of the Owner

        Subgraph<Owner> ownerSubgraph = entityGraph.addSubgraph("owner");
        ownerSubgraph.addAttributeNodes("id", "name");
        ownerSubgraph.addSubgraph("receivedNotifications");

        Subgraph<Host> hostSubgraph = entityGraph.addSubgraph("hosts");
        hostSubgraph.addAttributeNodes("id", "name");
        hostSubgraph.addSubgraph("receivedNotifications");


        return entityGraph;
    }

    public static EntityGraph<Owner> ownerForSearching(Session session) {

        EntityManager emf = session.unwrap(EntityManager.class);
        EntityGraph<Owner> entityGraph = emf.createEntityGraph(Owner.class);
        entityGraph.addAttributeNodes("id", "name","dateOfBirth", "contact", "username", "profileAvatar");
        simpleProperty(entityGraph.addSubgraph("propertiesOwned"));

        return entityGraph;
    }

    public static EntityGraph<Host> hostForSearching(Session session) {

        EntityManager emf = session.unwrap(EntityManager.class);
        EntityGraph<Host> entityGraph = emf.createEntityGraph(Host.class);
        entityGraph.addAttributeNodes("id", "name","dateOfBirth", "contact", "username");
        simpleProperty(entityGraph.addSubgraph("propertiesManaged"));

        return entityGraph;
    }

    // load simple information (only for display)
    public static EntityGraph<Renter> SimpleRenter(Session session) {

        EntityManager emf = session.unwrap(EntityManager.class);
        EntityGraph<Renter> entityGraph = emf.createEntityGraph(Renter.class);
        entityGraph.addAttributeNodes("id", "name","dateOfBirth", "contact", "username");
        return entityGraph;
    }

    public static EntityGraph<Owner> SimpleOwner(Session session) {

        EntityManager emf = session.unwrap(EntityManager.class);
        EntityGraph<Owner> entityGraph = emf.createEntityGraph(Owner.class);
        entityGraph.addAttributeNodes("id", "name","dateOfBirth", "contact", "username");
        return entityGraph;
    }

    public static EntityGraph<Host> SimpleHost(Session session) {

        EntityManager emf = session.unwrap(EntityManager.class);
        EntityGraph<Host> entityGraph = emf.createEntityGraph(Host.class);
        entityGraph.addAttributeNodes("id", "name","dateOfBirth", "contact", "username");
        return entityGraph;
    }
    
    public static EntityGraph<CommercialProperty> SimpleCommercialProperty(Session session){

        EntityManager emf = session.unwrap(EntityManager.class);
        EntityGraph<CommercialProperty> entityGraph = emf.createEntityGraph(CommercialProperty.class);
        entityGraph.addAttributeNodes("images", "address", "price", "type", "id", "businessType", "totalParkingSpace", "squareMeters", "images"); // Add only the name of the Owner
//        Subgraph<Owner> ownerSubgraph = entityGraph.addSubgraph("owner");
//        ownerSubgraph.addAttributeNodes("name");
//        Subgraph<Host> hostEntityGraph = entityGraph.addSubgraph("hosts");
//        hostEntityGraph.addAttributeNodes("id", "name");
//        hostEntityGraph.addSubgraph("receivedNotifications");
        return entityGraph;
    }

    public static EntityGraph<ResidentialProperty> SimpleResidentialProperty(Session session) {

        EntityManager emf = session.unwrap(EntityManager.class);
        EntityGraph<ResidentialProperty> entityGraph = emf.createEntityGraph(ResidentialProperty.class);
        entityGraph.addAttributeNodes("images", "address", "price", "type", "id", "totalRoom", "totalBedroom", "isPetAllowed","hasGarden", "images"); // Add only the name of the Owner
//        Subgraph<Owner> ownerSubgraph = entityGraph.addSubgraph("owner");
//        ownerSubgraph.addAttributeNodes("name");
//        Subgraph<Host> hostEntityGraph = entityGraph.addSubgraph("hosts");
//        hostEntityGraph.addAttributeNodes("id", "name");
//        hostEntityGraph.addSubgraph("receivedNotifications");
//        personSubgraph(entityGraph.addSubgraph("owner"));
//        personSubgraph(entityGraph.addSubgraph("hosts"));
        return entityGraph;
    }

    public static EntityGraph<RentalAgreement> SimpleRentalAgreement(Session session) {

        EntityManager emf = session.unwrap(EntityManager.class);
        EntityGraph<RentalAgreement> entityGraph = emf.createEntityGraph(RentalAgreement.class);

        Subgraph<Renter> subgraphMainRenter = entityGraph.addSubgraph("mainTenant");
        subgraphMainRenter.addAttributeNodes("id", "name", "contact", "username");

        Subgraph<Renter> subgraphSubRenter = entityGraph.addSubgraph("subTenants");
        subgraphMainRenter.addAttributeNodes("id", "name", "contact", "username");

        Subgraph<Host> subgraphHost = entityGraph.addSubgraph("host");
        subgraphMainRenter.addAttributeNodes("id", "name", "contact", "username");

        Subgraph<Property> subgraphProperty = entityGraph.addSubgraph("property");
        subgraphMainRenter.addAttributeNodes("id", "address", "price", "type");

        propertySubgraph(entityGraph.addSubgraph("property"));


        return entityGraph;
    }


    // load full ( for log in )
    public static EntityGraph<Renter> RenterFULL(Session session) {

        EntityManager emf = session.unwrap(EntityManager.class);
        EntityGraph<Renter> entityGraph = emf.createEntityGraph(Renter.class);
        entityGraph.addAttributeNodes("id", "name","dateOfBirth", "contact", "username", "password", "profileAvatar");

        rentalAgreementSubgraph(entityGraph.addSubgraph("agreementList"));
        rentalAgreementSubgraph(entityGraph.addSubgraph("subAgreements"));
        paymentGraph(entityGraph.addSubgraph("payments"));
        rentalAgreementSubgraph(entityGraph.addSubgraph("subAgreements"));


        notificationGraph(entityGraph.addSubgraph("sentNotifications"));
        notificationGraph(entityGraph.addSubgraph("receivedNotifications"));
        return entityGraph;
    }

    public static EntityGraph<Owner> OwnerFULL(Session session) {

        EntityManager emf = session.unwrap(EntityManager.class);
        EntityGraph<Owner> entityGraph = emf.createEntityGraph(Owner.class);
        entityGraph.addAttributeNodes("id", "name","dateOfBirth", "contact", "username", "password", "profileAvatar");
        notificationGraph(entityGraph.addSubgraph("sentNotifications"));
        notificationGraph(entityGraph.addSubgraph("receivedNotifications"));

        propertySubgraph(entityGraph.addSubgraph("propertiesOwned"));
        simplePerson(entityGraph.addSubgraph("hosts"));

        return entityGraph;
    }

    public static EntityGraph<Host> HostFULL(Session session) {


        EntityManager emf = session.unwrap(EntityManager.class);
        EntityGraph<Host> entityGraph = emf.createEntityGraph(Host.class);
        entityGraph.addAttributeNodes("id", "name","dateOfBirth", "contact", "username", "password", "profileAvatar");
//

        simpleRentalAgreement(entityGraph.addSubgraph("rentalAgreements"));
        simpleProperty(entityGraph.addSubgraph("propertiesManaged"));

        //owner
        Subgraph<Owner> ownerSubgraph = entityGraph.addSubgraph("cooperatingOwners");
        simplePerson(ownerSubgraph);
//        simpleProperty(ownerSubgraph.addSubgraph("propertiesOwned"));

        notificationGraph(entityGraph.addSubgraph("sentNotifications"));
        notificationGraph(entityGraph.addSubgraph("receivedNotifications"));
        return entityGraph;
    }


    // load full but only simple attribute (  for ADMIN )
    public static EntityGraph<Renter> SimpleRenterFull(Session session) {

        EntityManager emf = session.unwrap(EntityManager.class);
        EntityGraph<Renter> entityGraph = emf.createEntityGraph(Renter.class);
        entityGraph.addAttributeNodes("id", "name","dateOfBirth", "contact", "username", "password");

        simpleRentalAgreement(entityGraph.addSubgraph("agreementList"));
        simpleRentalAgreement(entityGraph.addSubgraph("subAgreements"));
        simplePayment(entityGraph.addSubgraph("payments"));

//        notificationGraph(entityGraph.addSubgraph("sentNotifications"));
//        notificationGraph(entityGraph.addSubgraph("receivedNotifications"));
        return entityGraph;
    }

    public static EntityGraph<Renter> SimpleRenterNotification(Session session) {

        EntityManager emf = session.unwrap(EntityManager.class);
        EntityGraph<Renter> entityGraph = emf.createEntityGraph(Renter.class);
        entityGraph.addAttributeNodes("id", "name","dateOfBirth", "contact", "username", "password");

        SimpleNotification(entityGraph.addSubgraph("receivedNotifications"));
        return entityGraph;
    }




    public static EntityGraph<Owner> SimpleOwnerFull(Session session) {

        EntityManager emf = session.unwrap(EntityManager.class);
        EntityGraph<Owner> entityGraph = emf.createEntityGraph(Owner.class);
        entityGraph.addAttributeNodes("id", "name","dateOfBirth", "contact", "username", "password");
        Subgraph<Property> propertySubgraph = entityGraph.addSubgraph("propertiesOwned");
        simpleProperty(propertySubgraph);
        propertySubgraph.addSubgraph("agreementList");
        propertySubgraph.addSubgraph("hosts");

        simplePerson(entityGraph.addSubgraph("hosts"));

        return entityGraph;
    }

    public static EntityGraph<Host> SimpleHostFull(Session session) {

        EntityManager emf = session.unwrap(EntityManager.class);
        EntityGraph<Host> entityGraph = emf.createEntityGraph(Host.class);
        entityGraph.addAttributeNodes("id", "name","dateOfBirth", "contact", "username", "password");

        simpleProperty(entityGraph.addSubgraph("propertiesManaged"));
        simpleRentalAgreement(entityGraph.addSubgraph("rentalAgreements"));
        simplePerson(entityGraph.addSubgraph("cooperatingOwners"));

        return entityGraph;
    }

    public static EntityGraph<Admin> SimpleAdminFull(Session session) {

        EntityManager emf = session.unwrap(EntityManager.class);
        EntityGraph<Admin> entityGraph = emf.createEntityGraph(Admin.class);
        entityGraph.addAttributeNodes("id", "name","dateOfBirth", "contact", "username", "password");

        return entityGraph;
    }

    public static EntityGraph<Payment> SimplePaymentFull(Session session) {

        EntityManager emf = session.unwrap(EntityManager.class);
        EntityGraph<Payment> entityGraph = emf.createEntityGraph(Payment.class);
        entityGraph.addAttributeNodes("paymentId", "amount","date", "paymentMethod");

        simpleRentalAgreement(entityGraph.addSubgraph("rentalAgreement"));
        simplePerson(entityGraph.addSubgraph("mainRenter"));
        simpleProperty(entityGraph.addSubgraph("property"));

        return entityGraph;
    }

    public static EntityGraph<CommercialProperty> SimpleCommercialPropertyFull(Session session){

        EntityManager emf = session.unwrap(EntityManager.class);
        EntityGraph<CommercialProperty> entityGraph = emf.createEntityGraph(CommercialProperty.class);
        entityGraph.addAttributeNodes("address", "price", "type", "id", "businessType", "totalParkingSpace", "squareMeters", "images"); // Add only the name of the Owner
        Subgraph<RentalAgreement> rentalAgreementSubgraph = entityGraph.addSubgraph("agreementList");
        Subgraph<Renter> mainTenantSubgraph = rentalAgreementSubgraph.addSubgraph("mainTenant");
        Subgraph<Host> hostEntityGraph = entityGraph.addSubgraph("hosts");

        rentalAgreementSubgraph.addAttributeNodes("id");
        mainTenantSubgraph.addAttributeNodes("name");
        hostEntityGraph.addSubgraph("receivedNotifications");

        simplePerson(entityGraph.addSubgraph("owner"));
        simplePerson(hostEntityGraph);

        return entityGraph;
    }

    public static EntityGraph<ResidentialProperty> SimpleResidentialPropertyFull(Session session) {

        EntityManager emf = session.unwrap(EntityManager.class);
        EntityGraph<ResidentialProperty> entityGraph = emf.createEntityGraph(ResidentialProperty.class);
        entityGraph.addAttributeNodes("address", "price", "type", "id", "totalRoom", "totalBedroom", "isPetAllowed","hasGarden", "images"); // Add only the name of the Owner

        Subgraph<RentalAgreement> rentalAgreementSubgraph = entityGraph.addSubgraph("agreementList");
        Subgraph<Renter> mainTenantSubgraph = rentalAgreementSubgraph.addSubgraph("mainTenant");
        Subgraph<Host> hostEntityGraph = entityGraph.addSubgraph("hosts");

        rentalAgreementSubgraph.addAttributeNodes("id");
        mainTenantSubgraph.addAttributeNodes("name");
        hostEntityGraph.addSubgraph("receivedNotifications");

        simplePerson(entityGraph.addSubgraph("owner"));
        simplePerson(hostEntityGraph);
        return entityGraph;
    }

    public static EntityGraph<RentalAgreement> SimpleRentalAgreementFull(Session session) {

        EntityManager emf = session.unwrap(EntityManager.class);
        EntityGraph<RentalAgreement> entityGraph = emf.createEntityGraph(RentalAgreement.class);
        entityGraph.addAttributeNodes("id", "period", "contractDate", "rentingFee", "status");
        Subgraph<Host> hostSubgraph = entityGraph.addSubgraph("host");
        Subgraph<Renter> mainTenantSubgraph = entityGraph.addSubgraph("mainTenant");
        Subgraph<Renter> subTenantsSubgraph = entityGraph.addSubgraph("subTenants");
        Subgraph<Property> propertySubgraph = entityGraph.addSubgraph("property");
        mainTenantSubgraph.addSubgraph("agreementList");
        subTenantsSubgraph.addSubgraph("subAgreements");

        simplePerson(hostSubgraph);
        simplePerson(mainTenantSubgraph);
        simplePerson(subTenantsSubgraph);
        simpleProperty(propertySubgraph);
//        Subgraph<Host> hostSubgraphProperty = propertySubgraph.addSubgraph("hosts");
//        hostSubgraphProperty.addAttributeNodes("id", "name");
//        hostSubgraphProperty.addSubgraph("rentalAgreements");

        Subgraph<Host> hostSubgraph1 =  propertySubgraph.addSubgraph("hosts");
        hostSubgraph1.addSubgraph("rentalAgreements");




        return entityGraph;
    }

    ///////////////////////// HELPERS /////////////////////////

    protected static void notificationGraph(Subgraph<Notification> graph){
        graph.addAttributeNodes("id", "content", "timestamp", "header");
        Subgraph<Person> senderSubgraph = graph.addSubgraph("sender");
        senderSubgraph.addAttributeNodes("id", "name");

        Subgraph<Person> receiversSubgraph = graph.addSubgraph("receivers");
        receiversSubgraph.addAttributeNodes("id", "name");


//        simplePerson(graph.addSubgraph("sender"));
//        simplePerson(graph.addSubgraph("receivers"));
    }

    protected static void SimpleNotification(Subgraph<Notification> graph){
        graph.addAttributeNodes("id", "content", "timestamp", "header");

    }

    protected static <T extends Person> void personSubgraph(Subgraph<T> graph){
        graph.addAttributeNodes("id", "name","dateOfBirth", "contact", "username", "password");
        notificationGraph(graph.addSubgraph("sentNotifications"));
        notificationGraph(graph.addSubgraph("receivedNotifications"));
    }


    private static void paymentGraph(Subgraph<Payment> graph){
        graph.addAttributeNodes("paymentId", "amount", "date", "paymentMethod");
//        personSubgraph(graph.addSubgraph("mainRenter"));
//        propertySubgraph(graph.addSubgraph("property"));
//        rentalAgreementSubgraph(graph.addSubgraph("rentalAgreement"));

        simplePerson(graph.addSubgraph("mainRenter"));
        simpleProperty(graph.addSubgraph("property"));
        simpleRentalAgreement(graph.addSubgraph("rentalAgreement"));
    }


    private static  void rentalAgreementSubgraph(Subgraph<RentalAgreement> graph){
        graph.addAttributeNodes("id", "period", "contractDate", "rentingFee", "status");

//        personSubgraph(graph.addSubgraph("mainTenant"));
//        personSubgraph(graph.addSubgraph("host"));
//        personSubgraph(graph.addSubgraph("subTenants"));
//        propertySubgraph(graph.addSubgraph("property"));

        simplePerson(graph.addSubgraph("mainTenant"));
        simplePerson(graph.addSubgraph("host"));
        simplePerson(graph.addSubgraph("subTenants"));
        simpleProperty(graph.addSubgraph("property"));
    }

    private static void propertySubgraph(Subgraph<Property> graph ){
        graph.addAttributeNodes("address", "price", "type", "id");

        Subgraph<Owner> ownerSubgraph = graph.addSubgraph("owner"); // Assuming `owner` is the name of the relationship in Property
        ownerSubgraph.addAttributeNodes("name"); // Add only the name of the Owner
        graph.addSubgraph("hosts");
        graph.addSubgraph("agreementList");
    }


    //Simple
    private static void simpleRentalAgreement(Subgraph<RentalAgreement> graph){
        graph.addAttributeNodes("id", "period", "contractDate", "rentingFee", "status");

        Subgraph<Property> ownerSubgraph = graph.addSubgraph("property"); // Assuming `owner` is the name of the relationship in Property
        ownerSubgraph.addAttributeNodes("address"); // Add only the name of the Owner

        Subgraph<Renter> mainTenantSubgraph = graph.addSubgraph("mainTenant");
        mainTenantSubgraph.addAttributeNodes("name");

        mainTenantSubgraph.addSubgraph("receivedNotifications");

        Subgraph<Payment> paymentSubgraph = graph.addSubgraph("payments");
        paymentSubgraph.addAttributeNodes("paymentId", "amount", "date", "paymentMethod");

//        simplePerson(graph.addSubgraph("mainTenant"));
//        personSimple(graph.addSubgraph("host"));
//        personSimple(graph.addSubgraph("subTenants"));
//        personSimple(graph.addSubgraph("property"));
    }

    private static <T extends Person> void simplePerson(Subgraph<T> graph){
        graph.addAttributeNodes("id", "name","dateOfBirth", "contact", "username", "password");
    }

    private static void simplePayment(Subgraph<Payment> graph){
        graph.addAttributeNodes("paymentId", "amount", "date", "paymentMethod");

        Subgraph<RentalAgreement> rentalAgreementSubgraph = graph.addSubgraph("rentalAgreement");
        rentalAgreementSubgraph.addAttributeNodes("agreementId");

//        personSimple(graph.addSubgraph("mainRenter"));
//        propertySubgraph(graph.addSubgraph("property"));       // new add simple
//        simpleRentalAgreement(graph.addSubgraph("rentalAgreement"));

    }

    private static void simpleProperty(Subgraph<Property> graph ){
        graph.addAttributeNodes("address", "price", "type", "id");

        Subgraph<Owner> ownerSubgraph = graph.addSubgraph("owner"); // Assuming `owner` is the name of the relationship in Property
        ownerSubgraph.addAttributeNodes("name"); // Add only the name of the Owner
    }








}
