package org.rmit;
import atlantafx.base.controls.Card;
import atlantafx.base.controls.PasswordTextField;
import atlantafx.base.controls.Tile;
import atlantafx.base.layout.DeckPane;
import atlantafx.base.theme.Styles;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.rmit.Helper.DatabaseUtil;
import org.rmit.Helper.ImageUtils;
import org.rmit.Helper.UIDecorator;
import org.rmit.model.ModelCentral;
import org.rmit.model.Session;

public class RentalAgreementApplication extends Application {
    @Override
    public void init() throws Exception {
        UIDecorator.setApplicationTheme();
//        if(DatabaseUtil.getSession() == null) System.out.println("Database not connected");
//        else System.out.println("Database  connected");
    }

    public void start(Stage primaryStage) throws Exception {
//        ModelCentral.getInstance().getStartViewFactory().startApplication();

        DeckPane deckPane = new DeckPane();

        // Create views (cards)
        StackPane view1 = new StackPane(new Button("View 1"));
        StackPane view2 = new StackPane(new Button("View 2"));
        StackPane view3 = new StackPane(new Button("View 3"));

        // Add views to DeckPane
        deckPane.getChildren().addAll(view1, view2, view3);

        // Switch to view2 programmatically
        Button switchButton = new Button("Switch to View 2");
        switchButton.setOnAction(e -> deckPane.swipeRight(view2));

        // Layout with control buttons
        StackPane root = new StackPane(deckPane, switchButton);
        Scene scene = new Scene(root, 400, 300);

        // Apply PrimerLight theme (if you use PrimerLight)

        primaryStage.setTitle("AtlantaFx DeckPane Example");
        primaryStage.setScene(scene);
        primaryStage.show();
    }



}
