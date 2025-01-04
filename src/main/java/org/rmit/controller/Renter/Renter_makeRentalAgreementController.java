package org.rmit.controller.Renter;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.rmit.Helper.DateUtils;
import org.rmit.Helper.EntityGraphUtils;
import org.rmit.Helper.NotificationUtils;
import org.rmit.Notification.Request;
import org.rmit.database.CommercialPropertyDAO;
import org.rmit.database.RenterDAO;
import org.rmit.database.ResidentialPropertyDAO;
import org.rmit.model.ModelCentral;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Owner;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Property.Property;
import org.rmit.model.Property.RentalPeriod;
import org.rmit.model.Session;

import java.net.URL;
import java.util.*;

public class Renter_makeRentalAgreementController implements Initializable {
    public Renter currentUser = (Renter) Session.getInstance().getCurrentUser();


    public TextField propertySearch_input;
    public Button searchProperty_btn;
    public ComboBox<Property> property_ComboBox;
    public TextField owner_input;
    public ComboBox<Host> host_comboBox;
    public ComboBox<RentalPeriod> rentalPeriod_comboBox;
    public TextField subRenterSearch_input;
    public Button searchRenter_btn;
    public Button submit_btn;
    public ListView<Renter> subRenter_listView;


    public ObjectProperty<Property> selectedProperty = new SimpleObjectProperty<>();
    public ObjectProperty<Owner> selectedOwner = new SimpleObjectProperty<>();
    public ObjectProperty<Host> selectedHost = new SimpleObjectProperty<>();
    public ObjectProperty<Renter> selectedSubRenter = new SimpleObjectProperty<>();
    public ObjectProperty<RentalPeriod> selectedRentalPeriod = new SimpleObjectProperty<>();


    public Button addRenter = new Button("Add");
    public Button removeRenter = new Button("Remove");
    public Set<Renter> selectedSubRenters = new HashSet<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addRenter.setOnAction(e -> addRenterToList());
        removeRenter.setOnAction(e -> removeSubRenter());
        submit_btn.setOnAction(e -> submitRA());
        searchProperty_btn.setOnAction(e -> searchProperty());
        searchRenter_btn.setOnAction(e -> searchRenter());
        property_ComboBox.setOnAction(e -> {
            selectedProperty.set(property_ComboBox.getSelectionModel().getSelectedItem());
            selectedOwner.set(property_ComboBox.getSelectionModel().getSelectedItem().getOwner());
        });
        host_comboBox.setOnAction(e -> {
            selectedHost.set(host_comboBox.getSelectionModel().getSelectedItem());
        });
        selectedOwner.addListener((observable, oldValue, newValue) -> {
            owner_input.setText(newValue.getName());
        });
        selectedProperty.addListener((observable, oldValue, newValue) -> {
            host_comboBox.getItems().clear();
            host_comboBox.getItems().addAll(newValue.getHosts());
        });
        rentalPeriod_comboBox.getItems().addAll(
                RentalPeriod.WEEKLY,
                RentalPeriod.FORTNIGHTLY,
                RentalPeriod.FORTNIGHTLY
        );
        rentalPeriod_comboBox.setOnAction(e -> {
            selectedRentalPeriod.set(rentalPeriod_comboBox.getSelectionModel().getSelectedItem());
        });
        subRenterSearch_input.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.isBlank()){
                reloadRenterListView(selectedSubRenters);
            }
        });
        owner_input.setEditable(false);
        customCellFactory();

    }

    private void addRenterToList() {
        System.out.println("Add renter");
        selectedSubRenters.add(subRenter_listView.getSelectionModel().getSelectedItem());
    }

    private void removeSubRenter() {
        System.out.println("Remove renter");
        selectedSubRenters.remove(subRenter_listView.getSelectionModel().getSelectedItem());
    }


    private void searchRenter() {
        if(subRenterSearch_input.getText().isBlank()) return;
        RenterDAO renterDAO = new RenterDAO();
        List<Renter> list = renterDAO.search(subRenterSearch_input.getText(), EntityGraphUtils::SimpleRenterNotification);
        subRenter_listView.getItems().clear();
        subRenter_listView.getItems().addAll(list);
    }


    private void customCellFactory(){
        property_ComboBox.setCellFactory(p -> new ListCell<>(){
            @Override
            protected void updateItem(Property item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getId() + " | " + item.getAddress());
                }
            }

        });
        property_ComboBox.setButtonCell(new ListCell<>(){
            @Override
            protected void updateItem(Property item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getId() + " | " + item.getAddress());
                }
            }
        });

        host_comboBox.setCellFactory(p -> new ListCell<>(){
            @Override
            protected void updateItem(Host item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }

        });
        host_comboBox.setButtonCell(new ListCell<>(){
            @Override
            protected void updateItem(Host item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });


        subRenter_listView.setCellFactory(p -> new ListCell<Renter>() {
            private final Button addRenter = new Button("Add");
            private final Button removeRenter = new Button("Remove");
            {
                addRenter.setOnAction(e -> {
                    Renter item = getItem();
                    if (item != null && !selectedSubRenters.contains(item)) {
                        selectedSubRenters.add(item);  // Add to the selected list
                        updateItem(item, false);  // Update the cell to show the "Remove" button
                    }
                });

                removeRenter.setOnAction(e -> {
                    Renter item = getItem();
                    if (item != null && selectedSubRenters.contains(item)) {
                        selectedSubRenters.remove(item);  // Remove from the selected list
                        reloadRenterListView(selectedSubRenters);
                        updateItem(item, false);  // Update the cell to show the "Add" button
                    }
                });
            }

            @Override
            protected void updateItem(Renter item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.getId() + " - " + item.getName());
                    if(currentUser.getId() == item.getId()) setGraphic(null);
                    else if (selectedSubRenters.contains(item)) {
                        setGraphic(removeRenter);  // Show "Remove" button
                    } else {
                        setGraphic(addRenter);  // Show "Add" button
                    }
                }
            }
        });
    }

    boolean isExist(Renter r){
        for(Renter renter : selectedSubRenters){
            if(renter == r) return true;
        }
        return false;
    }

    private void reloadRenterListView(Set<Renter> renters){
        subRenter_listView.getItems().clear();
        subRenter_listView.getItems().addAll(renters);

    }

    private void searchProperty() {
        if(propertySearch_input.getText().isBlank()) return;
        property_ComboBox.getItems().clear();
        List<Property> list = new ArrayList<>();
        CommercialPropertyDAO commercialPropertyDAO = new CommercialPropertyDAO();
        list.addAll(commercialPropertyDAO.search(propertySearch_input.getText(), EntityGraphUtils::commercialPropertyForSearching));
        ResidentialPropertyDAO residentialPropertyDAO = new ResidentialPropertyDAO();
        list.addAll(residentialPropertyDAO.search(propertySearch_input.getText(), EntityGraphUtils::residentalPropertyForSearching));
        property_ComboBox.getItems().addAll(list);
    }

    private void submitRA() {
        if(!ModelCentral.getInstance().getStartViewFactory().confirmMessage("Are you sure you want to send this rental agreement request?")) return;

        String s = NotificationUtils.buildDaft_RentalAgreement(
                selectedProperty.get().getId(),
                selectedHost.get().getId(),
                selectedProperty.get().getOwner().getId(),
                selectedRentalPeriod.get(),
                selectedSubRenters
                );

        String header = String.format(
                NotificationUtils.HEADER_REQUEST_AGREEMENT,
                selectedProperty.get().getId(),
                selectedProperty.get().getAddress()
        );

        String content = NotificationUtils.buildContent_REQUEST_AGREEMENT(
                selectedHost.get().getName(),
                currentUser.getName(),
                selectedProperty.get().getId() + "",
                selectedProperty.get().getAddress(),
                selectedRentalPeriod.get(),
                selectedProperty.get().getPrice()
        );

        Request request = new Request();
        request.setTimestamp(DateUtils.currentTimestamp());
        request.setSender(currentUser);
        request.addReceiver(selectedHost.get());
        request.setHeader(header);
        request.setContent(content);
        request.setDraftObject(s);

        currentUser.sentNotification(request);

        RenterDAO renterDAO = new RenterDAO();
        renterDAO.update(currentUser);
        System.out.println("Request sent");
    }
}
