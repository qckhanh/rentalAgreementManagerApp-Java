package org.rmit;

import javafx.application.Application;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;
import org.rmit.database.*;
import org.rmit.model.*;

import java.time.LocalDate;
import java.util.Set;

public class RentalAgreementApplication extends Application {


    @Override
    public void start(Stage stage) throws Exception {
    }

    @Override
    public void init() throws Exception {
        if(DatabaseUtil.getSession() == null) System.out.println("Database not connected");
        else System.out.println("Database  connected");
    }

    public static void main(String[] args) {
//        launch(args);












    }
}