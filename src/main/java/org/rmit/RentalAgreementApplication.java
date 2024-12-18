package org.rmit;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;
import org.rmit.database.*;
import org.rmit.model.*;
import org.rmit.model.Persons.Renter;

import java.time.LocalDate;
import java.util.Set;

public class RentalAgreementApplication extends Application {

    @Override
    public void init() throws Exception {
//        if(DatabaseUtil.getSession() == null) System.out.println("Database not connected");
//        else System.out.println("Database  connected");

    }

    @Override
    public void start(Stage stage) throws Exception {
//        RenterDAO renterDAO = new RenterDAO();
//        Renter tmp = renterDAO.get(2);
//        Session.getInstance().setCurrentUser(tmp);

        ModelCentral.getInstance().getViewFactory().startInit();


//        Slider lowSlider = new Slider(0, 100, 20); // Min = 0, Max = 100, Default = 20
//        Slider highSlider = new Slider(0, 100, 80); // Min = 0, Max = 100, Default = 80
//
//        lowSlider.setShowTickMarks(true);
//        lowSlider.setShowTickLabels(true);
//
//        highSlider.setShowTickMarks(true);
//        highSlider.setShowTickLabels(true);
//
//        Label rangeLabel = new Label("Range: 20 - 80");
//
//        // Synchronize sliders
//        lowSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue.doubleValue() > highSlider.getValue()) {
//                lowSlider.setValue(highSlider.getValue()); // Prevent overlap
//            }
//            rangeLabel.setText("Range: " + (int) lowSlider.getValue() + " - " + (int) highSlider.getValue());
//        });
//
//        highSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
//            if (newValue.doubleValue() < lowSlider.getValue()) {
//                highSlider.setValue(lowSlider.getValue()); // Prevent overlap
//            }
//            rangeLabel.setText("Range: " + (int) lowSlider.getValue() + " - " + (int) highSlider.getValue());
//        });
//
//        VBox root = new VBox(10, lowSlider, highSlider, rangeLabel);
//        Scene scene = new Scene(root, 400, 200);
//        stage.setScene(scene);
//        stage.setTitle("Custom Range Slider");
//        stage.show();
    }
}