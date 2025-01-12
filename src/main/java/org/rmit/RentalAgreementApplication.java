package org.rmit;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.stage.Stage;
import org.rmit.Helper.DatabaseUtil;
import org.rmit.Helper.TaskUtils;
import org.rmit.Helper.UIDecorator;
import org.rmit.view.ViewCentral;
import java.net.InetAddress;

public class RentalAgreementApplication extends Application {
    @Override
    public void init() throws Exception {
        UIDecorator.setApplicationTheme();
    }

    public void start(Stage primaryStage) throws Exception {
        ViewCentral.getInstance().getStartViewFactory().loadingView();
        Task<Boolean> setUpTask = TaskUtils.createTask(() -> {
            try{
                InetAddress address = InetAddress.getByName("www.google.com"); // check internet connection
                int attempt = 0;
                while(attempt < 5){
                    if(!address.isReachable(5000)){
                        System.out.println("No internet connection. Attempting to reconnect...");
                        attempt++;
                        Thread.sleep(1000);
                    }
                    else{
                        break;
                    }

                }
                DatabaseUtil.getSession();
                DatabaseUtil.warmUp();
                return true;
            }
            catch (Exception e){
//                ViewCentral.getInstance().getStartViewFactory().startNoInternetConnection();
                return false;
            }
        });

        TaskUtils.run(setUpTask);
        setUpTask.setOnSucceeded(e -> Platform.runLater(() -> {
            boolean isConnected = setUpTask.getValue();
            if(isConnected){
                ViewCentral.getInstance().getStartViewFactory().closeConnecting();
                ViewCentral.getInstance().getStartViewFactory().startApplication();
            }
            else{
                ViewCentral.getInstance().getStartViewFactory().closeConnecting();
                ViewCentral.getInstance().getStartViewFactory().startNoInternetConnection();
            }
        }));
    }
    public static void main(String[] args) {
        launch(args);
    }
}