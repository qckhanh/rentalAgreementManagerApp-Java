package Model;

import UIHelper.UserInterfaceManager;
import UIHelper.DateCreator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// class Tenant
public class Renter extends Person{
    private List<RentalAgreement> agreementList = new ArrayList<>();
    private List<Payment> payments = new ArrayList<>();

    public Renter(String name, Date dateOfBirth, String contact) {
        super(name, dateOfBirth, contact);
    }

    public List<RentalAgreement> getAgreementList() {
        return agreementList;
    }

    public void setAgreementList(List<RentalAgreement> agreementList) {
        this.agreementList = agreementList;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public void preview(){
        super.preview();
        if(agreementList == null || agreementList.isEmpty()) UserInterfaceManager.successMessage("No agreement found");
        else{
            List<List<String>> data = new ArrayList<>();
            List<String> header = List.of("Agreement ID", "Property ID", "Property Address", "Status");
            for (RentalAgreement rentalAgreement : agreementList) {
               List<String> row = new ArrayList<>();
                row.add(String.valueOf(rentalAgreement.getAgreementId()));
                row.add(String.valueOf(rentalAgreement.getProperty().getId()));
                row.add(rentalAgreement.getProperty().getAddress());
                row.add(rentalAgreement.getStatus().toString());
                data.add(row);
            }
            UserInterfaceManager.printTable(this.name + "'s rental agreements ", header, data);
        }
        if(payments == null || payments.isEmpty()) UserInterfaceManager.successMessage("No payment found");
        else{
            List<List<String>> data = new ArrayList<>();
            List<String> header = List.of("Payment ID", "Agreement ID", "Property's address", "Amount", "Date");
            for (Payment payment : payments) {
                List<String> row = new ArrayList<>();
                row.add(String.valueOf(payment.getPaymentId()));
                row.add(String.valueOf(payment.getAgreement().getAgreementId()));
                row.add(String.valueOf(payment.getAgreement().getProperty().getAddress()));
                row.add(String.valueOf(payment.getAmount()));
                row.add(DateCreator.formatDate(payment.getDate()));
                data.add(row);
            }
            UserInterfaceManager.printTable(this.name + "'s payments ", header, data);
        }
    }

    public void addAgreement(RentalAgreement agreement){
        agreementList.add(agreement);
    }

    public void addPayment(Payment payment){
        payments.add(payment);
    }


    @Override
    public String toString() {
        return  super.toString() +
                ", Payments=" + (payments.isEmpty() ? "NA" : payments)+
                ", agreementList=" +  ((agreementList.isEmpty()) ? "NA" : agreementList) +
                '\n';
    }
}
