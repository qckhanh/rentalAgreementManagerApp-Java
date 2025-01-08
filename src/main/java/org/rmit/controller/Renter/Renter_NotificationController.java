package org.rmit.controller.Renter;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.rmit.Helper.NotificationUtils;
import org.rmit.Helper.UIDecorator;
import org.rmit.Notification.Notification;
import org.rmit.Notification.Request;
import org.rmit.database.*;
import org.rmit.model.Agreement.AgreementStatus;
import org.rmit.model.Agreement.RentalAgreement;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Owner;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Property.Property;
import org.rmit.model.Property.RentalPeriod;
import org.rmit.model.Session;
import org.rmit.view.Host.NOTI_TYPE_FILTER;
import org.rmit.view.Host.ROLE_FILTER;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;

import static org.rmit.view.Host.ROLE_FILTER.*;
import static org.rmit.view.Host.ROLE_FILTER.SENDER;

public class Renter_NotificationController implements Initializable {
    public Label type_label;
    public Label sender_label;
    public Label timestamp_label;
    public Label receiver_label;
    public TextArea mainContent_textArea;
    public Button approve_btn;
    public Button deny_btn;
    public ListView<Notification> noti_ListView;
    public ComboBox<ROLE_FILTER> roleFilter_comboBox;
    public ComboBox<NOTI_TYPE_FILTER> notiType_comboBox;

    public ObjectProperty<ROLE_FILTER> roleFilterProperty = new SimpleObjectProperty<>(null);
    public ObjectProperty<NOTI_TYPE_FILTER> notiTypeProperty = new SimpleObjectProperty<>(null);
    public ObjectProperty<Notification> selectedNotificationProperty = new SimpleObjectProperty<>(null);
    public ObjectProperty<Renter> currentUser = new SimpleObjectProperty<>((Renter) Session.getInstance().getCurrentUser());
    public Button deleteNoti_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        approve_btn.setOnAction(e -> approveRequest());
        deny_btn.setOnAction(e -> denyRequest());

        UIDecorator.setDangerButton(deleteNoti_btn, UIDecorator.DELETE(), null);
        deleteNoti_btn.setOnAction(e -> deleteNoti());
        roleFilter_comboBox.getItems().addAll(
                SENDER,
                RECEIVER,
                ROLE_FILTER.NONE
        );
        notiType_comboBox.getItems().addAll(
                NOTI_TYPE_FILTER.REQUEST,
                NOTI_TYPE_FILTER.NORMAL,
                NOTI_TYPE_FILTER.NONE
        );
        notiType_comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            notiTypeProperty.set(newValue);
        });
        roleFilter_comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            roleFilterProperty.set(newValue);
        });
        noti_ListView.setOnMouseClicked(e -> showDetail(noti_ListView.getSelectionModel().getSelectedItem()));
        noti_ListView.setCellFactory(p -> new ListCell<Notification>(){
            @Override
            protected void updateItem(Notification notification, boolean empty){
                super.updateItem(notification, empty);
                if(empty || notification == null){
                    setText(null);
                    setOnMouseClicked(null);
                }
                else{
                    if(notification.getSender().getId() == currentUser.get().getId()){
                        setButtonVisible(false);
                    }
                    else{
                        setButtonVisible(true);
                    }

                    selectedNotificationProperty.set(notification);
                    setText(notification.getId() + " | " +  notification.getSender().getName() + " - " + notification.getTimestamp());
                }
            }
        });

        notiTypeProperty.addListener((observable, oldValue, newValue) -> {
            Set<Notification> notifications = getNoFilter();
            notifications = filterByType(notifications, newValue);
            notifications = filterByRole(notifications, roleFilterProperty.get());
            loadListView(notifications);
            System.out.println("Type filter changed");
        });

        roleFilterProperty.addListener((observable, oldValue, newValue) -> {
            Set<Notification> notifications = getNoFilter();
            notifications = filterByType(notifications, notiTypeProperty.get());
            notifications = filterByRole(notifications, newValue);
            loadListView(notifications);
            System.out.println("Role filter changed");
        });
        loadListView(getNoFilter());
    }

    private void approveRequest() {
    }

    private void denyRequest() {
    }

    private void deleteNoti() {
//        currentUser.get().getReceivedNotifications().clear();
//        currentUser.get().getSentNotifications().clear();
//        loadListView(getNoFilter());
//        HostDAO hostDAO = new HostDAO();
//        hostDAO.update(currentUser.get());
//        System.out.println("Notification deleted");
    }

    private void setButtonVisible(boolean visible){
        approve_btn.setVisible(visible);
        deny_btn.setVisible(visible);
    }

    private Set<Notification> getNoFilter(){
        Set<Notification> notifications = new HashSet<>();
        notifications.addAll(currentUser.get().getSentNotifications());
        notifications.addAll(currentUser.get().getReceivedNotifications());
        return notifications;
    }

    private Set<Notification> filterByType(Set<Notification> notifications, NOTI_TYPE_FILTER type){
        if(type == NOTI_TYPE_FILTER.NONE) return notifications;

        Set<Notification> filtered = new HashSet<>();
        if(type == NOTI_TYPE_FILTER.REQUEST){
            for(Notification notification : notifications){
                if(notification instanceof Request && type == NOTI_TYPE_FILTER.REQUEST){
                    filtered.add(notification);
                }
                else filtered.add(notification);
            }
        }
        return filtered;
    }

    private Set<Notification> filterByRole(Set<Notification> notifications, ROLE_FILTER role){
        if(role == NONE) return notifications;
        else if(role == SENDER) return currentUser.get().getSentNotifications();
        else return currentUser.get().getReceivedNotifications();
    }

    private void loadListView(Set<Notification> notifications){
        noti_ListView.getItems().clear();
        noti_ListView.getItems().addAll(notifications);
    }

    private void showDetail(Notification notification){
        if(notification == null) return;
        if(notification instanceof Request) type_label.setText("REQUEST");
        else type_label.setText("ANNOUNCEMENT");

        mainContent_textArea.setText(notification.getContent());
        sender_label.setText(notification.getSender().namePropertyProperty().get());
        receiver_label.setText("You and " + notification.getTotalReceivers() + " others");
        timestamp_label.setText(notification.getTimestamp());

    }
}
