package org.rmit.view.Owner;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.rmit.controller.Owner.OwnerController;
import org.rmit.controller.Renter.RenterController;
import org.rmit.view.Renter.PAYMENT_FILTER;
import org.rmit.view.Renter.RENTER_MENU_OPTION;

public class OwnerViewFactory {
    String OWNER_PATH = "/org/rmit/demo/FXMLs/Owner/";
    private ObjectProperty<OWNER_MENU_OPTION> ownerSelectedMenuItem;
    private ObjectProperty<PROPERTY_FILTER> propertyFilter;

    private AnchorPane owner_editProfileView;
    private AnchorPane owner_dashboardView;
    private AnchorPane owner_propertyManagerView;

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

    //getter and setter


}
