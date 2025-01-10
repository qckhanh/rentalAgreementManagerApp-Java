package org.rmit.controller.Owner;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.rmit.Helper.EntityGraphUtils;
import org.rmit.Helper.NotificationUtils;
import org.rmit.Helper.UIDecorator;
import org.rmit.Notification.NormalNotification;
import org.rmit.Notification.Notification;
import org.rmit.Notification.Request;
import org.rmit.database.*;
import org.rmit.view.ViewCentral;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Owner;
import org.rmit.model.Property.CommercialProperty;
import org.rmit.model.Property.Property;
import org.rmit.model.Property.ResidentialProperty;
import org.rmit.model.Session;
import org.rmit.view.Host.NOTI_TYPE_FILTER;
import org.rmit.view.Host.ROLE_FILTER;
import org.rmit.view.Start.NOTIFICATION_TYPE;

import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Function;

import static org.rmit.view.Host.ROLE_FILTER.*;
import static org.rmit.view.Host.ROLE_FILTER.SEND;

public class Owner_NotificationController implements Initializable {
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
    public ObjectProperty<Owner> currentUser = new SimpleObjectProperty<>((Owner) Session.getInstance().getCurrentUser());

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        decor();
        setUpData();
        setUpDataBehavior();
        setUpButtonAction();
        loadListView(getNoFilter());
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
        Request request = (Request) selectedNotificationProperty.get();
        if(request.isAllApproved() == true){
            ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.ERROR, anchorPane, "Cannot do further action on this request");
            return;
        }
        if(!ViewCentral.getInstance().getStartViewFactory().confirmMessage("Are you sure you want to approve this request?")) return;

        currentUser.get().acceptRequest(request);
        int draftID = NotificationUtils.getDraftID(request.getDraftObject());
        String draftType = NotificationUtils.getDraftType(request.getDraftObject());
        HostDAO hostDAO = new HostDAO();
        OwnerDAO ownerDAO = new OwnerDAO();
        Property property = null;

        Host host = hostDAO.get(Integer.parseInt(request.getSender().getId() + ""), EntityGraphUtils::SimpleHostFull);

        if(draftType.equals("CommercialProperty") || draftType.equals("ResidentialProperty")){
            if(draftType.equals("CommercialProperty")){
                CommercialPropertyDAO dao = new CommercialPropertyDAO();
                property = (CommercialProperty) dao.get(draftID, EntityGraphUtils::SimpleCommercialProperty);
            }
            else{
                ResidentialPropertyDAO dao = new ResidentialPropertyDAO();
                property = (ResidentialProperty) dao.get(draftID, EntityGraphUtils::SimpleResidentialProperty);
            }

            if(property == null){
                ViewCentral.getInstance().getStartViewFactory().pushNotification(
                        NOTIFICATION_TYPE.ERROR,
                        anchorPane,
                        "Cannot found property with ID: " + draftID + ". Cannot approve request"
                );
                denyRequest();
                return;
            }
            host.addProperty(property);
            boolean isUpadated = hostDAO.update(host);
            if(!isUpadated){
                ViewCentral.getInstance().getStartViewFactory().pushNotification(
                        NOTIFICATION_TYPE.ERROR,
                        anchorPane,
                        "Fail to add property to host. Please try again"
                );
                return;
            }
            String header = String.format(
                    NotificationUtils.HEADER_REQUEST_PROPERTY,
                    property.getId(),
                    property.getAddress()
            );

            String content = String.format(
                    NotificationUtils.CONTENT_APPROVE_PROPERTY,
                    request.getSender().getName(),
                    currentUser.get().getName(),
                    property.getId(),
                    property.getAddress()
            );
            NormalNotification notification = (NormalNotification) NotificationUtils.createNormalNotification(
                    currentUser.get(),
                    Arrays.asList(request.getSender()),
                    header,
                    content
            );
            currentUser.get().sentNotification(notification);
            boolean isSent =  ownerDAO.update(currentUser.get());
            NotificationDAO notificationDAO = new NotificationDAO();
            notificationDAO.update(notification);
            if(isSent){
                ViewCentral.getInstance().getStartViewFactory().pushNotification(
                        NOTIFICATION_TYPE.SUCCESS,
                        anchorPane,
                        "Notification successfully sent"
                );
            }
            else{
                ViewCentral.getInstance().getStartViewFactory().pushNotification(
                        NOTIFICATION_TYPE.ERROR,
                        anchorPane,
                        "Notification failed to send. Please try again"
                );
            }
        }

    }

    private void denyRequest() {
        Request request = (Request) selectedNotificationProperty.get();
        if(request.isAllApproved()){
            ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.ERROR, anchorPane, "Cannot do further action on this request");
            return;
        }
        if(!ViewCentral.getInstance().getStartViewFactory().confirmMessage("Are you sure you want to deny this request?")) return;

        currentUser.get().denyRequest(request);
        String header = "DENY REQUEST";
        String content = "Dear %s, \n"
                + "Your request to manage the property with ID: "+ NotificationUtils.getDraftID(request.getDraftObject())
                + " has been denied by " + currentUser.get().getName();

        NormalNotification notification = (NormalNotification) NotificationUtils.createNormalNotification(
                currentUser.get(),
                Arrays.asList(request.getSender()),
                header,
                content
        );
        currentUser.get().sentNotification(notification);
        OwnerDAO ownerDAO = new OwnerDAO();
        boolean isUpdated =  ownerDAO.update(currentUser.get());
        if(isUpdated){
            ViewCentral.getInstance().getStartViewFactory().pushNotification(
                    NOTIFICATION_TYPE.SUCCESS,
                    anchorPane,
                    "Notification successfully sent");
        }
        else{
            ViewCentral.getInstance().getStartViewFactory().pushNotification(
                    NOTIFICATION_TYPE.ERROR,
                    anchorPane,
                    "Notification failed to send. Please try again"
            );
        }

    }

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
