package org.rmit.controller.Admin;

import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.rmit.database.AdminDAO;
import org.rmit.database.HostDAO;
import org.rmit.database.OwnerDAO;
import org.rmit.database.RenterDAO;
import org.rmit.model.Persons.Admin;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Owner;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Session;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class Admin_DashboardController implements Initializable {
    public TextField search_input;
    public Button search_button;
    public Label welcomeLabel;
    public Label totalAgreement_label;
    public Label totalPayments_label;
    public ListView upcommingPayment_listView;
    public PieChart pieChart;
    public ObservableList<PieChart.Data> pieChartData;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        welcomeLabel.setText("Welcome " + Session.getInstance().getCurrentUser().getName());
        Session.getInstance().getCurrentUser().namePropertyProperty().addListener((observable, oldValue, newValue) ->
                welcomeLabel.setText("Welcome " + newValue)
        );

        int a = countSystemNumberOfHost();
        setPieChart();
    }

    private void setPieChart() {

    }

    // Helper methods to create the PieChart:
    private int countSystemNumberOfHost() {
        HostDAO hostDAO = new HostDAO();
        List<Host> hosts = hostDAO.getAll();
        System.out.println(hosts.size());
        return hosts.size();
    }


    private int countSystemNumberOfOwner() {
        OwnerDAO ownerDAO = new OwnerDAO();
        List<Owner> owners = ownerDAO.getAll();
        System.out.println(owners.size());
        return owners.size();
    }

    private int countSystemNumberOfRenter() {
        RenterDAO renterDAO = new RenterDAO();
        List<Renter> renters = renterDAO.getAll();
        return renters.size();
    }

    private int countSystemNumberOfAdmin() {
        AdminDAO adminDAO = new AdminDAO();
        List<Admin> admins = adminDAO.getAll();
        return admins.size();
    }

    private double getTotalValue(){
        return 0;
    }
}
