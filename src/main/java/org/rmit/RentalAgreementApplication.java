package org.rmit;
import atlantafx.base.controls.ModalPane;
import atlantafx.base.layout.DeckPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.rmit.Helper.DatabaseUtil;
import org.rmit.Helper.EntityGraphUtils;
import org.rmit.Helper.ImageUtils;
import org.rmit.Helper.UIDecorator;
import org.rmit.database.CommercialPropertyDAO;
import org.rmit.model.ModelCentral;
import org.rmit.model.Property.CommercialProperty;

import java.util.concurrent.atomic.AtomicInteger;


public class RentalAgreementApplication extends Application {
    @Override
    public void init() throws Exception {
        UIDecorator.setApplicationTheme();
        if(DatabaseUtil.getSession() == null) System.out.println("Database not connected");
        else System.out.println("Database  connected");
    }

    public void start(Stage primaryStage) throws Exception {
        ModelCentral.getInstance().getStartViewFactory().startApplication();

//        CommercialPropertyDAO commercialPropertyDAO = new CommercialPropertyDAO();
//        CommercialProperty commercialProperty = commercialPropertyDAO.get(5, EntityGraphUtils::SimpleCommercialPropertyFull);
//
////        while(true){
////            String path = ImageUtils.openFileChooseDialog();
////            if(!commercialProperty.addImages(ImageUtils.getByte(path))) break;
////        }
////
////        boolean isUpdated = commercialPropertyDAO.update(commercialProperty);
////        if(isUpdated) System.out.println("Updated");
////        else System.out.println("Not Updated");
//
//        ModalPane bigImage = new ModalPane();
//        DeckPane deckPane = new DeckPane();
//        HBox hBox = new HBox();
//        Button prev = new Button("Previous");
//        Button next = new Button("Next");
//
//        hBox.getChildren().addAll(prev, deckPane, next);
//
//        AtomicInteger index = new AtomicInteger();
//        ImageView currentImage = new ImageView(ImageUtils.byteToImage(commercialProperty.getImages().get(index.get())));
//
//        currentImage.setOnMouseClicked(e -> showFull(currentImage));
//
//        deckPane.getChildren().add(currentImage);
//        bigImage.show(currentImage);
//
//        prev.setOnAction(e -> {
//            currentImage.setFitWidth(200); // Desired width
//            currentImage.setFitHeight(150); // Desired height
//            currentImage.setPreserveRatio(true);
//            currentImage.setSmooth(true); // Optional for better rendering quality
//            currentImage.setCache(true); // Optional for performance
//            index.set(prevIndex(index.get(), commercialProperty.getImages().size()));
//            currentImage.setImage(ImageUtils.byteToImage(commercialProperty.getImages().get(index.get())));
//
//        });
//
//        next.setOnAction(e -> {
//            currentImage.setFitWidth(200); // Desired width
//            currentImage.setFitHeight(150); // Desired height
//            currentImage.setPreserveRatio(true);
//            currentImage.setSmooth(true); // Optional for better rendering quality
//            currentImage.setCache(true); // Optional for performance
//
//            index.set(nextIndex(index.get(), commercialProperty.getImages().size()));
//            currentImage.setImage(ImageUtils.byteToImage(commercialProperty.getImages().get(index.get())));
//        });
//
//        Scene scene = new Scene(hBox, 800, 600);
//        primaryStage.setScene(scene);
//        primaryStage.show();



    }

    private void showFull(ImageView image){
        System.out.println("Clicked");
        ModalPane bigImage = new ModalPane();
        ImageView bigImageView = new ImageView(image.getImage());
        bigImage.show(bigImageView);
        bigImage.requestFocus();
    }

    public int nextIndex(int index, int maxSize){
        return (index + 1) % maxSize;
    }

    public int prevIndex(int index, int maxSize){
        return (index - 1 + maxSize) % maxSize;
    }
}