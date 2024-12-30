package org.rmit.controller.Owner;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.rmit.Notification.NormalNotification;
import org.rmit.Notification.Notification;
import org.rmit.Notification.Request;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Owner;
import org.rmit.model.Session;
import org.rmit.view.Host.NOTI_TYPE_FILTER;
import org.rmit.view.Host.ROLE_FILTER;

import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

import static org.rmit.view.Host.ROLE_FILTER.*;
import static org.rmit.view.Host.ROLE_FILTER.SENDER;

public class Owner_NotificationController implements Initializable {
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
    public ObjectProperty<Owner> currentUser = new SimpleObjectProperty<>((Owner) Session.getInstance().getCurrentUser());
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
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
                    setText(notification.getId() + " | " +  notification.getSender().getName() + " - " + notification.getTimestamp().toString());
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
                if(notification instanceof Request){
                    filtered.add(notification);
                }
            }
        }
        else if(type == NOTI_TYPE_FILTER.NORMAL){
            for(Notification notification : notifications){
                if(notification instanceof NormalNotification){
                    filtered.add(notification);
                }
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
        System.out.println("Show detail");
        if(notification instanceof Request){
            type_label.setText("Request");
            mainContent_textArea.setText(((Request)notification).getDraftObject());
        }
        else{
            type_label.setText("Normal");
            mainContent_textArea.setText(notification.getMessage());
        }
        sender_label.setText(notification.getSender().namePropertyProperty().get());
        receiver_label.setText("Many receivers");
        timestamp_label.setText(notification.getTimestamp().toString());
        System.out.println("Show detail done");

    }
}
