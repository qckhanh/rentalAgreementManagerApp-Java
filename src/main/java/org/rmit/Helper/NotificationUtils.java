package org.rmit.Helper;

import org.rmit.Notification.NormalNotification;
import org.rmit.Notification.Notification;
import org.rmit.Notification.Request;
import org.rmit.model.Persons.Person;
import org.rmit.model.Property.CommercialProperty;
import org.rmit.model.Property.ResidentialProperty;

import java.util.List;

public class NotificationUtils {
    public static String HEADER_REQUEST = "About: Request Property : ID: %s, Address: %s";
    public static String HEADER_PAYMENT = "About: Purchase for Agreement : ID: %s, Property: %s";
    public static String HEADER_RENTAL = "About: Rental Agreement Request - Renter: %s";
    public static String HEADER_ADMIN = "[ADMIN]: New property assigned : ID: %s, Address: %s";
    public static String HEADER_PROPERTY_REQUEST = "About: Request Property : ID: %s, Address: %s";

    public static String HEADER_ADMIN_REMOVE = "[ADMIN]: Property removed : ID: %s, Address: %s";
    public static String HEADER_ADMIN_UPDATE = "[ADMIN]: Property updated : ID: %s, Address: %s";
    public static String HEADER_ADMIN_ADD = "[ADMIN]: New property assigned : ID: %s, Address: %s";

    public static String DAFT_PROPERTY_COMMERCIAL = "PROCOM-%s";
    public static String DAFT_PROPERTY_RESIDENTIAL = "PRORES-%s";
    public static String DAFT_PROPERTY_RA = "RA-%s";

    public static String HEADER_ADMIN_UPDATE_USER = "[ADMIN]: User's information updated";

    public static String CONTENT_REQUEST = "Dear %s, \n" +
            "I, %s, want to request to manage your property with ID: %s, Address: %s. " +
            "Please approve or deny the request.";
    public static String CONTENT_APPROVE_PROPERTY = "Dear %s, \n" +
            "Your request to manage the property with ID: %s, Address: %s has been approved.";

    public static String CONTENT_PAYMENT = "Dear %s \n" +
            "You have a new payment for the agreement with ID: %s, Property: %s. " +
            "Please approve or deny the request.";


    public String DENY_REQUEST = "[ %s ] has denied your request about [ %s ]";
    public String APPROVE_REQUEST = "[ %s ] has approved your request about [ %s ]";
    public String UNMANAGE_PROPERTY = "[ %s ] has unmanaged the property [ %s ]";
    public String MANAGE_PROPERTY = "[ %s ] has managed the property [ %s ]";


    public static Notification createNormalNotification(Person sender, List<Person> receivers, String header, String content) {
        Notification notification = new NormalNotification();
        notification.setTimestamp(DateUtils.currentTimestamp());
        notification.setSender(sender);
        for(Person p: receivers){
            notification.addReceiver(p);
        }
        notification.setHeader(header);
        notification.setContent(content);
        return notification;
    }

    public static Notification createRequest(Person sender, List<Person> receivers, String header, String content, String dartObject) {
        Request request = new Request();
        request.setTimestamp(DateUtils.currentTimestamp());
        request.setSender(sender);
        for(Person p: receivers){
            request.addReceiver(p);
        }
        request.setHeader(header);
        request.setContent(content);
        request.setDraftObject(dartObject);
        return request;
    }

    public static String getDraftType(String drafObject){
        if(drafObject.startsWith("PROCOM")) return "CommercialProperty";
        else if(drafObject.startsWith("PRORES")) return "ResidentialProperty";
        else return "RentalAgreement";
    }

    public static int getDraftID(String draftObject){
        String[] parts = draftObject.split("-");
        return Integer.parseInt(parts[1]);
    }
}
