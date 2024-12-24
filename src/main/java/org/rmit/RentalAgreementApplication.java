package org.rmit;

import javafx.application.Application;
import javafx.stage.Stage;
import org.rmit.model.ModelCentral;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Session;

public class RentalAgreementApplication extends Application {

    @Override
    public void init() throws Exception {
//        if(DatabaseUtil.getSession() == null) System.out.println("Database not connected");
//        else System.out.println("Database  connected");

    }

    @Override
    public void start(Stage stage) throws Exception {
        Session.getInstance().setCurrentUser(new Renter());
        ModelCentral.getInstance().getRenterViewFactory().startRenterView();
//        ModelCentral.getInstance().getStartViewFactory().startApplication();


    }
}