package org.rmit;
import javafx.application.Application;
import javafx.stage.Stage;
import org.rmit.Helper.DatabaseUtil;
import org.rmit.Helper.EntityGraphUtils;
import org.rmit.Helper.UIDecorator;
import org.rmit.database.CommercialPropertyDAO;
import org.rmit.database.ResidentialPropertyDAO;
import org.rmit.model.ModelCentral;
import org.rmit.model.Property.CommercialProperty;
import org.rmit.model.Property.ResidentialProperty;

import java.util.List;

public class RentalAgreementApplication extends Application {
    @Override
    public void init() throws Exception {
        UIDecorator.setApplicationTheme();
        if(DatabaseUtil.getSession() == null) System.out.println("Database not connected");
        else System.out.println("Database  connected");
    }

    public void start(Stage primaryStage) throws Exception {
        ModelCentral.getInstance().getStartViewFactory().startApplication();

//        ResidentialPropertyDAO dao = new ResidentialPropertyDAO();
//        List<ResidentialProperty> list = dao.search("a", EntityGraphUtils::SimpleResidentialProperty);
//        for(ResidentialProperty p : list) {
//            System.out.println(p);
//        }

//        CommercialPropertyDAO dao = new CommercialPropertyDAO();
//        List<CommercialProperty> list = dao.search("1", EntityGraphUtils::SimpleCommercialProperty);
//        System.out.println(list);
    }
}