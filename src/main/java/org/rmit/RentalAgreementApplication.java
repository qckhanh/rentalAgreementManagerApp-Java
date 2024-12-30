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
import org.rmit.Notification.NormalNotification;
import org.rmit.Notification.Notification;
import org.rmit.Notification.Request;
import org.rmit.database.*;
import org.rmit.model.ModelCentral;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Owner;
import org.rmit.model.Persons.Person;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Property.ResidentialProperty;
import org.rmit.model.Session;

import java.util.Map;

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
//        ModelCentral.getInstance().getStartViewFactory().startApplication();

//        RenterDAO renterDAO = new RenterDAO();
//        Renter renter = (Renter)renterDAO.get(1);
//        System.out.println("Renter name: " + renter.getName());

        HostDAO hostDAO = new HostDAO();
        Host host1 = (Host)hostDAO.get(42);
//        System.out.println("Host name: " + host1.getName());
//
//        OwnerDAO ownerDAO = new OwnerDAO();
//        Owner owner = (Owner) ownerDAO.get(22);
//        System.out.println("Owner name: " + owner.getName());
//
//
//        NormalNotification normalNotification = new NormalNotification(host1);
//        normalNotification.setMessage("Your property has been rented");
//        normalNotification.addReceiver(owner);
//        host1.sentNotification(normalNotification);


//        renterDAO.update(renter);
//        hostDAO.update(host1);
//        ownerDAO.update(owner);
//        System.out.println("Saved to database");

        Request notification = (Request) DAOInterface.getNotification(1);
        System.out.println(notification.getMessage());
        Notification notification2 = DAOInterface.getNotification(2);
        System.out.println(notification2.getMessage());
        Notification notification3 = DAOInterface.getNotification(3);
        System.out.println(notification3.getMessage());









    }
}