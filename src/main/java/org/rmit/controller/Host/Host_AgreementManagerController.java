package org.rmit.controller.Host;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.rmit.Helper.NotificationUtils;
import org.rmit.Notification.NormalNotification;
import org.rmit.database.HostDAO;
import org.rmit.model.Agreement.AgreementStatus;
import org.rmit.model.Agreement.Payment;
import org.rmit.model.Agreement.RentalAgreement;
import org.rmit.view.ViewCentral;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Person;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Property.Property;
import org.rmit.model.Property.RentalPeriod;
import org.rmit.model.Session;

import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class Host_AgreementManagerController implements Initializable {
    public ComboBox propertyFilter_comboBox;
    public ComboBox rentalPeriodFilter_comboBox;
    public TableView rentalAgreement_tableView;
    public ComboBox statusFilter_comboBox;

    public ObjectProperty<Person> currentUser = Session.getInstance().currentUserProperty();

    public ObjectProperty<Property> selectedProperty = new SimpleObjectProperty<>();
    public ObjectProperty<RentalPeriod> selectedRentalPeriod = new SimpleObjectProperty<>();
    public ObjectProperty<AgreementStatus> selectedStatus = new SimpleObjectProperty<>();
    public ComboBox unpaidCbox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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

        unpaidCbox.getItems().addAll(
                "UNPAID",
                "NONE"
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
        rentalAgreement_tableView.getColumns().addAll(
                createColumn("ID", "agreementId"),
                createColumn("Main Renter", "mainTenant",
                        agreement -> agreement.getMainTenant().namePropertyProperty().get(),
                        agreement -> showPerson(agreement.getMainTenant())
                ),
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
        );
        loadData(((Host) currentUser.get()).getRentalAgreements());

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

        unpaidCbox.setOnAction(e -> {
            Set<RentalAgreement> filteredList = noFilter();
            if (unpaidCbox.getValue().equals("UNPAID")) {
                filteredList = filterByUnpaid(filteredList);
                filteredList = filterByStatus(filteredList, selectedStatus.get());
                filteredList = filterByProperty(filteredList, selectedProperty.get());
                filteredList = filterByRentalPeriod(filteredList, selectedRentalPeriod.get());
                loadData(filteredList);
            }
            else {
                loadData(filteredList);
            }
        });

        customCellFactory();
    }

    private ObservableList<Property> createPropertyComboBox() {
        Set<Property> properties = new HashSet<>();
        properties.add(null);
        for(RentalAgreement ra : ((Host) currentUser.get()).getRentalAgreements()) {
            properties.add(ra.getProperty());
        }
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

    private void customCellFactory() {
        TableColumn<RentalAgreement, Void> colBtn = new TableColumn<>("Action");

        colBtn.setCellValueFactory(param -> null);

        Callback<TableColumn<RentalAgreement, Void>, TableCell<RentalAgreement, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<RentalAgreement, Void> call(final TableColumn<RentalAgreement, Void> param) {
                final TableCell<RentalAgreement, Void> cell = new TableCell<>() {
                    private final Button btn = new Button("Action");

                    {
                        btn.setOnAction(e -> {
                            RentalAgreement data = getTableView().getItems().get(getIndex());
                            if(!ViewCentral.getInstance().getStartViewFactory().confirmMessage("Are you sure?")) return;
                            String head = "Rent Payment Reminder";
                            int num = expectedPayments(data.getContractDate(), data.getPeriod()) - data.getPayments().size();
                            String content = "Please pay your rent. YOU ARE LACKING " + num + "PAYMENTS";
                            List<Person> l = new ArrayList<>();
                            l.add(data.getMainTenant());
                            System.out.println(data.getMainTenant());
                            NormalNotification notification = (NormalNotification) NotificationUtils.createNormalNotification(
                                    data.getHost(),
                                    l,
                                    head,
                                    content
                            );
                            currentUser.get().sentNotification(notification);
                            HostDAO hostDAO = new HostDAO();
                            hostDAO.update((Host) currentUser.get());
                            System.out.println(data);
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            RentalAgreement data = getTableView().getItems().get(getIndex());
                            boolean unpaid = isUnpaid(data);
                            if (unpaid) {
                                setGraphic(btn);
                            } else {
                                setGraphic(null);
                            }
                        }
                    }
                };
                return cell;
            }
        };

        colBtn.setCellFactory(cellFactory);
        rentalAgreement_tableView.getColumns().add(colBtn);
    }

    private Set<RentalAgreement> filterByUnpaid(Set<RentalAgreement> list) {
        Set<RentalAgreement> filtered = new HashSet<>();
        for (RentalAgreement ra : list) {
            if (isUnpaid(ra)) {
                filtered.add(ra);
            }
        }
        return filtered;
    }

    boolean isUnpaid(RentalAgreement ra) {
        return expectedPayments(ra.getContractDate(), ra.getPeriod()) > ra.getPayments().size();
    }

    private int expectedPayments(LocalDate start, RentalPeriod r) {
        long days = ChronoUnit.DAYS.between(start, LocalDate.now());
        if (r.equals(RentalPeriod.DAILY)) {
            return (int) days;
        } else if (r.equals(RentalPeriod.WEEKLY)) {
            return (int) days / 7;
        }
        else if (r.equals(RentalPeriod.MONTHLY)) {
            return (int) days / 30;
        }
        else if (r.equals(RentalPeriod.FORTNIGHTLY)) {
            return (int) days / 14;
        }
        return 0;
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
        return ((Host) currentUser.get()).getRentalAgreements();
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
