package org.rmit.controller.Admin;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class renterManagerController implements Initializable {
    public TableView properties_tableView;
    public Label welcomeLabel;
    public ComboBox propertyTypeFilter_comboBox;
    public ComboBox propertyStatusFilter_comboBox;
    public Button addRenterButton;
    public Button updateRenterButton;
    public Button deleteRenterButton;
    public Button readRenterButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
