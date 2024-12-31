package org.rmit.view.Admin;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.rmit.controller.Admin.AdminController;
import org.rmit.view.Host.HOST_MENU_OPTION;

public class AdminViewFactory {
    String path = "/org/rmit/demo/FXMLs/Admin/";
    private ObjectProperty<ADMIN_MENU_OPTION> selectedMenuItem;
    private AnchorPane admin_renterManagerView;
    private AnchorPane admin_dashboardView;

    public AdminViewFactory() {
        selectedMenuItem = new SimpleObjectProperty<>(ADMIN_MENU_OPTION.DASHBOARD);      // default view
    }

    // start admin view when user login as admin
    public void startAdminView(){
        FXMLLoader load = new FXMLLoader(getClass().getResource(path + "admin.fxml"));
        AdminController controller = new AdminController();
        load.setController(controller);
        createStage(load);
    }

    // generate admin view for each menu option
    public AnchorPane getAdmin_editProfileView(){
        if (admin_renterManagerView == null){
            try {
                admin_renterManagerView = new FXMLLoader(getClass().getResource(path + "editProfileAdmin.fxml")).load();
            } catch (Exception e){
                System.out.println("Error loading renter manager.fxml");
            }
        }
        return admin_renterManagerView;
    }

    public AnchorPane getAdmin_dashboardView() {
        if (admin_dashboardView == null){
            try {
                admin_dashboardView = new FXMLLoader(getClass().getResource(path + "dashboardHost.fxml")).load();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return admin_dashboardView;
    }

    public AnchorPane getRenterManagerView() {
        if (admin_renterManagerView == null){
            try {
                admin_renterManagerView = new FXMLLoader(getClass().getResource(path + "renterManager.fxml")).load();
            } catch (Exception e){
                System.out.println("Error loading renter manager.fxml");
            }
        }
        return admin_renterManagerView;
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
}
