package org.rmit.view.Admin;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.rmit.Helper.EntityGraphUtils;
import org.rmit.controller.Admin.AdminController;
import org.rmit.database.*;
import org.rmit.model.Agreement.Payment;
import org.rmit.model.Agreement.RentalAgreement;
import org.rmit.model.Persons.Admin;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Owner;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Property.CommercialProperty;
import org.rmit.model.Property.Property;
import org.rmit.model.Property.ResidentialProperty;

import java.util.ArrayList;
import java.util.List;

public class AdminViewFactory {
    String ADMIN_PATH = "/org/rmit/demo/FXMLs/Admin/";
    private ObjectProperty<ADMIN_MENU_OPTION> selectedMenuItem;
    private AnchorPane admin_renterManagerView;
    private AnchorPane admin_dashboardView;
    private AnchorPane admin_editProfileView;
    private AnchorPane admin_agreementManagerView;
    private AnchorPane admin_hostManagerView;
    private AnchorPane admin_ownerManagerView;
    private AnchorPane admin_paymentManagerView;
    private AnchorPane admin_adminManagerView;
    private AnchorPane admin_propertyManagerView;

    private ObjectProperty<List<Renter>> allRenter = new SimpleObjectProperty<>();
    private ObjectProperty<List<Host>> allHost = new SimpleObjectProperty<>();
    private ObjectProperty<List<Owner>> allOwner = new SimpleObjectProperty<>();
    private ObjectProperty<List<Admin>> allAdmin = new SimpleObjectProperty<>();
    private ObjectProperty<List<CommercialProperty>> allCommercialProperty = new SimpleObjectProperty<>();
    private ObjectProperty<List<ResidentialProperty>> allResidentialProperty = new SimpleObjectProperty<>();
    private ObjectProperty<List<Property>> allProperty = new SimpleObjectProperty<>();

    private ObjectProperty<List<Payment>> allPayment = new SimpleObjectProperty<>();
    private ObjectProperty<List<RentalAgreement>> allRentalAgreement = new SimpleObjectProperty<>();

    public AdminViewFactory() {
        selectedMenuItem = new SimpleObjectProperty<>(ADMIN_MENU_OPTION.DASHBOARD);      // default view

        RenterDAO renterDAO = new RenterDAO();
        HostDAO hostDAO = new HostDAO();
        OwnerDAO ownerDAO = new OwnerDAO();
        AdminDAO adminDAO = new AdminDAO();
        CommercialPropertyDAO commercialPropertyDAO = new CommercialPropertyDAO();
        ResidentialPropertyDAO residentialPropertyDAO = new ResidentialPropertyDAO();
        RentalAgreementDAO rentalAgreementDAO = new RentalAgreementDAO();
        PaymentDAO paymentDAO = new PaymentDAO();

        allRenter.set(renterDAO.getAll(EntityGraphUtils::SimpleRenterFull));
        allHost.set(hostDAO.getAll(EntityGraphUtils::SimpleHostFull));
        allOwner.set(ownerDAO.getAll(EntityGraphUtils::SimpleOwnerFull));
        allAdmin.set(adminDAO.getAll(EntityGraphUtils::SimpleAdminFull));
        allCommercialProperty.set(commercialPropertyDAO.getAll(EntityGraphUtils::SimpleCommercialPropertyFull));
        allResidentialProperty.set(residentialPropertyDAO.getAll(EntityGraphUtils::SimpleResidentialPropertyFull));

        List<Property> combine = new ArrayList<>();
        combine.addAll(allCommercialProperty.get());
        combine.addAll(allResidentialProperty.get());
        allProperty.set(combine);

        allRentalAgreement.set(rentalAgreementDAO.getAll(EntityGraphUtils::SimpleRentalAgreementFull));
        allPayment.set(paymentDAO.getAll(EntityGraphUtils::SimplePaymentFull));
    }

    // start admin view when user login as admin
    public void startAdminView(){
        FXMLLoader load = new FXMLLoader(getClass().getResource(ADMIN_PATH + "admin.fxml"));
        AdminController controller = new AdminController();
        load.setController(controller);
        createStage(load);
    }

    // generate admin view for each menu option
    public AnchorPane getAdmin_editProfileView(){
        if (admin_editProfileView == null){
            try {
                admin_editProfileView = new FXMLLoader(getClass().getResource(ADMIN_PATH + "editProfileAdmin.fxml")).load();
            } catch (Exception e){
                System.out.println("Error loading renter manager.fxml");
            }
        }
        return admin_editProfileView;
    }

    public AnchorPane getAdmin_dashboardView() {
        if (admin_dashboardView == null){
            try {
                admin_dashboardView = new FXMLLoader(getClass().getResource(ADMIN_PATH + "dashboardAdmin.fxml")).load();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return admin_dashboardView;
    }

    public AnchorPane getRenterManagerView() {
        if (admin_renterManagerView == null){
            try {
                admin_renterManagerView = new FXMLLoader(getClass().getResource(ADMIN_PATH + "renterManager.fxml")).load();
            } catch (Exception e){
                System.out.println("Error loading renter manager.fxml");
            }
        }
        return admin_renterManagerView;
    }

    public AnchorPane getAdmin_agreementManagerView() {
        if (admin_agreementManagerView == null){
            try {
                admin_agreementManagerView = new FXMLLoader(getClass().getResource(ADMIN_PATH + "agreementManager.fxml")).load();
            } catch (Exception e){
                System.out.println("Error loading agreement manager.fxml");
            }
        }
        return admin_agreementManagerView;
    }

    public AnchorPane getAdmin_HostManagerView() {
        if (admin_hostManagerView == null){
            try {
                admin_hostManagerView = new FXMLLoader(getClass().getResource(ADMIN_PATH + "hostManager.fxml")).load();
            } catch (Exception e){
                System.out.println("Error loading host manager.fxml");
            }
        }
        return admin_hostManagerView;
    }

    public AnchorPane getAdmin_OwnerManagerView() {
        if (admin_ownerManagerView == null){
            try {
                admin_ownerManagerView = new FXMLLoader(getClass().getResource(ADMIN_PATH + "ownerManager.fxml")).load();
            } catch (Exception e){
                System.out.println("Error loading owner manager.fxml");
            }
        }
        return admin_ownerManagerView;
    }

    public AnchorPane getAdmin_PaymentManagerView() {
        if (admin_paymentManagerView == null){
            try {
                admin_paymentManagerView = new FXMLLoader(getClass().getResource(ADMIN_PATH + "paymentManager.fxml")).load();
            } catch (Exception e){
                System.out.println("Error loading payment manager.fxml");
            }
        }
        return admin_paymentManagerView;
    }

    public AnchorPane getAdmin_AdminManagerView() {
        if (admin_adminManagerView == null){
            try {
                admin_adminManagerView = new FXMLLoader(getClass().getResource(ADMIN_PATH + "adminManager.fxml")).load();
            } catch (Exception e){
                System.out.println("Error loading admin manager.fxml");
                e.printStackTrace();
            }
        }
        return admin_adminManagerView;
    }

    public AnchorPane getPropertyManagerView() {
        if(admin_adminManagerView == null){
            try{
                admin_propertyManagerView = new FXMLLoader(getClass().getResource(ADMIN_PATH + "propertyManager.fxml")).load();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        return admin_propertyManagerView;
    }

    // helper method:
    private void createStage(FXMLLoader load) {
        Scene scene = null;
        try {
            scene = new Scene(load.load());
        } catch (Exception e) {
            e.printStackTrace();
        }

        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void resetView(){
        // set all view to null
        admin_renterManagerView = null;
        admin_dashboardView = null;
        admin_editProfileView = null;
    }

    // Setter and Getters:
    public ADMIN_MENU_OPTION getSelectedMenuItem() {
        return selectedMenuItem.get();
    }

    public ObjectProperty<ADMIN_MENU_OPTION> selectedMenuItemProperty() {
        return selectedMenuItem;
    }

    public void setSelectedMenuItem(ADMIN_MENU_OPTION selectedMenuItem) {
        this.selectedMenuItem.set(selectedMenuItem);
    }


    public List<RentalAgreement> getAllRentalAgreement() {
        return allRentalAgreement.get();
    }

    public ObjectProperty<List<RentalAgreement>> allRentalAgreementProperty() {
        return allRentalAgreement;
    }

    public void setAllRentalAgreement(List<RentalAgreement> allRentalAgreement) {
        this.allRentalAgreement.set(allRentalAgreement);
    }

    public List<Payment> getAllPayment() {
        return allPayment.get();
    }

    public ObjectProperty<List<Payment>> allPaymentProperty() {
        return allPayment;
    }

    public void setAllPayment(List<Payment> allPayment) {
        this.allPayment.set(allPayment);
    }

    public List<ResidentialProperty> getAllResidentialProperty() {
        return allResidentialProperty.get();
    }

    public ObjectProperty<List<ResidentialProperty>> allResidentialPropertyProperty() {
        return allResidentialProperty;
    }

    public void setAllResidentialProperty(List<ResidentialProperty> allResidentialProperty) {
        this.allResidentialProperty.set(allResidentialProperty);
    }

    public List<CommercialProperty> getAllCommercialProperty() {
        return allCommercialProperty.get();
    }

    public ObjectProperty<List<CommercialProperty>> allCommercialPropertyProperty() {
        return allCommercialProperty;
    }

    public void setAllCommercialProperty(List<CommercialProperty> allCommercialProperty) {
        this.allCommercialProperty.set(allCommercialProperty);
    }

    public List<Admin> getAllAdmin() {
        return allAdmin.get();
    }

    public ObjectProperty<List<Admin>> allAdminProperty() {
        return allAdmin;
    }

    public void setAllAdmin(List<Admin> allAdmin) {
        this.allAdmin.set(allAdmin);
    }

    public List<Owner> getAllOwner() {
        return allOwner.get();
    }

    public ObjectProperty<List<Owner>> allOwnerProperty() {
        return allOwner;
    }

    public void setAllOwner(List<Owner> allOwner) {
        this.allOwner.set(allOwner);
    }

    public List<Host> getAllHost() {
        return allHost.get();
    }

    public ObjectProperty<List<Host>> allHostProperty() {
        return allHost;
    }

    public void setAllHost(List<Host> allHost) {
        this.allHost.set(allHost);
    }

    public List<Renter> getAllRenter() {
        return allRenter.get();
    }

    public ObjectProperty<List<Renter>> allRenterProperty() {
        return allRenter;
    }

    public void setAllRenter(List<Renter> allRenter) {
        this.allRenter.set(allRenter);
    }

    public List<Property> getAllProperty() {
        return allProperty.get();
    }

    public ObjectProperty<List<Property>> allPropertyProperty() {
        return allProperty;
    }

    public void setAllProperty(List<Property> allProperty) {
        this.allProperty.set(allProperty);
    }
}
