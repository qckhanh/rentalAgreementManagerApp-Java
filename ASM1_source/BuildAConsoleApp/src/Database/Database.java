package Database;

import Model.*;
import UIHelper.UserInterfaceManager;

import java.io.*;
import java.util.*;

public class Database{
    private List<Host> hostList;
    private List<Owner> ownerList;
    private List<Renter> renterList;
    private List<Property> propertyList;
    private List<RentalAgreement> rentalAgreementList;
    private List<Payment> paymentList;

    private static Map<Integer, Integer> PERSON_ID;
    private static Map<Integer, Integer> PROPERTY_ID;
    private static Map<Integer, Integer> PAYMENT_ID;
    private static Map<Integer, Integer> AGREEMENT_ID;
    private final String PATH = "src\\DataSaved";

    private static  Database db;
    private Database() {
        hostList = new ArrayList<>();
        ownerList = new ArrayList<>();
        renterList = new ArrayList<>();
        propertyList = new ArrayList<>();
        rentalAgreementList = new ArrayList<>();
        paymentList = new ArrayList<>();

        PERSON_ID = new HashMap<>();
        PROPERTY_ID = new HashMap<>();
        PAYMENT_ID = new HashMap<>();
        AGREEMENT_ID = new HashMap<>();
        loadData();
    }
    public static Database getInstance(){
        return (db == null) ? db = new Database() : db;
    }
    public void loadData(){
        try{
            File file = new File(PATH + "\\USER_DATA.ser");
            File file2 = new File(PATH + "\\USER_ID.ser");

            if(file.length() == 0 || file2.length() == 0){
                UserInterfaceManager.errorMessage("Data loaded failed");
                return;
            }
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));

            hostList = (List<Host>) ois.readObject();
            ownerList = (List<Owner>) ois.readObject();
            renterList = (List<Renter>) ois.readObject();
            propertyList = (List<Property>) ois.readObject();
            rentalAgreementList = (List<RentalAgreement>) ois.readObject();
            paymentList = (List<Payment>) ois.readObject();   // new
            ois.close();

            ObjectInputStream ois2 = new ObjectInputStream(new FileInputStream(file2));
            PERSON_ID = (Map<Integer, Integer>) ois2.readObject();
            PROPERTY_ID = (Map<Integer, Integer>) ois2.readObject();
            PAYMENT_ID = (Map<Integer, Integer>) ois2.readObject();
            AGREEMENT_ID = (Map<Integer, Integer>) ois2.readObject();
            ois2.close();

            UserInterfaceManager.successMessage("Data loaded successfully");

        }
        catch (Exception e){
            UserInterfaceManager.errorMessage("Data loaded incorrectly");
        }
    }
    public void saveData() {
        try{
            File file = new File(PATH + "\\USER_DATA.ser");
            ObjectOutputStream data = new ObjectOutputStream(new FileOutputStream(file));
            data.writeObject(hostList);
            data.writeObject(ownerList);
            data.writeObject(renterList);
            data.writeObject(propertyList);
            data.writeObject(rentalAgreementList);
            data.writeObject(paymentList);   // new
            data.flush();
            data.close();

            File file2 = new File(PATH + "\\USER_ID.ser");
            ObjectOutputStream ID = new ObjectOutputStream(new FileOutputStream(file2));
            ID.writeObject(PERSON_ID);
            ID.writeObject(PROPERTY_ID);
            ID.writeObject(PAYMENT_ID);
            ID.writeObject(AGREEMENT_ID);
            ID.flush();
            ID.close();

            UserInterfaceManager.successMessage("Data saved successfully");
        }
        catch (Exception e){
            System.out.println(e);
        }

    }
    public static int IDGenerator(Class<?> c){
        int ID = Math.abs(new Random().nextInt() % 1000);

        while(true){
            ID = Math.abs(new Random().nextInt() % 1000);

            if(c.equals(Person.class)){
                if(!PERSON_ID.containsValue(ID)){
                    PERSON_ID.put(ID, 1);
                    break;
                }
            }
            else if(c.equals(Property.class)){
                if(!PROPERTY_ID.containsValue(ID)){
                    PROPERTY_ID.put(ID, 1);
                    break;
                }
            }
            else if(c.equals(Payment.class)){
                if(!PAYMENT_ID.containsValue(ID)){
                    PAYMENT_ID.put(ID, 1);
                    break;
                }
            }
            else if(c.equals(RentalAgreement.class)){
                if(!AGREEMENT_ID.containsValue(ID)){
                    AGREEMENT_ID.put(ID, 1);
                    break;
                }
            }
        }
        return ID;
    }
    public boolean add(Object o) {
        if(o instanceof Host){
            if(hostList.contains((Host) o)) return false;
            return hostList.add((Host) o);
        }
        else if(o instanceof Owner){
            if (ownerList.contains((Owner) o)) return false;
            return ownerList.add((Owner) o);
        }
        else if(o instanceof Renter){
            if(renterList.contains((Renter) o)) return false;
            return renterList.add((Renter) o);
        }
        else if(o instanceof Property){
            if(propertyList.contains((Property) o)) return false;
            return propertyList.add((Property) o);
        }
        else if(o instanceof RentalAgreement){
            if(rentalAgreementList.contains((RentalAgreement) o)) return false;
            return rentalAgreementList.add((RentalAgreement) o);
        }
        else if(o instanceof Payment){
            if(paymentList.contains((Payment) o)) return false;
            return paymentList.add((Payment) o);
        }
        return false;
    }
    public Object getByIndex(Class<?> clazz, int index) {
        if(index < 0) return null;
        if(clazz.equals(Host.class)){
            if(index >= hostList.size()) return null;
            return hostList.get(index);
        }
        else if(clazz.equals(Owner.class)){
            if (index >= ownerList.size()) return null;
            return ownerList.get(index);
        }
        else if(clazz.equals(Renter.class)){
            if(index >= renterList.size()) return null;
            return renterList.get(index);
        }
        else if(clazz.equals(Property.class)){
            if(index >= propertyList.size()) return null;
            return propertyList.get(index);
        }
        else if(clazz.equals(RentalAgreement.class)){
            if(index >= rentalAgreementList.size()) return null;
            return rentalAgreementList.get(index);
        }
        else if(clazz.equals(Payment.class)){
            if(index >= paymentList.size()) return null;
            return paymentList.get(index);
        }
        return new Object();
    }
    public Object getByID(Class<?> clazz, int id) {
        if(clazz.equals(Host.class)){
            for(Host host : hostList){
                if(host.getId() == id) return host;
            }
        }
        else if(clazz.equals(Owner.class)){
            for(Owner owner : ownerList){
                if(owner.getId() == id) return owner;
            }
        }
        else if(clazz.equals(Renter.class)){
            for(Renter renter : renterList){
                if(renter.getId() == id) return renter;
            }
        }
        else if(clazz.equals(Property.class)){
            for(Property property : propertyList){
                if(property.getId() == id) return property;
            }
        }
        else if(clazz.equals(RentalAgreement.class)){
            for(RentalAgreement rentalAgreement : rentalAgreementList) {
                if (rentalAgreement.getAgreementId() == id) return rentalAgreement;
            }
        }
        else if(clazz.equals(Payment.class)){
            for(Payment payment : paymentList){
                if(payment.getPaymentId() == id) return payment;
            }
        }
        return new Object();
    }
    public boolean delete(Object o) {
        if(o instanceof Host){
            for(Host host : hostList){
                if(host.equals(o)){
                    hostList.remove(host);
                    return true;
                }
            }
        }
        else if(o instanceof Owner){
            for (Owner owner : ownerList){
                if(owner.equals(o)){
                    ownerList.remove(owner);
                    return true;
                }
            }
        }
        else if(o instanceof Renter){
            for (Renter renter : renterList){
                if(renter.equals(o)){
                    renterList.remove(renter);
                    return true;
                }
            }
        }
        else if(o instanceof Property){
           for (Property property : propertyList){
               if(property.equals(o)){
                   propertyList.remove(property);
                   return true;
               }
           }
        }
        else if(o instanceof RentalAgreement){
            for (RentalAgreement rentalAgreement : rentalAgreementList){
                if(rentalAgreement.equals(o)){
                    rentalAgreementList.remove(rentalAgreement);
                    return true;
                }
            }
        } else if (o instanceof Payment) {
            for (Payment payment : paymentList) {
                if (payment.equals(o)) {
                    paymentList.remove(payment);
                    return true;
                }
            }

        }
        return false;
    }
    public List<?> getAll(Class<?> c) {
        if(c.equals(Host.class)) return hostList;
        else if(c.equals(Owner.class)) return ownerList;
        else if(c.equals(Renter.class)) return renterList;
        else if(c.equals(Property.class)) return propertyList;
        else if(c.equals(RentalAgreement.class)) return rentalAgreementList;
        else if(c.equals(Payment.class)) return paymentList;
        else return List.of();
    }

}
