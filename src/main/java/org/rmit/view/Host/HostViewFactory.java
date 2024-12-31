package org.rmit.view.Host;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.rmit.controller.Host.HostController;
import org.rmit.view.Renter.RENTER_MENU_OPTION;


public class HostViewFactory {
    private String HOST_PATH = "/org/rmit/demo/FXMLs/Host/";
    private ObjectProperty<HOST_MENU_OPTION> selectedMenuItem;        // for tracking which menu option is selected

    private AnchorPane editProfileView;
    private AnchorPane dashboardView;
    private AnchorPane managePropertyView;
    private AnchorPane agreementManagerView;
    private AnchorPane ownerManagerView;
    private AnchorPane notificationView;

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

    public AnchorPane getHost_ManagePropertyView() {
        if(managePropertyView == null){
            try{
                managePropertyView = new FXMLLoader(getClass().getResource(HOST_PATH + "manageProperty.fxml")).load();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return managePropertyView;
    }

    public AnchorPane getHost_agreementManagerView(){
        if (agreementManagerView == null){
            try {
                agreementManagerView = new FXMLLoader(getClass().getResource(HOST_PATH + "agreementManagerHost.fxml")).load();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return agreementManagerView;
    }

    public AnchorPane getHost_OwnerManagerView(){
        if (1 == 1){
            try {
                ownerManagerView = new FXMLLoader(getClass().getResource(HOST_PATH + "manageOwner.fxml")).load();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return ownerManagerView;
    }

    public AnchorPane getHost_NotificationView(){
        if(1 == 1){
            try{
                notificationView = new FXMLLoader(getClass().getResource(HOST_PATH + "notificationHost.fxml")).load();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        return notificationView;
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
        managePropertyView = null;
        agreementManagerView = null;
        ownerManagerView = null;
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
