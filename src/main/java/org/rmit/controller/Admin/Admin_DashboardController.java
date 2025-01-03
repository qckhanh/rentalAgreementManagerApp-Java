package org.rmit.controller.Admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.rmit.database.AdminDAO;
import org.rmit.database.HostDAO;
import org.rmit.database.OwnerDAO;
import org.rmit.database.RenterDAO;
import org.rmit.model.Agreement.Payment;
import org.rmit.model.Persons.Admin;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Owner;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Session;

import java.net.URL;
import java.time.LocalDate;
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

        // Display the Graph:
        setPieChart();
    }

    // Function to Sketch the Graphs:
    private void setPieChart() {
        pieChartData = createPieChartDataPeople();
        pieChart.setData(pieChartData);

        pieChart.setTitle("Account's Role Distribution");
        pieChart.setMinSize(300,300);
        pieChart.setMaxSize(400,400);

        // Đặt legend bên phải và hiện
        pieChart.setLegendSide(Side.BOTTOM);
        pieChart.setLegendVisible(true);

        // Hiển thị labels
        pieChart.setLabelsVisible(false);  // Tắt labels mặc định
        pieChart.setLabelLineLength(10);

        // Cập nhật dữ liệu cho legend
        for (PieChart.Data data : pieChartData) {
            String percentage = String.format("%.1f%%", (data.getPieValue() / getTotalValue() * 100));
            // Format: "Role Value.0 (Percentage%)"
            String formattedName = String.format("%s %.1f (%s)",
                    data.getName(),    // Role name
                    data.getPieValue(), // Value
                    percentage         // Percentage
            );
            data.setName(formattedName);
        }

        pieChart.setStartAngle(60);
        pieChart.setClockwise(true);
    }

    private void setLineGraph() {}


    // Helper methods to create the first PieChart:
//    private int countSystemNumberOfHost() {
//        HostDAO hostDAO = new HostDAO();
//        List<Host> hosts = hostDAO.getAll();
//        if (hosts == null) return 0;
//        System.out.println("Number of Hosts: " + hosts.size());
//        return hosts.size();
//    }

    private int countSystemNumberOfOwner() {
        OwnerDAO ownerDAO = new OwnerDAO();
        List<Owner> owners = ownerDAO.getAll();
        if (owners == null) return 0;
        System.out.println("Number of Owners: " + owners.size());
        return owners.size();
    }

    private int countSystemNumberOfRenter() {
        RenterDAO renterDAO = new RenterDAO();
        List<Renter> renters = renterDAO.getAll();
        if (renters == null) return 0;
        System.out.println("Number of Renters: " + renters.size());
        return renters.size();
    }

    private int countSystemNumberOfAdmin() {
        AdminDAO adminDAO = new AdminDAO();
        List<Admin> admins = adminDAO.getAll();
        if (admins == null) return 0;
        System.out.println("Number of Admins: " + admins.size());
        return admins.size();
    }

    private double getTotalValue(){
        double total = 0;
        for (PieChart.Data data : pieChartData){
            total += data.getPieValue();
        }
        return total;
    }

    // Helper Method to get the data for the LineGraph:
    // Function to calculate the total revenue by year:

    // Create the PieChartData:
    private ObservableList<PieChart.Data> createPieChartDataPeople(){
        ObservableList<PieChart.Data> data;
//        int numberOfHost = countSystemNumberOfHost();
        int numberOfOwner = countSystemNumberOfOwner();
        int numberOfRenter = countSystemNumberOfRenter();
        int numberOfAdmin = countSystemNumberOfAdmin();

        if (numberOfOwner == 0 && numberOfRenter == 0 && numberOfAdmin == 0){
            data = FXCollections.observableArrayList(new PieChart.Data("No Data", 1));
        }
        else {
            data = FXCollections.observableArrayList(
//                    new PieChart.Data("Host", numberOfHost),
                    new PieChart.Data("Owner", numberOfOwner),
                    new PieChart.Data("Renter", numberOfRenter),
                    new PieChart.Data("Admin", numberOfAdmin)
            );
        }
        return data;
    }
}
