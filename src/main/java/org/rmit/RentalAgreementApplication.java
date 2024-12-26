package org.rmit;

import javafx.application.Application;
import javafx.stage.Stage;
import org.rmit.database.*;
import org.rmit.model.Agreement.Payment;
import org.rmit.model.ModelCentral;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Property.CommercialProperty;
import org.rmit.model.Session;

import java.lang.module.Configuration;
import java.util.List;

public class RentalAgreementApplication extends Application {

    @Override
    public void init() throws Exception {
        if(DatabaseUtil.getSession() == null) System.out.println("Database not connected");
        else System.out.println("Database  connected");
    }

    @Override
    public void start(Stage stage) throws Exception {
////        Session.getInstance().setCurrentUser(new Host());
////        ModelCentral.getInstance().getHostViewFactory().startHostView();
        ModelCentral.getInstance().getStartViewFactory().startApplication();
    }
}