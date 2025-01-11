package org.rmit.controller.Host;

import atlantafx.base.layout.DeckPane;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.rmit.Helper.*;
import org.rmit.Notification.Request;
import org.rmit.database.*;
import org.rmit.model.Persons.Owner;
import org.rmit.view.ViewCentral;
import org.rmit.model.Persons.Host;
import org.rmit.model.Property.CommercialProperty;
import org.rmit.model.Property.Property;
import org.rmit.model.Property.PropertyStatus;
import org.rmit.model.Property.ResidentialProperty;
import org.rmit.model.Session;
import org.rmit.view.Start.NOTIFICATION_TYPE;

import java.net.URL;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

public class Host_ManagePropertyController implements Initializable {
    //preview
    public TextField propertyID_input;
    public TextField address_input;
    public TextField price_input;
    public TextField ownerName_input;
    public TextField statusProperty_input;
    public TextField propertyType_input;
    public TextField totalAgremeent_input;
    public TextField bussinessType_input;
    public TextField parkingSlot_input;
    public TextField squareArea_input;
    public TextField totalRoom_input;
    public TextField totalBedroom_input;
    public TextField petAllow_input;
    public TextField gadern_input;

    //manage
    public ListView<Property> property_listView;
    public Button manageProperty_btn;

    //search
    public TextField search_input;
    public Button search_btn;

    public Host currentUser = (Host) Session.getInstance().getCurrentUser();
//    public Set<Property> managedProperties = currentUser.getPropertiesManaged();
    public Set<Property> searchResult = new HashSet<>();
    public ObjectProperty<Set<Property>> managedProperties = new SimpleObjectProperty<>(currentUser.getPropertiesManaged());
    public ObjectProperty<Property> selectedProperty = new SimpleObjectProperty<>();
    public Map<Integer, Property> propertyMap = new HashMap<>();
    public ComboBox<PropertyStatus> propertyStatusCbox;
    public Button saveChangesButton;
    public Button prevImg_btn;
    public Button nextImg_btn;
    public DeckPane imageShow_deckPane;
    public ImageView imageView_propertyImg;
    public ObjectProperty<List<byte[]>> selectedImage = new SimpleObjectProperty<>();
    public int currentImageIndex = 0;
    public AnchorPane anchorPane;
    public List<Property> listPropertyFound = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        decor();
        saveChangesButton.setDisable(true);
        nextImg_btn.setOnAction(e -> nextImg_btn());
        prevImg_btn.setOnAction(e -> prevImg_btn());
        search_btn.setOnAction(event -> searchProperty());
        search_input.setOnAction(event -> searchProperty());
        search_input.textProperty().addListener((o, old, neww) ->{
            if(neww.isBlank()) property_listView.setItems(getPropertyList(managedProperties.get()));
        });

        selectedProperty.addListener((observable, oldValue, newValue) -> {
            imageView_propertyImg.setImage(ImageUtils.byteToImage(null));
            showPropertyDetail(newValue);
            saveChangesButton.setDisable(true);
        });

        property_listView.setItems(getPropertyList(managedProperties.get()));

        propertyStatusCbox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Property selected = selectedProperty.get();
            if (selected != null && selected.getStatus() != newValue) {
                saveChangesButton.setDisable(false);
            }
            else {
                saveChangesButton.setDisable(true);
            }
        });
        saveChangesButton.setOnAction(e-> saveChanges());

        property_listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                propertyStatusCbox.setDisable(true);
                selectedProperty.set(newValue);
                isManagingThisProperty(newValue);
                showPropertyDetail(newValue);
            }
        });

        // Create clickable rows with a custom cell factory
        property_listView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(Property property, boolean empty) {
                super.updateItem(property, empty);
                if (empty || property == null) {
                    UIDecorator.setNormalButton(manageProperty_btn, UIDecorator.MANAGE(), "Select a property");
                    setText(null);
                    setOnMouseClicked(null); // Remove click handler for empty cells
                } else {
//                    selectedProperty.set(property);
                    setText(property.getAddress()); // Display the address or another property field
//                    setOnMouseClicked(event -> {
//                        if (event.getClickCount() == 1) { // Single click
//
//                        }
//                    });
                }
            }
        });

    }

    private void saveChanges() {
        if(!ViewCentral.getInstance().getStartViewFactory().confirmMessage("Are you sure you want to save changes?")) return;
        selectedProperty.get().setStatus(propertyStatusCbox.getValue());
        Property selected = selectedProperty.get();
        boolean isUpdated = false;
        if (selected instanceof CommercialProperty) {
            CommercialPropertyDAO cpDAO = new CommercialPropertyDAO();
            isUpdated = cpDAO.update((CommercialProperty) selected);
        }
        else {
            ResidentialPropertyDAO rpDAO = new ResidentialPropertyDAO();
            isUpdated = rpDAO.update((ResidentialProperty) selected);
        }
        if (isUpdated) {
            int id = Integer.parseInt(selected.getId() + "");
            propertyMap.get(id).setStatus(selected.getStatus());
            ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.SUCCESS, anchorPane, "Status updated successfully");
        }
        else {
            ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.ERROR, anchorPane, "Failed to update status. Please try again");
        }
    }

    private void decor(){
        UIDecorator.setNormalButton(nextImg_btn, UIDecorator.NEXT(), null);
        UIDecorator.setNormalButton(prevImg_btn, UIDecorator.PREVIOUS(), null);
        UIDecorator.buttonIcon(search_btn, UIDecorator.SEARCH());
        UIDecorator.setNormalButton(manageProperty_btn, UIDecorator.MANAGE(), "Select a property");
        UIDecorator.setDangerButton(saveChangesButton, UIDecorator.SEND(), "Save Changes");
    }

    private void searchProperty() {
        if(search_input.getText().isBlank()) return;
        search_btn.setDisable(true);

        CommercialPropertyDAO dao = new CommercialPropertyDAO();
        ResidentialPropertyDAO dao2 = new ResidentialPropertyDAO();
        ViewCentral.getInstance().getStartViewFactory().standOnNotification(NOTIFICATION_TYPE.INFO, anchorPane, "Searching...");

        listPropertyFound.clear();
        CountDownLatch latch = new CountDownLatch(2);
        Task<List<CommercialProperty>> commercialTask = TaskUtils.createTask(() -> {
            return dao.search(search_input.getText(), EntityGraphUtils::SimpleCommercialProperty);
        });
        Task<List<ResidentialProperty>> residentialTask = TaskUtils.createTask(() -> {
            return dao2.search(search_input.getText(), EntityGraphUtils::SimpleResidentialProperty);
        });
//        TaskUtils.run(commercialTask);
        TaskUtils.run(residentialTask);

        commercialTask.setOnSucceeded(e -> {
            System.out.println("Commercial task done");
            listPropertyFound.addAll(commercialTask.getValue());
//            for(Property p: commercialTask.getValue()){
//                propertyMap.put(Integer.parseInt(p.getId() + ""), p);
//            }
            ViewCentral.getInstance().getStartViewFactory().standOnNotification(NOTIFICATION_TYPE.SUCCESS, anchorPane, "Search: 50% completed");
            latch.countDown();
        });
        residentialTask.setOnSucceeded(e -> {
            System.out.println("Residental task done");
            listPropertyFound.addAll(residentialTask.getValue());
//            for(Property p: residentialTask.getValue()){
//                propertyMap.put(Integer.parseInt(p.getId() + ""), p);
//            }
            ViewCentral.getInstance().getStartViewFactory().standOnNotification(NOTIFICATION_TYPE.SUCCESS, anchorPane, "Search: 50% completed");
            latch.countDown();
        });

        Callable<Void> task = () -> {
            try{
                latch.await();
                property_listView.setItems(getPropertyList(new HashSet<>(listPropertyFound)));
                ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.SUCCESS, anchorPane, "Found " + listPropertyFound.size() + " property(s)");
                search_btn.setDisable(false);
                System.out.println("doneeeeeeeee");
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
            return null;
        };
        TaskUtils.countDown(latch, task);
    }

    public ObservableList<Property> getPropertyList(Set<Property> pList){
        ObservableList<Property> list = FXCollections.observableArrayList();
        list.addAll(pList);
        return list;
    }

    private void unmanageProperty(Property property){
        if(!ViewCentral.getInstance().getStartViewFactory().confirmMessage("Are you sure you want to unmanage this property?")) return;
        manageProperty_btn.setDisable(true);
        ((Host)Session.getInstance().getCurrentUser()).removeProperty(property);
        managedProperties.get().remove(property);
        property_listView.setItems(getPropertyList(managedProperties.get()));
        DAOInterface dao = new HostDAO();
        boolean isUpdated =  dao.update(Session.getInstance().getCurrentUser());
        if(isUpdated){
            ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.SUCCESS, anchorPane, "Successfully unmanaged property");
        }
        else{
            ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.ERROR, anchorPane, "Failed to unmanage property");
        }
        manageProperty_btn.setDisable(false);
    }

    private void requestManageProperty(int propertyID){
        Property property = propertyMap.get(propertyID);
        System.out.println(property);
        if(!ViewCentral.getInstance().getStartViewFactory().confirmMessage("Are you sure you want to request manage this property?")) return;
        manageProperty_btn.setDisable(true);
        String content = String.format(
                NotificationUtils.CONTENT_REQUEST_PROPERTY,
                property.getOwner().getName(),
                currentUser.getName(),
                property.getId(),
                property.getAddress()
        );
        String header = String.format(
                NotificationUtils.HEADER_REQUEST_PROPERTY,
                property.getId(),
                property.getAddress()
        );
        String draftObject = String.format(
                ((property instanceof CommercialProperty)
                        ? NotificationUtils.DAFT_PROPERTY_COMMERCIAL
                        : NotificationUtils.DAFT_PROPERTY_RESIDENTIAL),
                property.getId()
        );
        OwnerDAO ownerDAO = new OwnerDAO();
        Owner owner = ownerDAO.get(Integer.parseInt(property.getOwner().getId() + ""), EntityGraphUtils::OwnerForEmailSent);
        Request request = (Request) NotificationUtils.createRequest(
                currentUser,
                new ArrayList<>(Collections.singletonList(owner)),
                header,
                content,
                draftObject
        );
        currentUser.sentNotification(request);
        HostDAO hostDAO = new HostDAO();
        boolean isUpdated =  hostDAO.update(currentUser);
        if(isUpdated){
            ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.SUCCESS, anchorPane, "Successfully requested manage property");
        }
        else{
            ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.ERROR, anchorPane, "Failed to request manage property");
        }
        manageProperty_btn.setDisable(false);
    }

    private void clearDataCommercial(){
        bussinessType_input.setText("");
        parkingSlot_input.setText("");
        squareArea_input.setText("");
    }

    private void clearDataResidential(){
        totalRoom_input.setText("");
        totalBedroom_input.setText("");
        petAllow_input.setText("");
        gadern_input.setText("");
    }

    private void clearDataProperty(){
        propertyID_input.setText("");
        address_input.setText("");
        price_input.setText("");
        ownerName_input.setText("");
        propertyStatusCbox.setValue(null);
        propertyType_input.setText("");
        totalAgremeent_input.setText("");
        clearDataCommercial();
        clearDataResidential();
    }

    private void  isManagingThisProperty(Property property){
        for(Property p: managedProperties.get()){
            if(p.getId() == property.getId()){
                UIDecorator.setDangerButton(manageProperty_btn, UIDecorator.DELETE(), "Unmanaged");
                manageProperty_btn.setOnAction(event -> unmanageProperty(property));
                propertyStatusCbox.setDisable(false);
                return;
            }
        }
        UIDecorator.setSuccessButton(manageProperty_btn, UIDecorator.ADD(), "Request Manage");
        manageProperty_btn.setOnAction(event -> requestManageProperty(Integer.parseInt(property.getId() + "")));
    }

    private void showPropertyDetail(Property property){
        clearDataProperty();
        int id = Integer.parseInt(property.getId()+"");
        ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.SUCCESS, anchorPane, "Loading property details...");
        if(propertyMap.containsKey(id)){
            property = propertyMap.get(id);
        }else{
            if(property.getType().toString().equals("COMMERCIAL")){
                CommercialPropertyDAO commercialPropertyDAO = new CommercialPropertyDAO();
                property = (CommercialProperty)commercialPropertyDAO.get(id, EntityGraphUtils::SimpleCommercialProperty);
            }
            else{
                ResidentialPropertyDAO residentialPropertyDAO = new ResidentialPropertyDAO();
                property = (ResidentialProperty)residentialPropertyDAO.get(id, EntityGraphUtils::SimpleResidentialProperty);
            }
        }
        ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.SUCCESS, anchorPane, "Loaded property details");
        currentImageIndex = 0;
        selectedImage.set(property.getImages());
        if(selectedImage.get().size() != 0) imageView_propertyImg.setImage(ImageUtils.byteToImage(selectedImage.get().get(currentImageIndex)));
        propertyMap.put(id, property);
        propertyID_input.setText(property.getId()+"");
        address_input.setText(property.getAddress());
        price_input.setText(property.getPrice()+"");
        ownerName_input.setText(property.getOwner().getName());
        propertyType_input.setText(property.getType().toString());
        propertyStatusCbox.setValue(property.getStatus());
        propertyStatusCbox.setItems(FXCollections.observableArrayList(
                PropertyStatus.AVAILABLE,
                PropertyStatus.UNAVAILABLE
        ));
//        totalAgremeent_input.setText(property.getAgreementList().size()+"");
        if(property.getType().toString().equals("COMMERCIAL")){
            CommercialProperty commercialProperty = (CommercialProperty) property;
            bussinessType_input.setText(commercialProperty.getBusinessType());
            parkingSlot_input.setText(commercialProperty.getTotalParkingSpace()+"");
            squareArea_input.setText(commercialProperty.getSquareMeters()+"");
        }
        else if(property.getType().toString().equals("RESIDENTIAL")){
            ResidentialProperty residentialProperty = (ResidentialProperty) property;
            totalRoom_input.setText(residentialProperty.getTotalRoom()+"");
            totalBedroom_input.setText(residentialProperty.getTotalBedroom()+"");
            petAllow_input.setText(residentialProperty.isPetAllowed()+"");
            gadern_input.setText(residentialProperty.isHasGarden()+"");
        }

    }

    private void prevImg_btn() {
        if(selectedImage.get().size() == 0 || selectedImage.get() == null){
            ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.ERROR, anchorPane, "No images to display");
            return;
        }
        int selectedImagesSize = selectedImage.get().size();
        int position = (currentImageIndex - 1 + selectedImagesSize) % selectedImagesSize;
        currentImageIndex = position;
        imageView_propertyImg.setImage(ImageUtils.byteToImage(selectedImage.get().get(position)));
    }

    private void nextImg_btn() {
        if(selectedImage.get().size() == 0 || selectedImage.get() == null){
            ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.ERROR, anchorPane, "No images to display");
            return;
        }
        int selectedImagesSize = selectedImage.get().size();

        int position = (currentImageIndex  + 1) % selectedImagesSize;
        currentImageIndex = position;
        imageView_propertyImg.setImage(ImageUtils.byteToImage(selectedImage.get().get(position)));
    }
}
