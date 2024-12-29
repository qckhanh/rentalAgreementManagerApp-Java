package org.rmit.view.Guest;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import jdk.jfr.FlightRecorder;
import org.rmit.controller.Guest.GuestController;

public class GuestViewFactory {
    private String HOST_PATH = "/org/rmit/demo/FXMLs/Guest/";
    private ObjectProperty<GUEST_MENU_OPTION> selectedMenuItem;        // for tracking which menu option is selected

    private AnchorPane browsePropertyView;
    private AnchorPane dashboardView;
    private AnchorPane browseUserView;
//    private AnchorPane agreementManagerView;
//    private AnchorPane ownerManagerView;

    public GuestViewFactory() {
        System.out.println("Guest View Factory created");
        selectedMenuItem = new SimpleObjectProperty<>(GUEST_MENU_OPTION.DASHBOARD);      // default view
    }

    public void startGuestView(){
        FXMLLoader load = new FXMLLoader(getClass().getResource(HOST_PATH + "guest.fxml"));
        GuestController controller = new GuestController();
        load.setController(controller);
        createStage(load);
    }

    //generate renter view for each menu option
    public AnchorPane getGuest_BrowsePropertyView(){
        if (browsePropertyView == null){
            try {
                browsePropertyView = new FXMLLoader(getClass().getResource(HOST_PATH + "browseProperty.fxml")).load();
            } catch (Exception e){
                System.out.println("Error loading browse property.fxml");
            }
        }
        return browsePropertyView;
    }

    public AnchorPane getGuest_dashboardView(){
        if (dashboardView == null){
            try {
                dashboardView = new FXMLLoader(getClass().getResource(HOST_PATH + "dashboardGuest.fxml")).load();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return dashboardView;
    }

    public AnchorPane getGuest_BrowsePersonView() {
        if(browseUserView == null){
            try{
                browseUserView = new FXMLLoader(getClass().getResource(HOST_PATH + "browseUser.fxml")).load();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return browseUserView;
    }

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
        browsePropertyView = null;
        browseUserView = null;
    }

    public ObjectProperty<GUEST_MENU_OPTION> getSelectedMenuItemProperty() {
        return selectedMenuItem;
    }

    public void setSelectedMenuItem(GUEST_MENU_OPTION selectedMenuItem) {
        this.selectedMenuItem.set(selectedMenuItem);
    }
}
