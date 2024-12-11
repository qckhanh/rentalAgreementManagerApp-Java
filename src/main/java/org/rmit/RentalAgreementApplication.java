package org.rmit;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.SessionFactory;
import org.rmit.database.HibernateUtil;

public class RentalAgreementApplication extends Application {


    @Override
    public void start(Stage stage) throws Exception {
    }

    @Override
    public void init() throws Exception {
        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        if(sessionFactory == null) {
            System.out.println("Session factory is null");
        }
        else{
            System.out.println("Database connected");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}