package org.rmit;
import javafx.application.Application;
import javafx.stage.Stage;
import org.rmit.Helper.DateUtils;
import org.rmit.Helper.UIDecorator;
import org.rmit.database.*;
import org.rmit.model.Agreement.RentalAgreement;
import org.rmit.model.ModelCentral;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Renter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RentalAgreementApplication extends Application {

    @Override
    public void init() throws Exception {
        UIDecorator.setApplicationTheme();
        if(DatabaseUtil.getSession() == null) System.out.println("Database not connected");
        else System.out.println("Database  connected");
    }

    @Override
    public void start(Stage stage) throws Exception {
        ModelCentral.getInstance().getStartViewFactory().startApplication();

//        HostDAO hostDAO = new HostDAO();
//        Host host = hostDAO.get(60);

//        RentalAgreementDAO raDAO = new RentalAgreementDAO();
//        RentalAgreement ra = raDAO.get(52);
//        ra.setHost(host);
//        host.addAgreement(ra);
//
//        hostDAO.update(host);
//
//        System.out.println("Hello");

//        System.out.println(host.getRentalAgreements().size());




//        RenterDAO renterDAO = new RenterDAO();
//        Renter renter = renterDAO.validateLogin("renter", "renter");
//        System.out.println(renter);

//        System.out.println(DateUtils.currentTimestamp());
















    }
}