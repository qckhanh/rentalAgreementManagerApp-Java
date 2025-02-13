package org.rmit.view.Renter;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.rmit.controller.Renter.RenterController;

public class RenterViewFactory {
    String RENTER_PATH = "/org/rmit/demo/FXMLs/Renter/";
    private ObjectProperty<RENTER_MENU_OPTION> selectedMenuItem;  // for tracking which menu option is selected

    private AnchorPane renter_editProfileView;
    private AnchorPane renter_dashboardView;
    private AnchorPane renter_paymentManagerView;
    private AnchorPane renter_agreementManagerView;
    private AnchorPane renter_makePaymentView;
    private AnchorPane renter_makeRentalAgreementView;
    private AnchorPane renter_notificationView;


    public RenterViewFactory() {
        selectedMenuItem = new SimpleObjectProperty<>(RENTER_MENU_OPTION.DASHBOARD);      // default view
    }

    //start renter view when user login as renter
    public void startRenterView(){
        FXMLLoader renterLoad = new FXMLLoader(getClass().getResource(RENTER_PATH + "renter.fxml"));
        RenterController renterController = new RenterController();
        renterLoad.setController(renterController);
        createStage(renterLoad);
    }

    //generate renter view for each menu option
    public AnchorPane getRenter_EditProfileView(){
        if (renter_editProfileView == null){
            try {
                renter_editProfileView = new FXMLLoader(getClass().getResource(RENTER_PATH + "editProfileRenter.fxml")).load();
            } catch (Exception e){
                System.out.println("Error loading edit profile.fxml");
            }
        }
        return renter_editProfileView;
    }
    public AnchorPane getRenter_NotificationView(){
        if (renter_notificationView == null){
            try {
                renter_notificationView = new FXMLLoader(getClass().getResource(RENTER_PATH + "notificationRenter.fxml")).load();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return renter_notificationView;
    }
    public AnchorPane getRenter_dashboardView(){
        if (null == null){
            try {
                renter_dashboardView = new FXMLLoader(getClass().getResource(RENTER_PATH + "dashboardRenter.fxml")).load();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return renter_dashboardView;
    }
    public AnchorPane getRenter_paymentManagerView(){
        if (1 == 1){
            try {
                renter_paymentManagerView = new FXMLLoader(getClass().getResource(RENTER_PATH + "paymentManagerRenter.fxml")).load();
            } catch (Exception e){
                System.out.println("Error loading paymentManagerRenter.fxml");
            }
        }
        return renter_paymentManagerView;
    }
    public AnchorPane getRenter_agreementManagerView(){
        if (renter_agreementManagerView == null){
            try {
                renter_agreementManagerView = new FXMLLoader(getClass().getResource(RENTER_PATH + "agreementManagerRenter.fxml")).load();
            } catch (Exception e){
                e.printStackTrace();
                System.out.println("Error loading paymentManagerRenter.fxml");
            }
        }
        return renter_agreementManagerView;
    }
    public AnchorPane getRenter_makePaymentView(){
        if(renter_makePaymentView == null){
            try {
                renter_makePaymentView = new FXMLLoader(getClass().getResource(RENTER_PATH + "makePaymentRenter.fxml")).load();
            } catch (Exception e){
                System.out.println("Error loading makePaymentRenter.fxml");
            }
        }
        return renter_makePaymentView;
    }
    public AnchorPane getRenter_makeRentalAgreementView(){
        if(renter_makeRentalAgreementView == null){
            try {
                renter_makeRentalAgreementView = new FXMLLoader(getClass().getResource(RENTER_PATH + "makeRentalAgreementRenter.fxml")).load();
            } catch (Exception e){
                System.out.println("Error loading makeRentalAgreementRenter.fxml");
            }
        }
        return renter_makeRentalAgreementView;
    }

    //helper method
    private void createStage(FXMLLoader loader) {
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage. show();
    }
    public void resetView(){
        renter_editProfileView = null;
        renter_dashboardView = null;
        renter_paymentManagerView = null;
        renter_agreementManagerView = null;
        renter_makePaymentView = null;
        renter_makeRentalAgreementView = null;
        renter_notificationView = null;
    }

    //getter and setter
    public RENTER_MENU_OPTION getSelectedMenuItem() {
        return selectedMenuItem.get();
    }
    public void setSelectedMenuItem(RENTER_MENU_OPTION selectedMenuItem) {
        this.selectedMenuItem.set(selectedMenuItem);
    }
    public ObjectProperty<RENTER_MENU_OPTION> selectedMenuItemProperty() {
        return selectedMenuItem;
    }

}
