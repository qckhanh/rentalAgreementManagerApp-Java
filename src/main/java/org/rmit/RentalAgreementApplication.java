package org.rmit;
import atlantafx.base.theme.Styles;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.rmit.Helper.ImageUtils;
import org.rmit.Helper.UIDecorator;
import org.rmit.database.DatabaseUtil;
import org.rmit.database.ResidentialPropertyDAO;
import org.rmit.model.ModelCentral;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Owner;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Property.ResidentialProperty;
import org.rmit.model.Session;

import javax.management.Notification;

import static org.rmit.Helper.ImageUtils.DEFAULT_IMAGE;
import static org.rmit.database.DAOInterface.isValidUsername;

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





// Method to create cards for roles





    }
}