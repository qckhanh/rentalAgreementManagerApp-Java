package Model;

import UIHelper.UserInterfaceManager;
import UIHelper.DateCreator;

import java.util.ArrayList;
import java.util.List;

public class CommercialProperty extends Property {
    private String businessType;
    private int totalParkingSpace;
    private double squareMeters;

    public CommercialProperty(String address, double price, PropertyStatus status) {
        super(address, price, status);
    }

    public CommercialProperty() {
        super();
    }

    public CommercialProperty(String address, double price, PropertyStatus status, String businessType, int totalParkingSpace, double squareMeters) {
        super(address, price, status);
        this.businessType = businessType;
        this.totalParkingSpace = totalParkingSpace;
        this.squareMeters = squareMeters;
    }


    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public int getTotalParkingSpace() {
        return totalParkingSpace;
    }

    public void setTotalParkingSpace(int totalParkingSpace) {
        this.totalParkingSpace = totalParkingSpace;
    }

    public double getSquareMeters() {
        return squareMeters;
    }

    public void setSquareMeters(double squareMeters) {
        this.squareMeters = squareMeters;
    }

    @Override
    public String toString() {
        return super.toString()  +
                " Business Type = " + businessType  +
                ", Parking Space = " + totalParkingSpace +
                ", Square Meters = " + squareMeters +
                ']';
    }

    @Override
    public void preview() {
        UserInterfaceManager.printMenu("Property Information",
                List.of(
                        "ID: " + super.getId(),
                        "Owner: " + ((super.getOwner() == null) ?  "null" : super.getOwner().getName()),
                        "Address: " + super.getAddress(),
                        "Price: " + super.getPrice(),
                        "Status: " + super.getStatus().toString(),
                        "Business Type: " + this.businessType,
                        "Parking Space: " + this.totalParkingSpace,
                        "Square Meters: " + this.squareMeters
                )
        );
        List<Host> hosts = super.getHosts();
        List<RentalAgreement> agreementList = super.getAgreementList();
        if(hosts == null || hosts.isEmpty()) UserInterfaceManager.successMessage("No host found");
        else{
            List<List<String>> data = new ArrayList<>();
            List<String> header = List.of("Host ID", "Name", "Contact");
            for (Host host : hosts) {
                List<String> row = new ArrayList<>();
                row.add(String.valueOf(host.getId()));
                row.add(host.getName());
                row.add(host.getContact());
                data.add(row);
            }
            UserInterfaceManager.printTable("Hosts", header, data);
        }
        if(agreementList == null || agreementList.isEmpty()) UserInterfaceManager.successMessage("No agreement found");
        else {


            List<List<String>> data = new ArrayList<>();
            List<String> header = List.of("Agreement ID", "Property's address", "Period", "Contract Date", "Price", "Status");
            for (RentalAgreement rentalAgreement : agreementList) {
                List<String> row = new ArrayList<>();
                row.add(String.valueOf(rentalAgreement.getAgreementId()));
                row.add(rentalAgreement.getProperty().getAddress());
                row.add(rentalAgreement.getPeriod().toString());
                row.add(DateCreator.formatDate(rentalAgreement.getContractDate()));
                row.add(rentalAgreement.getRentingFee() + "");
                row.add(rentalAgreement.getStatus().toString());
                data.add(row);
            }
            UserInterfaceManager.printTable("Rental agreements ", header, data);
        }

    }
}

