package org.rmit.controller.Renter;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.rmit.Helper.UIDecorator;
import org.rmit.Notification.Notification;
import org.rmit.Notification.Request;
import org.rmit.database.*;
import org.rmit.view.ViewCentral;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Session;
import org.rmit.view.Host.NOTI_TYPE_FILTER;
import org.rmit.view.Host.ROLE_FILTER;
import org.rmit.view.Start.NOTIFICATION_TYPE;

import java.net.URL;
import java.util.*;
import java.util.function.Function;

import static org.rmit.view.Host.ROLE_FILTER.*;
import static org.rmit.view.Host.ROLE_FILTER.SEND;

public class Renter_NotificationController implements Initializable {
    public AnchorPane anchorPane;

    public Label type_label;
    public Label sender_label;
    public Label timestamp_label;
    public Label receiver_label;
    public TextArea mainContent_textArea;

    public Button approve_btn;
    public Button deny_btn;
    public Button deleteNoti_btn;

    public TableView<Notification> notificationTableView;
    public ComboBox<ROLE_FILTER> roleFilter_comboBox;
    public ComboBox<NOTI_TYPE_FILTER> notiType_comboBox;

    public ObjectProperty<ROLE_FILTER> roleFilterProperty = new SimpleObjectProperty<>(NONE);
    public ObjectProperty<NOTI_TYPE_FILTER> notiTypeProperty = new SimpleObjectProperty<>(NOTI_TYPE_FILTER.NONE);
    public ObjectProperty<Notification> selectedNotificationProperty = new SimpleObjectProperty<>(null);
    public ObjectProperty<Renter> currentUser = new SimpleObjectProperty<>((Renter) Session.getInstance().getCurrentUser());

    private ObservableList<Notification> notifications = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        decor();
        setUpData();
        setUpDataBehavior();
        setUpButtonAction();
        loadListView(getNoFilter());

        notifications.addAll(currentUser.get().receivedNotificationsPropertyProperty().get());
        notifications.addAll(currentUser.get().sentNotificationsPropertyProperty().get());
    }

    // Setup
    private void setUpData(){
        notificationTableView.getColumns().addAll(
                createColumn("ID", "id"),
                createColumn("Sender", "sender", notification -> notification.getSender().getName()),
                createColumn("Time", "timestamp")
        );

        roleFilter_comboBox.getItems().addAll(
                SEND,
                RECEIVE,
                ROLE_FILTER.NONE
        );
        notiType_comboBox.getItems().addAll(
                NOTI_TYPE_FILTER.REQUEST,
                NOTI_TYPE_FILTER.NORMAL,
                NOTI_TYPE_FILTER.NONE
        );
    }
    private void setUpDataBehavior(){
        approve_btn.setVisible(false);
        deny_btn.setVisible(false);

        notiType_comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            notiTypeProperty.set(newValue);
        });
        roleFilter_comboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            roleFilterProperty.set(newValue);
        });



        notificationTableView.getSelectionModel().selectedItemProperty().addListener((o, oldValue, newValue) -> {
            selectedNotificationProperty.set(notificationTableView.getSelectionModel().getSelectedItem());
            if(newValue == null) return;
            showDetail(notificationTableView.getSelectionModel().getSelectedItem());
            if(newValue instanceof Request && newValue.getSender().getId() != currentUser.get().getId()){
                approve_btn.setVisible(true);
                deny_btn.setVisible(true);
                if(((Request) newValue).isAllApproved()){
                    approve_btn.setDisable(true);
                    deny_btn.setDisable(true);
                }
                else{
                    approve_btn.setDisable(false);
                    deny_btn.setDisable(false);
                }
            }
            else{
                approve_btn.setVisible(false);
                deny_btn.setVisible(false);
            }
        });

        notificationTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        notificationTableView.getSortOrder().add(notificationTableView.getColumns().get(1));
        mainContent_textArea.setWrapText(true);

// You can also set the preferred size or other properties as needed
        mainContent_textArea.setPrefWidth(320);
        mainContent_textArea.setPrefHeight(200);

        notiTypeProperty.setValue(NOTI_TYPE_FILTER.NONE);
        roleFilterProperty.setValue(NONE);
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
    }
    private void setUpButtonAction(){
        approve_btn.setOnAction(e -> approveRequest());
        deny_btn.setOnAction(e -> denyRequest());
        deleteNoti_btn.setOnAction(e -> deleteNoti());
    }
    private void decor(){
        UIDecorator.setSuccessButton(approve_btn, UIDecorator.APPROVE(), "Approve");
        UIDecorator.setDangerButton(deny_btn, UIDecorator.DENY(), "Deny");
        UIDecorator.setDangerButton(deleteNoti_btn, UIDecorator.DELETE(), null);
    }

    // Action
    private void approveRequest() {
    }

    private void denyRequest() {}

    private void deleteNoti() {
        if(!ViewCentral.getInstance().getStartViewFactory().confirmMessage("Are you sure you want to delete this notification?")) return;
        Notification notification = selectedNotificationProperty.get();
        int id = Integer.parseInt(notification.getId() + "");
        NotificationDAO notificationDAO = new NotificationDAO();
        notificationDAO.delete(notification);
        if (currentUser.get().getSentNotifications().contains(notification)) {
            currentUser.get().getSentNotifications().remove(notification);
        } else {
            currentUser.get().getReceivedNotifications().remove(notification);
        }

        currentUser.get().getSentNotifications().remove(notification);
        currentUser.get().getReceivedNotifications().remove(notification);

        loadListView(getNoFilter());

        ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.SUCCESS, anchorPane, "Notification deleted successfully");
    }

    // Helper

    private Set<Notification> getNoFilter(){
        Set<Notification> notifications = new HashSet<>();
        notifications.addAll(currentUser.get().getSentNotifications());
        notifications.addAll(currentUser.get().getReceivedNotifications());


        return notifications;
    }

    private Set<Notification> filterByType(Set<Notification> notifications, NOTI_TYPE_FILTER type){
        if(type.equals(NOTI_TYPE_FILTER.NONE)) return notifications;

        Set<Notification> filtered = new HashSet<>();

        for(Notification notification : notifications){
            if(type.equals(NOTI_TYPE_FILTER.REQUEST)){
                if(notification instanceof Request) filtered.add(notification);
            }
            else{
                if(!(notification instanceof Request)) filtered.add(notification);
            }
        }
        return filtered;
    }

    private Set<Notification> filterByRole(Set<Notification> notifications, ROLE_FILTER role){
        if(role.equals(NONE)) return notifications;
        Set<Notification> list = new HashSet<>();
        for(Notification notification : notifications){
            if(role.equals(SEND)){
                if(notification.getSender().getId() == currentUser.get().getId()) list.add(notification);
            }
            else{
                if(notification.getSender().getId() != currentUser.get().getId()) list.add(notification);
            }

        }
        return list;
    }

    private void loadListView(Set<Notification> notifications){
        notificationTableView.getItems().clear();
        notificationTableView.getItems().addAll(notifications);
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

    private <S, T> TableColumn<S, T> createColumn(String columnName, String propertyName) {
        TableColumn<S, T> column = new TableColumn<>(columnName);
        column.setCellValueFactory(new PropertyValueFactory<>(propertyName));
        return column;
    }

    private <S, T> TableColumn<S, T> createColumn(String columnName, String propertyName, Function<S, T> extractor) {
        TableColumn<S, T> column = new TableColumn<>(columnName);

        column.setCellValueFactory(cellData -> new SimpleObjectProperty<>(extractor.apply(cellData.getValue())));
        column.setCellFactory(col -> new TableCell<S, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setStyle(""); // Reset cell style for empty cells
                    setOnMouseClicked(null); // Remove any click listeners when the cell is empty
                } else {
                    setText(item.toString());
                }
            }
        });

        return column;
    }
}
