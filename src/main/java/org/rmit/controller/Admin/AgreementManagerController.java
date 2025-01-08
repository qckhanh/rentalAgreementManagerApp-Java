package org.rmit.controller.Admin;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.rmit.Helper.DatabaseUtil;
import org.rmit.database.RentalAgreementDAO;
import org.rmit.model.Agreement.AgreementStatus;
import org.rmit.model.Agreement.RentalAgreement;
import org.rmit.model.ModelCentral;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Property.CommercialProperty;
import org.rmit.model.Property.Property;
import org.rmit.model.Property.RentalPeriod;

import java.net.URL;
import java.util.*;
import java.util.function.Function;

public class AgreementManagerController implements Initializable {
    public Button addAgreementButton;
    public Button updateAgreeementButton;
    public Button deleteAgreementButton;
    public Button saveToDB_btn;


    public TableView<RentalAgreement> agreements_tableView;
    public ComboBox<AgreementStatus> agreementStatusFilter_comboBox;
    public ComboBox<Property> propertyFilter_comboBox;
    public ComboBox<Host> host_comboBox;
    public ComboBox<Renter> mainRenter_comboBox;
    public ListView<Renter> subRenter_listView;
    public ComboBox<Property> property_comboBox;
    public ComboBox<RentalPeriod> rentalPeriod_comboBox;
    public ComboBox<AgreementStatus> status_comboBox;

    public TextField id_input;
    public TextField fee_input;
    public DatePicker contractDate_datePicker;

    private ObjectProperty<RentalAgreement> selectedRentalAgreement = new SimpleObjectProperty<>();
    private ObservableList<RentalAgreement> rentalAgreementsObservableList = FXCollections.observableArrayList();
    private List<RentalAgreement> rentalAgreementsList = ModelCentral.getInstance().getAdminViewFactory().getAllRentalAgreement();
    private Set<Renter> selectedSubRenters = new HashSet<>();

    private ObjectProperty<Renter> selectedMainRenter = new SimpleObjectProperty<>();
    private ObjectProperty<Property> selectedProperty = new SimpleObjectProperty<>();
    private ObjectProperty<Host> selectedHost = new SimpleObjectProperty<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setUpButtonAction();
        setUpDataBehavior();
        setUpDataView();
    }

    private void setUpButtonAction(){
//        saveToDB_btn.setVisible(false);
//        saveToDB_btn.setOnAction(e -> saveToDB());
//        addAgreementButton.setOnAction(e -> addAgreement());
//        updateAgreeementButton.setOnAction(e -> updateAgreement());
//        deleteAgreementButton.setOnAction(e -> deleteAgreement());
    }

    private void saveToDB() {
        if(isTextFieldChanged(new RentalAgreement())){
            if(!ModelCentral.getInstance().getStartViewFactory().confirmMessage("Do you want to save this agreement?")) return;
            RentalAgreement ra = new RentalAgreement();
            ra.setProperty(selectedProperty.get());
            ra.setPeriod(rentalPeriod_comboBox.getValue());
            ra.setRentingFee(Double.parseDouble(fee_input.getText()));
            ra.setStatus(status_comboBox.getValue());
            ra.setContractDate(contractDate_datePicker.getValue());
            ra.setHost(selectedHost.get());
            ra.setMainTenant(selectedMainRenter.get());
            ra.setSubTenants(selectedSubRenters);

            RentalAgreementDAO rentalAgreementDAO = new RentalAgreementDAO();
            DatabaseUtil.warmUp();
            boolean isUpdate = rentalAgreementDAO.update(ra);
            if(isUpdate){
                rentalAgreementsList.set(rentalAgreementsList.indexOf(ra), ra);
                rentalAgreementsObservableList.setAll(rentalAgreementsList);
                System.out.println("Agreement updated");
            }
            else{
                System.out.println("Agreement not updated");
            }
        }
    }

    private void addAgreement() {
        clearInput();
//        saveToDB_btn.setVisible(true);
        setEditable(true);

        mainRenter_comboBox.getItems().clear();
        mainRenter_comboBox.getItems().addAll(ModelCentral.getInstance().getAdminViewFactory().getAllRenter());

        subRenter_listView.getItems().clear();
        subRenter_listView.getItems().addAll(ModelCentral.getInstance().getAdminViewFactory().getAllRenter());
    }

    private void updateAgreement() {
        boolean isEditable = fee_input.isEditable();
        setEditable(!isEditable);
        if(isTextFieldChanged(selectedRentalAgreement.get())){
            if(!ModelCentral.getInstance().getStartViewFactory().confirmMessage("Do you want to save this agreement?")) return;
            RentalAgreement ra = selectedRentalAgreement.get();
            ra.setProperty(selectedProperty.get());
            ra.setPeriod(rentalPeriod_comboBox.getValue());
            ra.setRentingFee(Double.parseDouble(fee_input.getText()));
            ra.setStatus(status_comboBox.getValue());
            ra.setContractDate(contractDate_datePicker.getValue());
            ra.setHost(selectedHost.get());
            ra.setMainTenant(selectedMainRenter.get());
            ra.setSubTenants(selectedSubRenters);

            RentalAgreementDAO rentalAgreementDAO = new RentalAgreementDAO();
            DatabaseUtil.warmUp();
            boolean isUpdate = rentalAgreementDAO.update(ra);
            if(isUpdate){
                rentalAgreementsList.set(rentalAgreementsList.indexOf(ra), ra);
                rentalAgreementsObservableList.setAll(rentalAgreementsList);
                System.out.println("Agreement updated");
            }
            else{
                System.out.println("Agreement not updated");
            }

        }

    }

    private void setUpDataBehavior() {
        agreements_tableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            selectedRentalAgreement.set(newValue);
//            saveToDB_btn.setVisible(false);
            showInfor(newValue);
        });
        agreements_tableView.setItems(rentalAgreementsObservableList);
        rentalAgreementsObservableList.setAll(rentalAgreementsList);

        property_comboBox.setButtonCell(new ListCell<Property>() {
            @Override
            protected void updateItem(Property item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("All");
                } else {
                    setText("ID: " + item.getId() + " - " +  item.addressPropertyProperty().get());
                }
            }
        });

        property_comboBox.setCellFactory(param -> new ListCell<Property>() {
            @Override
            protected void updateItem(Property item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("All");
                } else {
                    setText("ID: " + item.getId() + " - " +  item.addressPropertyProperty().get());
                }
            }
        });

        propertyFilter_comboBox.setButtonCell(new ListCell<Property>() {
            @Override
            protected void updateItem(Property item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("All");
                } else {
                    setText("ID: " + item.getId() + " - " +  item.addressPropertyProperty().get());
                }
            }
        });

        propertyFilter_comboBox.setCellFactory(param -> new ListCell<Property>() {
            @Override
            protected void updateItem(Property item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("All");
                } else {
                    setText("ID: " + item.getId() + " - " +  item.addressPropertyProperty().get());
                }
            }
        });



        host_comboBox.setButtonCell(new ListCell<Host>() {
            @Override
            protected void updateItem(Host item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("All");
                } else {
                    setText("ID: " + item.getId() + " - " +  item.namePropertyProperty().get());
                }
            }
        });

        host_comboBox.setCellFactory(param -> new ListCell<Host>() {
            @Override
            protected void updateItem(Host item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("All");
                } else {
                    setText("ID: " + item.getId() + " - " +  item.namePropertyProperty().get());
                }
            }
        });

        mainRenter_comboBox.setButtonCell(new ListCell<Renter>() {
            @Override
            protected void updateItem(Renter item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("All");
                } else {
                    setText("ID: " + item.getId() + " - " +  item.namePropertyProperty().get());
                }
            }
        });

        mainRenter_comboBox.setCellFactory(param -> new ListCell<Renter>() {
            @Override
            protected void updateItem(Renter item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("All");
                } else {
                    setText("ID: " + item.getId() + " - " +  item.namePropertyProperty().get());
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

                        subRenter_listView.getItems().clear();
                        subRenter_listView.getItems().addAll(item);
                        subRenter_listView.getItems().addAll(ModelCentral.getInstance().getAdminViewFactory().getAllRenter());
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
                    if( selectedMainRenter.get() == null  || selectedMainRenter.get().getId() == item.getId()) setGraphic(null);
                    else if (selectedSubRenters.contains(item)) {
                        setGraphic(removeRenter);  // Show "Remove" button
                    } else {
                        setGraphic(addRenter);  // Show "Add" button
                    }

                }
            }
        });

        property_comboBox.setOnAction(e -> {
            selectedProperty.set(property_comboBox.getSelectionModel().getSelectedItem());
//            selectedOwner.set(property_ComboBox.getSelectionModel().getSelectedItem().getOwner());
//            resetErrorLabels();
        });

        selectedProperty.addListener((o, old, neww) -> {
            host_comboBox.getItems().clear();
            if(neww != null){
                host_comboBox.getItems().addAll(neww.getHosts());
            }
        });

        mainRenter_comboBox.getSelectionModel().selectedItemProperty().addListener((o, old, neww) -> {
            if(neww != null){
                selectedMainRenter.set(neww);
                selectedSubRenters.remove(neww);
            }
        });

        host_comboBox.getSelectionModel().selectedItemProperty().addListener((o, old, neww) -> {
            selectedHost.set(neww);
            System.out.println("Selected host: " + neww);
        });

    }

    private void setUpDataView(){
        agreements_tableView.getColumns().addAll(
                createColumn("ID", "agreementId"),
                createColumn("Main Renter", "mainRenter",
                        ra -> ra.mainTenantPropertyProperty().get().namePropertyProperty().get()),
                createColumn("Contract Date", "contractDate"),
                createColumn("Property Address", "property",
                        ra -> ra.getProperty().addressPropertyProperty().get())
        );
        propertyFilter_comboBox.getItems().addAll((Property) new CommercialProperty());
        propertyFilter_comboBox.getItems().addAll(ModelCentral.getInstance().getAdminViewFactory().getAllProperty());
        agreementStatusFilter_comboBox.getItems().addAll(AgreementStatus.values());
        rentalPeriod_comboBox.getItems().addAll(RentalPeriod.values());
        status_comboBox.getItems().addAll(AgreementStatus.values());
        mainRenter_comboBox.getItems().addAll(ModelCentral.getInstance().getAdminViewFactory().getAllRenter());
        property_comboBox.getItems().addAll(ModelCentral.getInstance().getAdminViewFactory().getAllProperty());
        subRenter_listView.setItems(FXCollections.observableArrayList(ModelCentral.getInstance().getAdminViewFactory().getAllRenter()));
    }

    private void showInfor(RentalAgreement newValue) {
        if(newValue == null) return;
        setEditable(false);

        id_input.setText(newValue.getAgreementId() + "");
        fee_input.setText(newValue.rentingFeePropertyProperty().get() + "");
        contractDate_datePicker.setValue(newValue.contractDatePropertyProperty().get());

        property_comboBox.setValue(newValue.getProperty());
        rentalPeriod_comboBox.setValue(newValue.periodPropertyProperty().get());
        status_comboBox.setValue(newValue.statusPropertyProperty().get());
        mainRenter_comboBox.setValue(newValue.mainTenantPropertyProperty().get());



        host_comboBox.getItems().clear();
        host_comboBox.getItems().addAll(selectedProperty.get().getHosts());
        host_comboBox.setValue(newValue.getHost());



        subRenter_listView.getItems().clear();
        subRenter_listView.getItems().addAll(newValue.getSubTenants());
        subRenter_listView.getItems().addAll(ModelCentral.getInstance().getAdminViewFactory().getAllRenter());

        selectedSubRenters.clear();
        selectedSubRenters.addAll(newValue.getSubTenants());
    }

    private void setEditable(boolean isEditable) {
        id_input.setEditable(false);
        fee_input.setEditable(isEditable);
        contractDate_datePicker.setDisable(!isEditable);
        contractDate_datePicker.setEditable(isEditable);
        property_comboBox.setDisable(!isEditable);
        rentalPeriod_comboBox.setDisable(!isEditable);
        status_comboBox.setDisable(!isEditable);
        host_comboBox.setDisable(!isEditable);
        mainRenter_comboBox.setDisable(!isEditable);
        subRenter_listView.setDisable(!isEditable);
    }

    private void clearInput() {
        id_input.clear();
        fee_input.clear();
        contractDate_datePicker.setValue(null);
        host_comboBox.setValue(null);
        host_comboBox.getItems().clear();

        property_comboBox.setValue(null);
        rentalPeriod_comboBox.setValue(null);
        status_comboBox.setValue(null);
        mainRenter_comboBox.setValue(null);
        subRenter_listView.getItems().clear();
    }

    private boolean isTextFieldChanged(RentalAgreement ra){
        if(ra == null) return false;
        if(!property_comboBox.getValue().equals(ra.getProperty())) return true;
        if(!rentalPeriod_comboBox.getValue().equals(ra.periodPropertyProperty().get())) return true;
        if(!status_comboBox.getValue().equals(ra.statusPropertyProperty().get())) return true;
        if(!host_comboBox.getValue().equals(ra.getHost())) return true;
        if(!mainRenter_comboBox.getValue().equals(ra.mainTenantPropertyProperty().get())) return true;
        for(Renter renter: selectedSubRenters){
            if(!ra.getSubTenants().contains(renter)) return true;
        }
        return false;
    }
    // Helper Method:
    private void deleteAgreement() {
        RentalAgreement ra = selectedRentalAgreement.get();
        if(ra == null) return;
        if(ra.getStatus() != null){
            if(!ra.getStatus().equals(AgreementStatus.COMPLETED)){
                System.out.println("Cannot delete this agreement because it is not completed");
                return;
            }
        }
        if(!ModelCentral.getInstance().getStartViewFactory().confirmMessage("Do you want to delete this agreement?")) return;
        RentalAgreementDAO rentalAgreementDAO = new RentalAgreementDAO();

        ra.setHost(null);
//        ra.setMainTenant(null);
//        ra.setSubTenants(null);
//        ra.setProperty(null);
        DatabaseUtil.warmUp();
        rentalAgreementDAO.update(ra);
        DatabaseUtil.warmUp();
        boolean isDelete = rentalAgreementDAO.delete(ra);
        if(isDelete){
            rentalAgreementsList.remove(ra);
            rentalAgreementsObservableList.setAll(rentalAgreementsList);
            System.out.println("Agreement deleted");
        }
        else{
            System.out.println("Agreement not deleted");
        }

    }

    private <S, T> TableColumn<S, T> createColumn(String columnName, String propertyName) {
        TableColumn<S, T> column = new TableColumn<>(columnName);
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        return column;
    }

    private <S, T> TableColumn<S, T> createColumn(String columnName, String propertyName, Function<S, T> extractor) {
        TableColumn<S, T> column = new TableColumn<>(columnName);

        column.setCellValueFactory(cellData -> new SimpleObjectProperty<>(extractor.apply(cellData.getValue())));
        column.setCellFactory(col -> new TableCell<S, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle(""); // Reset cell style for empty cells
                    setOnMouseClicked(null); // Remove any click listeners when the cell is empty
                } else {
                    setText(item.toString());
                }
            }
        });

        return column;
    }
}
