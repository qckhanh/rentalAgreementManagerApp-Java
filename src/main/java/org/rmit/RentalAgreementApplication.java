package org.rmit;
import javafx.application.Application;
import javafx.stage.Stage;
import org.rmit.Helper.DatabaseUtil;
import org.rmit.Helper.UIDecorator;
import org.rmit.view.ViewCentral;
import java.net.InetAddress;

public class RentalAgreementApplication extends Application {
    @Override
    public void init() throws Exception {
        UIDecorator.setApplicationTheme();
    }

    public void start(Stage primaryStage) throws Exception {
        try{
            InetAddress address = InetAddress.getByName("www.google.com"); // check internet connection
            if (!address.isReachable(5000)){
                ViewCentral.getInstance().getStartViewFactory().startNoInternetConnection();
                return;
            }
            DatabaseUtil.getSession();
            DatabaseUtil.warmUp();
            ViewCentral.getInstance().getStartViewFactory().startApplication();
        }
        catch (Exception e){
            ViewCentral.getInstance().getStartViewFactory().startNoInternetConnection();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}