package org.rmit.controller.Admin;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class OwnerManagerController implements Initializable {
    public Label welcomeLabel;
    public TableView properties_tableView;
    public ComboBox propertyTypeFilter_comboBox;
    public ComboBox propertyStatusFilter_comboBox;
    public Button addOwnerButton;
    public Button updateOwnerButton;
    public Button deleteOwnerButton;
    public Button readOwnerButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
