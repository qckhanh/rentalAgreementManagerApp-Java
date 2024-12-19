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
//    private ObjectProperty<RENTER_MENU_OPTION> renterSelectedMenuItem;        // for tracking which menu option is selected
//    private ObjectProperty<PAYMENT_FILTER> paymentFilter;
//
//    private AnchorPane renter_editProfileView;
//    private AnchorPane renter_dashboardView;
//    private AnchorPane renter_paymentManagerView;
//    private AnchorPane renter_agreementManagerView;

    public OwnerViewFactory() {
//        renterSelectedMenuItem = new SimpleObjectProperty<>(RENTER_MENU_OPTION.DASHBOARD);      // default view
    }

    //start renter view when user login as renter
    public void startOwnerView(){
        FXMLLoader load = new FXMLLoader(getClass().getResource(OWNER_PATH + "owner.fxml"));
        OwnerController controller = new OwnerController();
        load.setController(controller);
        createStage(load);
    }

    //generate renter view for each menu option

//    public AnchorPane getRenter_EditProfileView(){
//        if (renter_editProfileView == null){
//            try {
//                renter_editProfileView = new FXMLLoader(getClass().getResource(RENTER_PATH + "editProfileRenter.fxml")).load();
//            } catch (Exception e){
//                System.out.println("Error loading edit profile.fxml");
//            }
//        }
//        return renter_editProfileView;

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
//        renter_editProfileView = null;
//        renter_dashboardView = null;
//        renter_paymentManagerView = null;
//        renter_agreementManagerView = null;
    }

    //getter and setter

}
