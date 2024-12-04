package Model;

import UIHelper.UserInterfaceManager;
import UIHelper.DateCreator;

import java.util.ArrayList;
import java.util.List;

public class ResidentialProperty extends Property {
    private int totalRoom;
    private int totalBedroom;
    private boolean isPetAllowed;
    private boolean hasGarden;

    public ResidentialProperty(String address, double price, PropertyStatus status, int totalRoom, int totalBedroom, boolean isPetAllowed, boolean hasGarden) {
        super(address, price, status);
        this.totalRoom = totalRoom;
        this.totalBedroom = totalBedroom;
        this.isPetAllowed = isPetAllowed;
        this.hasGarden = hasGarden;
    }

    public int getTotalRoom() {
        return totalRoom;
    }

    public void setTotalRoom(int totalRoom) {
        this.totalRoom = totalRoom;
    }

    public int getTotalBedroom() {
        return totalBedroom;
    }

    public void setTotalBedroom(int totalBedroom) {
        this.totalBedroom = totalBedroom;
    }

    public boolean isPetAllowed() {
        return isPetAllowed;
    }

    public void setPetAllowed(boolean petAllowed) {
        isPetAllowed = petAllowed;
    }

    public boolean isHasGarden() {
        return hasGarden;
    }

    public void setHasGarden(boolean hasGarden) {
        this.hasGarden = hasGarden;
    }

    @Override
    public String toString() {
        return super. toString() +
                " Total Room = " + totalRoom +
                ", Total Bedroom = " + totalBedroom +
                ", PetAllowed = " + isPetAllowed +
                ", Garden = " + hasGarden +
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
                        "Total Room: " + this.totalRoom,
                        "Total Bedroom: " + this.totalBedroom,
                        "Pet Allowed: " + this.isPetAllowed,
                        "Has Garden: " + this.hasGarden
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
