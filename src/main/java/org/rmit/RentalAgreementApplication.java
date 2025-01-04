package org.rmit;
import javafx.application.Application;
import javafx.stage.Stage;
import org.rmit.Helper.DatabaseUtil;
import org.rmit.Helper.EntityGraphUtils;
import org.rmit.Helper.UIDecorator;
import org.rmit.database.CommercialPropertyDAO;
import org.rmit.database.HostDAO;
import org.rmit.database.RentalAgreementDAO;
import org.rmit.database.RenterDAO;
import org.rmit.model.Agreement.RentalAgreement;
import org.rmit.model.ModelCentral;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Property.CommercialProperty;
import org.rmit.model.Property.Property;

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

        RenterDAO renterDAO = new RenterDAO();
        System.out.println(renterDAO.get(1, EntityGraphUtils::SimpleRenter));

//        RentalAgreementDAO rentalAgreementDAO = new RentalAgreementDAO();
//        RentalAgreement rentalAgreement = new RentalAgreement();
//
//        RenterDAO renterDAO = new RenterDAO();
//        HostDAO hostDAO = new HostDAO();
//        CommercialPropertyDAO property = new CommercialPropertyDAO();
//
////        Renter mainRenter = renterDAO.get(2, EntityGraphUtils::SimpleRenterFull);
////        rentalAgreement.setMainTenant(mainRenter);
////
////        CommercialProperty commercialProperty = property.get(2, EntityGraphUtils::SimpleCommercialProperty);
////
////        Host host = hostDAO.get(41, EntityGraphUtils::SimpleHostFull);
////
//        Renter subRenter = renterDAO.get(7552, EntityGraphUtils::SimpleRenterFull);
//        System.out.println(subRenter.getAgreementList().size());
////
////        rentalAgreement.setHost(host);
////        rentalAgreement.setProperty(commercialProperty);
////        rentalAgreement.addSubTenant(subRenter);
////
////        host.addAgreement(rentalAgreement);
////        mainRenter.addAgreement(rentalAgreement);
//        subRenter.addAgreement(rentalAgreement);
////
////        boolean isADDED = rentalAgreementDAO.add(rentalAgreement);
////        boolean isUpdateHost =  hostDAO.update(host);
////        boolean isUpdateMainRenter = renterDAO.update(mainRenter);
////        boolean isUpdateSubRenter = renterDAO.update(subRenter);
////
////        System.out.println("Host updated: " + isUpdateHost);
////        System.out.println("Main Renter updated: " + isUpdateMainRenter);
////        System.out.println("Sub Renter updated: " + isUpdateSubRenter);
////        System.out.println("Rental Agreement added: " + isADDED);
















    }
}