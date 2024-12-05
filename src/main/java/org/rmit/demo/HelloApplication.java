package org.rmit.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.rmit.database.HibernateUtil;
import org.rmit.entity.Renter;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        if(sessionFactory == null){
            System.out.println("Session factory is null");
            return;
        }
        else{
            /// do sth here
        }




        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();


//        HibernateUtil.shutdown(session);
    }

    public static void main(String[] args) {
        launch();
    }
}