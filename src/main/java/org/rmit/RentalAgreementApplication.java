package org.rmit;
import javafx.application.Application;
import javafx.stage.Stage;
import org.rmit.Helper.DatabaseUtil;
import org.rmit.Helper.UIDecorator;
import org.rmit.model.ModelCentral;

public class RentalAgreementApplication extends Application {
    @Override
    public void init() throws Exception {
        UIDecorator.setApplicationTheme();
        if(DatabaseUtil.getSession() == null) System.out.println("Database not connected");
        else System.out.println("Database  connected");
    }

    public void start(Stage primaryStage) throws Exception {
        ModelCentral.getInstance().getStartViewFactory().startApplication();
    }
}
