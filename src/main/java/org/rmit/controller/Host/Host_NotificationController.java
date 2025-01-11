package org.rmit.controller.Host;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.rmit.Helper.EntityGraphUtils;
import org.rmit.Helper.NotificationUtils;
import org.rmit.Helper.TaskUtils;
import org.rmit.Helper.UIDecorator;
import org.rmit.Notification.Notification;
import org.rmit.Notification.Request;
import org.rmit.database.*;
import org.rmit.model.Agreement.AgreementStatus;
import org.rmit.model.Agreement.RentalAgreement;
import org.rmit.view.ViewCentral;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Owner;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Property.*;
import org.rmit.model.Session;
import org.rmit.view.Host.NOTI_TYPE_FILTER;
import org.rmit.view.Host.ROLE_FILTER;
import org.rmit.view.Start.NOTIFICATION_TYPE;

import javax.swing.plaf.multi.MultiOptionPaneUI;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;

import static org.rmit.view.Host.ROLE_FILTER.*;

public class Host_NotificationController implements Initializable {
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
    public ObjectProperty<Host> currentUser = new SimpleObjectProperty<>((Host) Session.getInstance().getCurrentUser());

    Set<Renter> subRentersSet = new HashSet<>();
    Renter mainRenter;
    Owner owner;
    Property property;

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
        if(request.isAllApproved()){
            ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.ERROR, anchorPane, "Cannot do further action on this request");
            return;
        }
        if(!ViewCentral.getInstance().getStartViewFactory().confirmMessage("Are you sure you want to approve this request? By accept this request, you will approve the request action.")) return;

        //reset
        mainRenter = null;
        owner = null;
        property = null;
        subRentersSet.clear();

        approve_btn.setDisable(true);
        currentUser.get().acceptRequest(request);

        String draft = request.getDraftObject();
        if(NotificationUtils.getDraftType(draft).equals("RentalAgreement")){
            //set up entity
            List<String> ids = NotificationUtils.draftID_RentalAgreement(draft);
            long mainRenterId = request.getSender().getId();
            long propertyID = Long.parseLong(ids.get(0));
            long hostID = Long.parseLong(ids.get(1));
            long ownerID = Long.parseLong(ids.get(2));
            RentalPeriod rentalPeriod = RentalPeriod.valueOf(ids.get(3));
            List<Integer> subRenters = new ArrayList<>();
            for(int i = 4; i < ids.size(); i++){
                subRenters.add(Integer.parseInt(ids.get(i)));
            }

            // get entity
            RenterDAO renterDAO = new RenterDAO();
            HostDAO hostDAO = new HostDAO();
            OwnerDAO ownerDAO = new OwnerDAO();
            RentalAgreementDAO rentalAgreementDAO = new RentalAgreementDAO();
            ViewCentral.getInstance().getStartViewFactory().standOnNotification(NOTIFICATION_TYPE.INFO, anchorPane, "Approving request...");
            CountDownLatch latch = new CountDownLatch(3 + subRenters.size());
            Task<Renter> getMainRenter = TaskUtils.createTask(() -> renterDAO.get(Integer.parseInt(mainRenterId + ""), EntityGraphUtils::SimpleRenter));
            Task<Owner> getOwner = TaskUtils.createTask(() -> ownerDAO.get(Integer.parseInt(ownerID + ""), EntityGraphUtils::SimpleOwner));
            Task<Property> getProperty = TaskUtils.createTask(() -> {
                CommercialPropertyDAO dao = new CommercialPropertyDAO();
                Property property = (Property) dao.get(Integer.parseInt(propertyID + ""), EntityGraphUtils::SimpleCommercialProperty);
                if(property == null){
                    ResidentialPropertyDAO dao2 = new ResidentialPropertyDAO();
                    property = (Property) dao2.get(Integer.parseInt(propertyID + ""), EntityGraphUtils::SimpleResidentialProperty);
                }
                return property;
            });
            for(int id: subRenters){
                Task<Renter> getSubRenter = TaskUtils.createTask(() -> renterDAO.get(id, EntityGraphUtils::SimpleRenter));
                TaskUtils.run(getSubRenter);
                getSubRenter.setOnSucceeded(e -> Platform.runLater(() -> {
                    Renter subRenter = getSubRenter.getValue();
                    subRentersSet.add(subRenter);
                    latch.countDown();
                }));
            }

            TaskUtils.run(getMainRenter);
            TaskUtils.run(getOwner);
            TaskUtils.run(getProperty);

            getMainRenter.setOnSucceeded(e -> Platform.runLater(() -> {
                mainRenter = getMainRenter.getValue();
                latch.countDown();
            }));
            getOwner.setOnSucceeded(e -> Platform.runLater(() -> {
                owner = getOwner.getValue();
                latch.countDown();
            }));
            getProperty.setOnSucceeded(e -> Platform.runLater(() -> {
                property = getProperty.getValue();
                latch.countDown();
            }));

            Callable<Void> task = () -> {
                try {
                    latch.await();

                    RentalAgreement rentalAgreement = new RentalAgreement();
                    rentalAgreement.setMainTenant(mainRenter);
                    rentalAgreement.setProperty(property);
                    property.setStatus(PropertyStatus.RENTED);
                    rentalAgreement.setHost(currentUser.get());
                    rentalAgreement.setPeriod(rentalPeriod);
                    rentalAgreement.setContractDate(LocalDate.now());
                    rentalAgreement.setSubTenants(subRentersSet);
                    rentalAgreement.setStatus(AgreementStatus.NEW);
                    rentalAgreement.setRentingFee(property.getPrice());


                    Task<Boolean> addRentalAgreement = TaskUtils.createTask(() -> rentalAgreementDAO.add(rentalAgreement));
                    TaskUtils.run(addRentalAgreement);

                    Platform.runLater(() -> {
                        addRentalAgreement.setOnSucceeded(e -> Platform.runLater(() -> {
                            CountDownLatch latch2 = new CountDownLatch(3 + subRentersSet.size());

                            //main renter
                            Task<Boolean> updateMainRenter = TaskUtils.createTask(() -> {
                                mainRenter.addAgreement(rentalAgreement);
                                return renterDAO.update(mainRenter);
                            });
                            updateMainRenter.setOnSucceeded(e2 -> Platform.runLater(() -> {
                                latch2.countDown();
                            }));

                            //sub renter
                            for (Renter r : subRentersSet) {
                                Task<Boolean> addSubAgreement = TaskUtils.createTask(() -> {
                                    r.addSubAgreement(rentalAgreement);
                                    return renterDAO.update(r);
                                });
                                addSubAgreement.setOnSucceeded(e2 -> Platform.runLater(() -> {
                                    latch2.countDown();
                                }));
                                TaskUtils.run(addSubAgreement);
                            }

                            // update current user
                            Task<Boolean> updateCurrentUser = TaskUtils.createTask(() -> {
                                currentUser.get().addAgreement(rentalAgreement);
                                return hostDAO.update(currentUser.get());
                            });

                            // update notification
                            Task<Boolean> updateNotification = TaskUtils.createTask(() -> {
                                request.setAllApproved(true);
                                return new NotificationDAO().update(request);
                            });

                            //on success
                            updateNotification.setOnSucceeded(e2 -> Platform.runLater(() -> {
                                latch2.countDown();
                            }));
                            updateCurrentUser.setOnSucceeded(e2 -> Platform.runLater(() -> {
                                latch2.countDown();
                            }));

                            //run
                            TaskUtils.run(updateCurrentUser);
                            TaskUtils.run(updateNotification);
                            TaskUtils.run(updateMainRenter);

                            Callable<Void> task2 = () -> {
                                try {
                                    latch2.await();
                                    Platform.runLater(() -> {
                                        ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.SUCCESS, anchorPane, "Request approved successfully");
                                    });
                                } catch (InterruptedException e3) {
                                    e3.printStackTrace();
                                }
                                return null;
                            };
                            TaskUtils.countDown(latch2, task2);
                        }));
                    });

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            };
            TaskUtils.countDown(latch, task);
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
        HostDAO hostDAO = new HostDAO();
        Task<Boolean> update = TaskUtils.createTask(() -> hostDAO.update(currentUser.get()));
        TaskUtils.run(update);
        ViewCentral.getInstance().getStartViewFactory().standOnNotification(NOTIFICATION_TYPE.INFO, anchorPane, "Denying request...");
        update.setOnSucceeded(e -> Platform.runLater(() -> {
            ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.SUCCESS, anchorPane, "Request denied successfully");
        }));
    }

    private void deleteNoti() {
        Notification notification = notificationTableView.getSelectionModel().getSelectedItem();
        if(notification == null){
            ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.WARNING, anchorPane, "No notification selected");
            return;
        }
        if(!ViewCentral.getInstance().getStartViewFactory().confirmMessage("Are you sure you want to delete this notification?")) return;

        int id = Integer.parseInt(notification.getId() + "");
        NotificationDAO notificationDAO = new NotificationDAO();

        Task<Boolean> deleteTask = TaskUtils.createTask(() -> notificationDAO.delete(notification));
        TaskUtils.run(deleteTask);
        ViewCentral.getInstance().getStartViewFactory().standOnNotification(NOTIFICATION_TYPE.INFO, anchorPane, "Deleting notification...");
        deleteTask.setOnSucceeded(e -> Platform.runLater(() -> {
            if(deleteTask.getValue()){
                ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.SUCCESS, anchorPane, "Notification deleted successfully");
                if (currentUser.get().getSentNotifications().contains(notification)) {
                    currentUser.get().getSentNotifications().remove(notification);
                } else {
                    currentUser.get().getReceivedNotifications().remove(notification);
                }
                loadListView(getNoFilter());
            }
            else {
                ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.ERROR, anchorPane, "Notification deleted failed. Try again");
            }
        }));
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
