package org.example.groupproject_4knights;

import database.HibernateUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        stage.setTitle("Hello!");
//        stage.setScene(scene);
//        stage.show();


        SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        if(sessionFactory == null) System.out.println("Session Factory creation failed");
        else{
            Session session = sessionFactory.openSession();
            System.out.println("Session created");
            HibernateUtil.shutdown(session);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}