package org.rmit;

import javafx.application.Application;
import javafx.stage.Stage;
import org.rmit.database.DatabaseUtil;
import org.rmit.model.*;
import org.rmit.model.Persons.Owner;
import org.rmit.model.Persons.Renter;

public class RentalAgreementApplication extends Application {

    @Override
    public void init() throws Exception {
        if(DatabaseUtil.getSession() == null) System.out.println("Database not connected");
        else System.out.println("Database  connected");
    }

    @Override
    public void start(Stage stage) throws Exception {
        ModelCentral.getInstance().getViewFactory().startApplication();
    }


}