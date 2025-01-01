package org.rmit.controller.Owner;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.rmit.Helper.DateUtils;
import org.rmit.Helper.NotificationUtils;
import org.rmit.Notification.NormalNotification;
import org.rmit.Notification.Notification;
import org.rmit.Notification.Request;
import org.rmit.database.*;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Owner;
import org.rmit.model.Persons.Person;
import org.rmit.model.Property.CommercialProperty;
import org.rmit.model.Property.Property;
import org.rmit.model.Property.ResidentialProperty;
import org.rmit.model.Session;
import org.rmit.view.Host.NOTI_TYPE_FILTER;
import org.rmit.view.Host.ROLE_FILTER;

import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
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

        noti_ListView.setCellFactory(p -> new ListCell<Notification>(){
            @Override
            protected void updateItem(Notification notification, boolean empty){
                super.updateItem(notification, empty);
                if(empty || notification == null){
                    setText(null);
                    setOnMouseClicked(null);
                }
                else{
                    if(notification.getSender().getId() == currentUser.get().getId()) setButtonVisible(false);
                    else setButtonVisible(true);
                    selectedNotificationProperty.set(notification);
                    setText(notification.getId() + " | " +  notification.getSender().getName() + " - " + notification.getTimestamp().toString());
                }
            }
        });

        noti_ListView.setOnMouseClicked(e -> showDetail(noti_ListView.getSelectionModel().getSelectedItem()));
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
        approve_btn.setOnAction(e -> approve());
        deny_btn.setOnAction(e -> deny());
    }

    private int getIDFromDraftObject(String draftObject) {
        String[] parts = draftObject.split(" ");
        return Integer.parseInt(parts[parts.length - 1]);
    }

    private void deny() {
        Request request = (Request) selectedNotificationProperty.get();
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
        ownerDAO.update(currentUser.get());
        System.out.println("Request denied and notification sent");
    }

    public void sentNotification(String message, Person sender, Person receiver){
        Notification notification = new NormalNotification();
        notification.setContent(message);
        notification.setSender(sender);
        notification.addReceiver(receiver);
        notification.setTimestamp(DateUtils.formatDate(LocalDate.now()));
        sender.sentNotification(notification);
    }


    private void approve() {
        Request request = (Request) selectedNotificationProperty.get();
        if(request.isAllApproved() == true){
            System.out.println("All approved");
            return;
        }

        currentUser.get().acceptRequest(request);
        int draftID = NotificationUtils.getDraftID(request.getDraftObject());
        String draftType = NotificationUtils.getDraftType(request.getDraftObject());
        HostDAO hostDAO = new HostDAO();
        OwnerDAO ownerDAO = new OwnerDAO();
        Property property = null;

        Host host = hostDAO.get(Integer.parseInt(request.getSender().getId() + ""));

        if(draftType.equals("CommercialProperty") || draftType.equals("ResidentialProperty")){
            if(draftType.equals("CommercialProperty")){
                CommercialPropertyDAO dao = new CommercialPropertyDAO();
                property = (CommercialProperty) dao.get(draftID);
            }
            else{
                ResidentialPropertyDAO dao = new ResidentialPropertyDAO();
                property = (ResidentialProperty) dao.get(draftID);
            }

            if(property == null){
                System.out.println("Property not found");
                return;
            }
            host.addProperty(property);
            boolean isUpadated = hostDAO.update(host);
            if(!isUpadated){
                System.out.println("Host not updated");
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
            ownerDAO.update(currentUser.get());
            System.out.println("Property added and notification sent");
        }

//        if(draftObject.startsWith("PROPERTY")){
//            System.out.println("This is a property request");
//            int propertyID = getIDFromDraftObject(draftObject);
//
//            Property property = null;
//            DAOInterface propertyDAO = new CommercialPropertyDAO();
//            property = (CommercialProperty)propertyDAO.get(propertyID);
//            if(property == null){
//                propertyDAO = new ResidentialPropertyDAO();
//                property = (ResidentialProperty)propertyDAO.get(propertyID);
//            }
//            if(property == null){
//                System.out.println("Property not found");
//                return;
//            }
//            HostDAO hostDAO = new HostDAO();
//            OwnerDAO ownerDAO = new OwnerDAO();
//            Host host = (Host) hostDAO.get(Integer.parseInt(request.getSender().getId() + ""));
//            host.addProperty(property);
//            if(hostDAO.update(host)){
//                System.out.println("Host updated and property added");
//                sentNotification(
//                        "Your request to add property " + property.getId() + " | Address: " + property.getAddress() + " has been approved",
//                        currentUser.get(),
//                        request.getSender()
//                );
//                hostDAO.update(host);
//                ownerDAO.update(currentUser.get());
//                System.out.println("Notification sent and saved");
//            }
//            else{
//                System.out.println("Host not updated");
//            }
//
//        }
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
