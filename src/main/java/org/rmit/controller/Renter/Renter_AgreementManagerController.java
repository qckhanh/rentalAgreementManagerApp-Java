package org.rmit.controller.Renter;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.rmit.model.Agreement.AgreementStatus;
import org.rmit.model.Agreement.Payment;
import org.rmit.model.Agreement.RentalAgreement;
import org.rmit.model.Persons.Person;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Property.Property;
import org.rmit.model.Property.RentalPeriod;
import org.rmit.model.Session;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

public class Renter_AgreementManagerController implements Initializable {
    public ComboBox propertyFilter_comboBox;
    public ComboBox rentalPeriodFilter_comboBox;
    public TableView rentalAgreement_tableView;
    public ComboBox statusFilter_comboBox;
    public ObjectProperty<Person> currentUser = Session.getInstance().currentUserProperty();

    public ObjectProperty<Property> selectedProperty = new SimpleObjectProperty<>();
    public ObjectProperty<RentalPeriod> selectedRentalPeriod = new SimpleObjectProperty<>();
    public ObjectProperty<AgreementStatus> selectedStatus = new SimpleObjectProperty<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println(((Renter) currentUser.get()).getAgreementList().size());
        propertyFilter_comboBox.setPromptText("Property");
        rentalPeriodFilter_comboBox.setPromptText("Rental Period");
        statusFilter_comboBox.setPromptText("Agreement Status");

        selectedProperty.set(null);
        selectedRentalPeriod.set(RentalPeriod.NONE);
        selectedStatus.set(AgreementStatus.NONE);

        rentalPeriodFilter_comboBox.setOnAction(event -> {
            selectedRentalPeriod.set((RentalPeriod) rentalPeriodFilter_comboBox.getValue());
        });
        rentalPeriodFilter_comboBox.getItems().addAll(
                RentalPeriod.NONE,
                RentalPeriod.DAILY,
                RentalPeriod.MONTHLY,
                RentalPeriod.WEEKLY,
                RentalPeriod.FORTNIGHTLY
        );

        statusFilter_comboBox.setOnAction(event -> {
            selectedStatus.set((AgreementStatus) statusFilter_comboBox.getValue());
        });
        statusFilter_comboBox.getItems().addAll(
                AgreementStatus.NONE,
                AgreementStatus.COMPLETED,
                AgreementStatus.NEW,
                AgreementStatus.ACTIVE
        );
        propertyFilter_comboBox.setOnAction(event -> {
            selectedProperty.set((Property) propertyFilter_comboBox.getValue());
        });
        propertyFilter_comboBox.getItems().addAll(createPropertyComboBox());

        rentalAgreement_tableView.setEditable(true);
        rentalAgreement_tableView.getColumns().addAll(
                createColumn("ID", "agreementId"),
                createColumn("Main Renter", "mainTenant",
                        agreement -> agreement.getMainTenant().namePropertyProperty().get(),
                        agreement -> showPerson(agreement.getMainTenant())
                ),
//                createColumn("Sub-renter", "subTenants",
//                        agreement -> agreement.getSubTenants().size(),
//                        agreement -> showSubRenter(agreement.getSubTenants())
//                ),
                createColumn("Property's Address", "property",
                        agreement -> agreement.getProperty().getAddress(),
                        agreement -> showProperty(agreement.getProperty())
                ),
                createColumn("Host", "host",
                        agreement -> agreement.getHost().namePropertyProperty().get(),
                        agreement -> showPerson(agreement.getHost())
                ),
                createColumn("Period", "period"),
                createColumn("Contract Date", "contractDate"),
                createColumn("Fee", "rentingFee"),
                createColumn("Status", "status")
//                createColumn("Payment", "payments",
//                        agreement -> agreement.getPayments().size(),
//                        agreement -> showPayment(agreement.getPayments())
//                )
        );
//        loadData(((Renter) currentUser.get()).getAgreementList());
//        loadData(((Renter) currentUser.get()).getSubAgreements());
        loadData(((Renter) currentUser.get()).getAllAgreements());

        selectedProperty.addListener((observable, oldValue, newValue) -> {
            Set<RentalAgreement> filteredList = noFilter();
            filteredList = filterByProperty(filteredList, newValue);
            filteredList = filterByRentalPeriod(filteredList, selectedRentalPeriod.get());
            filteredList = filterByStatus(filteredList, selectedStatus.get());
            loadData(filteredList);
            System.out.println(">> Filter by property");
        });

        selectedStatus.addListener((observable, oldValue, newValue) -> {
            Set<RentalAgreement> filteredList = noFilter();
            filteredList = filterByStatus(filteredList, newValue);
            filteredList = filterByProperty(filteredList, selectedProperty.get());
            filteredList = filterByRentalPeriod(filteredList, selectedRentalPeriod.get());
            loadData(filteredList);
            System.out.println(">> Filter by status");
        });

        selectedRentalPeriod.addListener((observable, oldValue, newValue) -> {
            Set<RentalAgreement> filteredList = noFilter();
            filteredList = filterByRentalPeriod(filteredList, newValue);
            filteredList = filterByProperty(filteredList, selectedProperty.get());
            filteredList = filterByStatus(filteredList, selectedStatus.get());
            loadData(filteredList);
            System.out.println(">> Filter by rental period");
        });
    }

    private ObservableList<Property> createPropertyComboBox() {
        Set<Property> properties = new HashSet<>();
        properties.add(null);
        for(RentalAgreement ra : ((Renter) currentUser.get()).getAgreementList()) {
            properties.add(ra.getProperty());
        }
//        for(RentalAgreement ra : ((Renter) currentUser.get()).getSubAgreements()) {
//            properties.add(ra.getProperty());
//        }
        return FXCollections.observableArrayList(properties);
    }

    private void showPerson(Person person) {
        if(person == null) return;
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Person Details");
        alert.setHeaderText("Details of the Person");
        alert.setContentText(
                "Person ID: " + person.getId() + "\n" +
                        "Person Name: " + person.getName() + "\n" +
                        "Person Date of Birth: " + person.getDateOfBirth() + "\n" +
                        "Person Contact: " + person.getContact() + "\n" +
                        "Person Username: " + person.getUsername() + "\n" +
                        "Person Password: " + person.getPassword() + "\n"
        );
        alert.showAndWait();
    }

    private void showPayment(Set<Payment> payments) {
        System.out.println("show payment");
    }

    private void showProperty(Property property) {
        if(property == null) return;
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Person Details");
        alert.setHeaderText("Details of the property: ");
        alert.setContentText(
                "Property ID: " + property.getId() + "\n" +
                        "Property Address: " + property.getAddress() + "\n" +
                        "Property's Owner: " + property.getOwner().getName() + "\n" +
                        "Property Renting Fee: " + property.getPrice()  + "\n" +
                        "Property Status: " + property.getStatus() + "\n"
        );
        alert.showAndWait();
    }

    private void showSubRenter(Set<Renter> subTenants) {
        System.out.println("show sub renter");
    }

    private void loadData(Set<RentalAgreement> list) {
        ObservableList<RentalAgreement> rentalAgreementsTableView = FXCollections.observableArrayList();
        rentalAgreementsTableView.addAll(list);
        rentalAgreement_tableView.setItems(rentalAgreementsTableView);
    }

    private <T> TableColumn<RentalAgreement, T> createColumn(String columnName, String propertyName, Function<RentalAgreement, T> extractor, Consumer<RentalAgreement> onClickAction) {
        TableColumn<RentalAgreement, T> column = new TableColumn<>(columnName);

        column.setCellValueFactory(cellData -> new SimpleObjectProperty<>(extractor.apply(cellData.getValue())));

        // Set a custom cell factory to handle clicks on individual cells
        column.setCellFactory(col -> new TableCell<RentalAgreement, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle(""); // Reset cell style for empty cells
                    setOnMouseClicked(null); // Remove any click listeners when the cell is empty
                } else {
                    setText(item.toString());
                    setOnMouseClicked(event -> {
                        if (event.getClickCount() == 1 && onClickAction != null) {
                            RentalAgreement ra = getTableView().getItems().get(getIndex());
                            onClickAction.accept(ra);
                        }
                    });
                }
            }
        });

        return column;
    }

    //for primitive cell, no clickable
    private TableColumn<RentalAgreement, ?> createColumn(String columnName, String propertyName) {
        TableColumn<RentalAgreement, ?> column = new TableColumn<>(columnName);
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        return column;
    }

    private Set<RentalAgreement> noFilter() {
        return ((Renter) currentUser.get()).getAgreementList();
    }

    private Set<RentalAgreement> filterByProperty(Set<RentalAgreement> list, Property property) {
        if(property == null) return list;
        Set<RentalAgreement> filteredList = new HashSet<>();
        for(RentalAgreement ra : list) {
            if(ra.getProperty().equals(property)) {
                filteredList.add(ra);
            }
        }
        return filteredList;
    }

    private Set<RentalAgreement> filterByRentalPeriod(Set<RentalAgreement> list, RentalPeriod rentalPeriod) {
        if(rentalPeriod.equals(RentalPeriod.NONE)) return list;
        Set<RentalAgreement> filteredList = new HashSet<>();
        for(RentalAgreement ra : list) {
            if(ra.getPeriod().equals(rentalPeriod)) {
                filteredList.add(ra);
            }
        }
        return filteredList;
    }

    private Set<RentalAgreement> filterByStatus(Set<RentalAgreement> list, AgreementStatus status) {
        if (status.equals(AgreementStatus.NONE)) return list;

        Set<RentalAgreement> filteredList = new HashSet<>();
        for (RentalAgreement ra : list) {
            if (ra.getStatus().equals(status)) {
                filteredList.add(ra);
            }
        }
        return filteredList;
    }

}
