package org.rmit;
import atlantafx.base.theme.Styles;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.rmit.Helper.ImageUtils;
import org.rmit.Helper.UIDecorator;
import org.rmit.database.DatabaseUtil;
import org.rmit.model.ModelCentral;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Session;

import javax.management.Notification;

import static org.rmit.Helper.ImageUtils.DEFAULT_IMAGE;

public class RentalAgreementApplication extends Application {

    @Override
    public void init() throws Exception {
        UIDecorator.setApplicationTheme();
        if(DatabaseUtil.getSession() == null) System.out.println("Database not connected");
        else System.out.println("Database  connected");
    }

    @Override
    public void start(Stage stage) throws Exception {
//        Session.getInstance().setCurrentUser(new Renter());
//        ModelCentral.getInstance().getRenterViewFactory().startRenterView();
        ModelCentral.getInstance().getStartViewFactory().startApplication();

//        ModelCentral.getInstance().getStartViewFactory().showNotification("ABC");




    }
}