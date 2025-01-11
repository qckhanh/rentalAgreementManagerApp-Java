package org.rmit.controller.Renter;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.rmit.Helper.EntityGraphUtils;
import org.rmit.Helper.TaskUtils;
import org.rmit.Helper.UIDecorator;
import org.rmit.Notification.Notification;
import org.rmit.model.Agreement.Payment;
import org.rmit.view.ViewCentral;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Session;
import org.rmit.database.RenterDAO;
import org.rmit.view.Start.NOTIFICATION_TYPE;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.Set;

public class Renter_DashboardController implements Initializable {

    public Label welcomeLabel;
    public TableView<Notification> recentNotiTableView;
    public Button refreshButton;
    public Label dateShow;
    public Label agreementsNumber;
    public Label paymentsNumber;
    public AnchorPane anchorPane;

    private ObservableList<Payment> paymentObservableList = FXCollections.observableArrayList();
    private ObservableList<Notification> notificationObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        welcomeLabel.setText("Welcome " + Session.getInstance().getCurrentUser().getName());
        Session.getInstance().getCurrentUser().namePropertyProperty().addListener((observable, oldValue, newValue) ->
                welcomeLabel.setText("Welcome " + newValue)
        );

        loadRecentNotification((Session.getInstance().getCurrentUser()).getReceivedNotifications());
        recentNotiTableView.getColumns().addAll(
                createColumn("Header", "header"),
                createColumn("Time", "timestamp")
        );
        recentNotiTableView.setItems(notificationObservableList);

        String text = LocalDate.now().getDayOfWeek().toString() + ", " +
                LocalDate.now().getDayOfMonth() + " " +
                LocalDate.now().getMonth().toString() + " " +
                LocalDate.now().getYear();
        dateShow.setText(text);
        agreementsNumber.setText(String.valueOf(((Renter) Session.getInstance().getCurrentUser()).getAgreementList().size()));
        paymentsNumber.setText(String.valueOf(((Renter) Session.getInstance().getCurrentUser()).getPayments().size()));
        refreshButton.setOnAction(e -> refreshData());
        decor();
    }

    private void decor(){
       UIDecorator.setNormalButton(refreshButton, UIDecorator.REFRESH(), null);
    }

    private void refreshData() {
        RenterDAO renterDAO = new RenterDAO();
        int id = Integer.parseInt(Session.getInstance().getCurrentUser().getId()+"");
        refreshButton.setDisable(true);
        Task<Renter> renterTask = TaskUtils.createTask(() -> renterDAO.get(id, EntityGraphUtils::SimpleRenterFull));
        renterTask.setOnSucceeded(e -> Platform.runLater(() -> {
            agreementsNumber.setText(String.valueOf(renterTask.getValue().getAgreementList().size()));
            paymentsNumber.setText(String.valueOf(renterTask.getValue().getPayments().size()));
            ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.SUCCESS, anchorPane, "Date now up to date");
            refreshButton.setDisable(false);
        }));
    }

    private TableColumn<Notification, ?> createColumn(String columnName, String propertyName) {
        TableColumn<Notification, ?> column = new TableColumn<>(columnName);
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        return column;
    }



    private void loadRecentPayment(Set<Payment> p) {
        paymentObservableList.addAll(p);
    }

    private void loadRecentNotification(Set<Notification> s) {
        notificationObservableList.addAll(s);
    }
}
