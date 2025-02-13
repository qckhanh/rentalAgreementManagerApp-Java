package org.rmit.view.Owner;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.rmit.controller.Owner.OwnerController;


public class OwnerViewFactory {
    String OWNER_PATH = "/org/rmit/demo/FXMLs/Owner/";
    private ObjectProperty<OWNER_MENU_OPTION> ownerSelectedMenuItem;
    private ObjectProperty<PROPERTY_FILTER> propertyFilter;

    private AnchorPane owner_editProfileView;
    private AnchorPane owner_dashboardView;
    private AnchorPane owner_propertyManagerView;
    private AnchorPane owner_addPropertyView;
    private AnchorPane owner_updatePropertyView;
    private AnchorPane owner_hostManagerView;
    private AnchorPane notificationView;

    public OwnerViewFactory() {
        ownerSelectedMenuItem = new SimpleObjectProperty<>(OWNER_MENU_OPTION.DASHBOARD);
    }

    //start renter view when user login as renter
    public void startOwnerView(){
        FXMLLoader load = new FXMLLoader(getClass().getResource(OWNER_PATH + "owner.fxml"));
        OwnerController controller = new OwnerController();
        load.setController(controller);
        createStage(load);
    }

    //generate owner view for each menu option
    public AnchorPane getOwner_editProfileView() {
        if (owner_editProfileView == null) {
            try {
                owner_editProfileView = new FXMLLoader(getClass().getResource(OWNER_PATH + "editProfileOwner.fxml")).load();
            } catch (Exception e) {
                System.out.println("Error loading edit profile.fxml");
            }
        }
        return owner_editProfileView;
    }

    public AnchorPane getOwner_dashboardView() {
        if (owner_dashboardView == null) {
            try {
                owner_dashboardView = new FXMLLoader(getClass().getResource(OWNER_PATH + "dashboardOwner.fxml")).load();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return owner_dashboardView;
    }

    public AnchorPane getOwner_propertyManagerView() {
        if (owner_propertyManagerView == null) {
            try {
                owner_propertyManagerView = new FXMLLoader(getClass().getResource(OWNER_PATH + "propertiesManager.fxml")).load();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return  owner_propertyManagerView;
    }

    public AnchorPane getOwner_hostManagerView() {
        if (owner_hostManagerView == null) {
            try {
                owner_hostManagerView = new FXMLLoader(getClass().getResource(OWNER_PATH + "hostManager.fxml")).load();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return owner_hostManagerView;
    }

    public AnchorPane getOwner_NotificationView(){
        if(1 == 1){
            try{
                notificationView = new FXMLLoader(getClass().getResource(OWNER_PATH + "notificationOwner.fxml")).load();
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
        //set all view to null
        owner_editProfileView = null;
        owner_dashboardView = null;
        owner_propertyManagerView = null;
        owner_hostManagerView = null;
    }

    public OWNER_MENU_OPTION getOwnerSelectedMenuItem() {
        return ownerSelectedMenuItem.get();
    }

    public ObjectProperty<OWNER_MENU_OPTION> ownerSelectedMenuItemProperty() {
        return ownerSelectedMenuItem;
    }

    public void setOwnerSelectedMenuItem(OWNER_MENU_OPTION ownerSelectedMenuItem) {
        this.ownerSelectedMenuItem.set(ownerSelectedMenuItem);
    }

    public AnchorPane getOwner_addPropertyView() {
        if (owner_addPropertyView == null) {
            try {
                owner_addPropertyView = new FXMLLoader(getClass().getResource(OWNER_PATH + "addPropertiesForm.fxml")).load();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return owner_addPropertyView;
    }

    public AnchorPane getOwner_updatePropertyView() {
        if (owner_updatePropertyView == null) {
            try {
                owner_updatePropertyView = new FXMLLoader(getClass().getResource(OWNER_PATH + "updatePropertiesForm.fxml")).load();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return owner_updatePropertyView;
    }

    //getter and setter


}
