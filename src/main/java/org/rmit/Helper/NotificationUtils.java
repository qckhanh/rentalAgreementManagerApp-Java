package org.rmit.Helper;

import org.rmit.Notification.NormalNotification;
import org.rmit.Notification.Notification;
import org.rmit.Notification.Request;
import org.rmit.model.Persons.Person;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Property.RentalPeriod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class NotificationUtils {
    public static String HEADER_REQUEST_PROPERTY = "About: Request Property : ID: %s, Address: %s";
    public static String HEADER_REQUEST_AGREEMENT = "About: Request A Rental Agreement : Property ID: %s, Address: %s";

    public static String HEADER_PAYMENT = "About: Purchase for Agreement : ID: %s, Property: %s";
    public static String HEADER_RENTAL = "About: Rental Agreement Request - Renter: %s";
    public static String HEADER_ADMIN = "[ADMIN]: New property assigned : ID: %s, Address: %s";

    public static String HEADER_ADMIN_REMOVE = "[ADMIN]: Property removed : ID: %s, Address: %s";
    public static String HEADER_ADMIN_UPDATE = "[ADMIN]: Property updated : ID: %s, Address: %s";
    public static String HEADER_ADMIN_ADD = "[ADMIN]: New property assigned : ID: %s, Address: %s";

    public static String DAFT_PROPERTY_COMMERCIAL = "PROCOM-%s";
    public static String DAFT_PROPERTY_RESIDENTIAL = "PRORES-%s";
    public static String DAFT_PROPERTY_RA = "RA-%s";

    public static String HEADER_ADMIN_UPDATE_USER = "[ADMIN]: User's information updated";

    public static String CONTENT_REQUEST_PROPERTY = "Dear %s, \n" +
            "My name is %s and I want to request to manage your property with ID: %s, Address: %s. " +
            "Please approve or deny the request.";
    public static String CONTENT_REQUEST_AGREEMENT = "Dear %s, \n" +
            "My name is %s and I want to rent your property with ID: %s, Address: %s. " +
            "I will pay for the rent %s, and pay %s $ for every transaction" +
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

    public static String buildContent_REQUEST_AGREEMENT(String hostName, String renterName, String propertyID, String propertyAddress, RentalPeriod period, double rentFee){
        return String.format(
                CONTENT_REQUEST_AGREEMENT,
                hostName,
                renterName,
                propertyID,
                propertyAddress,
                period.toString(),
                rentFee
        );
    }

    public static String buildDaft_RentalAgreement(long propertyID, long hostID, long OwnerID, RentalPeriod period, Set<Renter> subRenters){
        String draft = "RA" + ",";
        draft += propertyID + ","
                + hostID + ","
                + OwnerID + ","
                + period.toString();
        for(Renter r: subRenters){
            draft += ","+ r.getId();
        }
        return draft;
    }

    public static List<String> draftID_RentalAgreement(String draft){
        if(!getDraftType(draft).equals("RentalAgreement")) return Collections.EMPTY_LIST;
        String[] parts = draft.split(",");
        List<String> ids = new ArrayList<>();
        for(int i = 1; i < parts.length; i++){
            ids.add(parts[i]);
        }
        return ids;
    }
}
