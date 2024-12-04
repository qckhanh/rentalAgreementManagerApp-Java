package Model;

import UIHelper.UserInterfaceManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Owner extends Person{
    private List<Property> propertiesOwned = new ArrayList<>();
    private List<Host> hosts = new ArrayList<>();


    public Owner(String name, Date dateOfBirth, String contact) {
        super(name, dateOfBirth, contact);

    }

    public void addProperty(Property property){
        propertiesOwned.add(property);
        property.setOwner(this);
    }

    public List<Property> getPropertiesOwned() {
        return propertiesOwned;
    }

    public void setPropertiesOwned(List<Property> propertiesOwned) {
        this.propertiesOwned = propertiesOwned;
    }

    public List<Host> getHosts() {
        return hosts;
    }

    public void setHosts(List<Host> hosts) {
        this.hosts = hosts;
    }

    public void preview(){
        super.preview();
        List<List<String>> data = new ArrayList<>();
        List<String> header = new ArrayList<>();

        if (propertiesOwned == null || propertiesOwned.isEmpty()) UserInterfaceManager.successMessage("No property found");
        else {
            header = List.of("Property ID", "Address", "Type", "Status");
            for (Property property : propertiesOwned) {
                List<String> row = new ArrayList<>();
                row.add(String.valueOf(property.getId()));
                row.add(property.getAddress());
                row.add(property instanceof CommercialProperty ? "Commercial" : "Residential");
                row.add(property.getStatus().toString());
                data.add(row);
            }
            UserInterfaceManager.printTable(this.name + "'s properties ", header, data);
        }
        if (hosts == null || hosts.isEmpty()) UserInterfaceManager.successMessage("No host found");
        else {
            data.clear();
            header = List.of("Host ID", "Name", "Contact");
            for (Host host : hosts) {
                List<String> row = new ArrayList<>();
                row.add(String.valueOf(host.getId()));
                row.add(host.getName());
                row.add(host.getContact());
                data.add(row);
            }
            UserInterfaceManager.printTable(this.name + "'s hosts ", header, data);
        }

    }




}
