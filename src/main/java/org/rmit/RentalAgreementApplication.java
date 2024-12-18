package org.rmit;

import javafx.application.Application;
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
        if(DatabaseUtil.getSession() == null) System.out.println("Database not connected");
        else System.out.println("Database  connected");
    }

    @Override
    public void start(Stage stage) throws Exception {
//        RenterDAO renterDAO = new RenterDAO();
//        Renter tmp = renterDAO.get(2);
//        Session.getInstance().setCurrentUser(tmp);

        ModelCentral.getInstance().getViewFactory().startInit();
    }
}