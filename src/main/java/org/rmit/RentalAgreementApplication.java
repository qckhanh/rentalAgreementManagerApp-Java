package org.rmit;
import javafx.application.Application;
import javafx.stage.Stage;
import org.rmit.database.*;
import org.rmit.model.ModelCentral;


public class RentalAgreementApplication extends Application {

    @Override
    public void init() throws Exception {
        if(DatabaseUtil.getSession() == null) System.out.println("Database not connected");
        else System.out.println("Database  connected");
    }

    @Override
    public void start(Stage stage) throws Exception {
        ModelCentral.getInstance().getStartViewFactory().startApplication();
    }
}