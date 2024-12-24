package org.rmit.view.Host;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.rmit.controller.Host.HostController;
import org.rmit.view.Renter.RENTER_MENU_OPTION;


public class HostViewFactory {
    String HOST_PATH = "/org/rmit/demo/FXMLs/Host/";
    private ObjectProperty<HOST_MENU_OPTION> selectedMenuItem;        // for tracking which menu option is selected

    private AnchorPane editProfileView;
    private AnchorPane dashboardView;

    public HostViewFactory() {
        selectedMenuItem = new SimpleObjectProperty<>(HOST_MENU_OPTION.DASHBOARD);      // default view
    }

    public void startHostView(){
        FXMLLoader load = new FXMLLoader(getClass().getResource(HOST_PATH + "host.fxml"));
        HostController controller = new HostController();
        load.setController(controller);
        createStage(load);
    }

    //generate renter view for each menu option
    public AnchorPane getHost_EditProfileView(){
        if (editProfileView == null){
            try {
                editProfileView = new FXMLLoader(getClass().getResource(HOST_PATH + "editProfileHost.fxml")).load();
            } catch (Exception e){
                System.out.println("Error loading edit profile.fxml");
            }
        }
        return editProfileView;
    }
    public AnchorPane getHost_dashboardView(){
        if (dashboardView == null){
            try {
                dashboardView = new FXMLLoader(getClass().getResource(HOST_PATH + "dashboardHost.fxml")).load();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return dashboardView;
    }
    public AnchorPane getRenter_paymentManagerView(){
//        if (1 == 1){
//            try {
//                renter_paymentManagerView = new FXMLLoader(getClass().getResource(HOST_PATH + "paymentManagerRenter.fxml")).load();
//            } catch (Exception e){
//                System.out.println("Error loading paymentManagerRenter.fxml");
//            }
//        }
//        return renter_paymentManagerView;
        return new AnchorPane();

    }
    public AnchorPane getRenter_agreementManagerView(){
//        if (renter_agreementManagerView == null){
//            try {
//                renter_agreementManagerView = new FXMLLoader(getClass().getResource(HOST_PATH + "agreementManagerRenter.fxml")).load();
//            } catch (Exception e){
//                System.out.println("Error loading paymentManagerRenter.fxml");
//            }
//        }
//        return renter_agreementManagerView;
        return new AnchorPane();
    }
    public AnchorPane getRenter_makePaymentView(){
//        if(renter_makePaymentView == null){
//            try {
//                renter_makePaymentView = new FXMLLoader(getClass().getResource(HOST_PATH + "makePaymentRenter.fxml")).load();
//            } catch (Exception e){
//                System.out.println("Error loading makePaymentRenter.fxml");
//            }
//        }
//        return renter_makePaymentView;
        return new AnchorPane();

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
        stage.show();
    }

    public void resetView(){
        dashboardView = null;
        editProfileView = null;
    }
    //getter and setter

    public HOST_MENU_OPTION getSelectedMenuItem() {
        return selectedMenuItem.get();
    }

    public ObjectProperty<HOST_MENU_OPTION> selectedMenuItemProperty() {
        return selectedMenuItem;
    }

    public void setSelectedMenuItem(HOST_MENU_OPTION selectedMenuItem) {
        this.selectedMenuItem.set(selectedMenuItem);
    }
}
