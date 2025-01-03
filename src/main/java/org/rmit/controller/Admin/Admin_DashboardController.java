package org.rmit.controller.Admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.rmit.database.*;
import org.rmit.model.Agreement.AgreementStatus;
import org.rmit.model.Agreement.Payment;
import org.rmit.model.Agreement.RentalAgreement;
import org.rmit.model.Persons.Admin;
import org.rmit.model.Persons.Owner;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Property.*;
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

    @FXML
    public PieChart pieChart;
    public ObservableList<PieChart.Data> pieChartData;

    @FXML
    public LineChart<String, Number> lineChart;

    @FXML
    public PieChart pieChartProperty;
    public ObservableList<PieChart.Data> pieChartDataProperty;

    @FXML
    public Label approxYearRevenue;

    LocalDate currentDate = LocalDate.now();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        welcomeLabel.setText("Welcome " + Session.getInstance().getCurrentUser().getName());
        Session.getInstance().getCurrentUser().namePropertyProperty().addListener((observable, oldValue, newValue) ->
                welcomeLabel.setText("Welcome " + newValue)
        );

        // Display the Graph:
        setPieChart();
        setLineGraph();
        setPieChartProperty();
        setEstimatedYearlyRevenue();
    }

    /* Functions to set up the Graphs */
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
            String percentage = String.format("%.1f%%", (data.getPieValue() / getTotalValuePerson() * 100));
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
    private void setLineGraph() {
        // Create the Series for the Line Graph:
        XYChart.Series<String, Number> revenueSeries = new XYChart.Series<>();
        revenueSeries.setName("Yearly Revenue");

        // Get the data for the Line Graph:
        int currentYear = LocalDate.now().getYear();
        for (int year = currentYear - 5; year < currentYear; year++){
            double revenue = calculatePastYearlyRevenue(year);
            revenueSeries.getData().add(new XYChart.Data<>(String.valueOf(year), revenue));
            System.out.println("Year: " + year + " Revenue: " + revenue);
        }

        //Clear Old Data and Add New Series:
        lineChart.getData().clear();
        lineChart.getData().add(revenueSeries);

        // Customize the Line Graph:
        lineChart.setTitle("Yearly Revenue Trend");
        lineChart.getXAxis().setLabel("Year");
        lineChart.getYAxis().setLabel("Revenue (VND");

        // Set the size of the Line Graph:
        lineChart.setMinSize(450, 300);
        lineChart.setMaxSize(450, 300);

        // Add animation:
        lineChart.setAnimated(true);

        // Set the legend to the right and visible:
        lineChart.setLegendSide(Side.BOTTOM);
        lineChart.setLegendVisible(true);
    }
    private void setPieChartProperty() {
        pieChartDataProperty = createPieChartDataProperty();
        pieChartProperty.setData(pieChartDataProperty);

        pieChartProperty.setTitle("Property Type Distribution");
        pieChartProperty.setMinSize(250, 250);
        pieChartProperty.setMaxSize(250, 250);

        // Configure the legend after setting the data
        pieChartProperty.setLegendSide(Side.BOTTOM);
        pieChartProperty.setLegendVisible(true);
        pieChartProperty.setLabelLineLength(10);

        // Hide default labels
        pieChartProperty.setLabelsVisible(false);

        for (PieChart.Data data : pieChartDataProperty) {
            String percentage = String.format("%.1f%%", (data.getPieValue() / (countCommercialProperties() + countResidentialProperties()) * 100));
            String formattedName = String.format("%s %.1f (%s)",
                    data.getName(),    // Role name
                    data.getPieValue(), // Value
                    percentage         // Percentage
            );
            data.setName(formattedName);
        }

        pieChartProperty.setStartAngle(60);
        pieChartProperty.setClockwise(true);
    }
    private void setEstimatedYearlyRevenue(){
        double revenue = calculateEstimatedYearlyRevenue();
        approxYearRevenue.setText(revenue + " VND");
    }

    /* Functions to help get and prepare the data for the graphs:*/
    // Helper methods to create the data for the first PieChart:
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
//        System.out.println("Number of Owners: " + owners.size());
        return owners.size();
    }

    private int countSystemNumberOfRenter() {
        RenterDAO renterDAO = new RenterDAO();
        List<Renter> renters = renterDAO.getAll();
        if (renters == null) return 0;
//        System.out.println("Number of Renters: " + renters.size());
        return renters.size();
    }

    private int countSystemNumberOfAdmin() {
        AdminDAO adminDAO = new AdminDAO();
        List<Admin> admins = adminDAO.getAll();
        if (admins == null) return 0;
//        System.out.println("Number of Admins: " + admins.size());
        return admins.size();
    }

    private double getTotalValuePerson(){
        double total = 0;
        for (PieChart.Data data : pieChartData){
            total += data.getPieValue();
        }
        return total;
    }

    // Function to count the total number of Commercial and Residential Properties:
    private int countCommercialProperties(){
        List<CommercialProperty> commercialProperties = new CommercialPropertyDAO().getAll();

        if (commercialProperties == null) return 0;
        System.out.println("Number of Commercial Properties: " + commercialProperties.size());
        return commercialProperties.size();
    }

    private int countResidentialProperties(){
        List<ResidentialProperty> residentialProperties = new ResidentialPropertyDAO().getAll();

        if (residentialProperties == null) return 0;
        System.out.println("Number of Residential Properties: " + residentialProperties.size());
        return residentialProperties.size();
    }

    // Function to create the data for the Person-Property PieChart:
    private ObservableList<PieChart.Data> createPieChartDataPeople(){
        ObservableList<PieChart.Data> data;
//        int numberOfHost = countSystemNumberOfHost();
        int numberOfHost = 0;
        int numberOfOwner = countSystemNumberOfOwner();
        int numberOfRenter = countSystemNumberOfRenter();
        int numberOfAdmin = countSystemNumberOfAdmin();

        if (numberOfHost == 0 && numberOfOwner == 0 && numberOfRenter == 0 && numberOfAdmin == 0){
            data = FXCollections.observableArrayList(new PieChart.Data("No Data", 1));
        }
        else {
            data = FXCollections.observableArrayList(
                    new PieChart.Data("Host", numberOfHost),
                    new PieChart.Data("Owner", numberOfOwner),
                    new PieChart.Data("Renter", numberOfRenter),
                    new PieChart.Data("Admin", numberOfAdmin)
            );
        }
        return data;
    }

    // Function to create the data for the Property PieChart:
    private ObservableList<PieChart.Data> createPieChartDataProperty(){
        ObservableList<PieChart.Data> data;
        int numberOfCommercialProperties = countCommercialProperties();
        int numberOfResidentialProperties = countResidentialProperties();

        if (numberOfCommercialProperties == 0 && numberOfResidentialProperties == 0){
            data = FXCollections.observableArrayList(new PieChart.Data("No Data", 1));
        }
        else {
            data = FXCollections.observableArrayList(
                    new PieChart.Data("Commercial Property", numberOfCommercialProperties),
                    new PieChart.Data("Residential Property", numberOfResidentialProperties)
            );
        }
        return data;
    }

    // Functions to get the data for the Line Graph [yearly revenue - except this year]:
    private double calculatePastYearlyRevenue(int year){
        double total = 0;
        List<Payment> payments = new PaymentDAO().getAll();

        if (payments == null) return 0;
        for (Payment payment : payments){
            if (payment.getDate().getYear() == year){
                total += payment.getAmount();
            }
        }
        return total;
    }

    private double calculateEstimatedYearlyRevenue() {
        double total = 0;
        List<RentalAgreement> agreements = new RentalAgreementDAO().getAll();

        if (agreements == null || agreements.isEmpty()) {
            System.out.println("No agreements found.");
            return total;
        }

        for (RentalAgreement agreement : agreements) {
            System.out.println("--------------------------------------------------");
            System.out.println("Agreement ID: " + agreement.getAgreementId());
            System.out.println("Period: " + agreement.getPeriod());
            System.out.println("Price: " + agreement.getProperty().getPrice());

            if (agreement.getContractDate().getYear() == currentDate.getYear() && agreement.getStatus().equals(AgreementStatus.ACTIVE)) {
                if (agreement.getPeriod().equals(RentalPeriod.DAILY)) {
                    if (agreement.getContractDate().getYear() < currentDate.getYear()) {
                        total += (agreement.getProperty().getPrice()) * 365;
                    } else {
                        total += (agreement.getProperty().getPrice()) * (365 - agreement.getContractDate().getDayOfYear());
                    }
                } else if (agreement.getPeriod().equals(RentalPeriod.WEEKLY)) {
                    if (agreement.getContractDate().getYear() < currentDate.getYear()) {
                        total += (agreement.getProperty().getPrice()) * 52;
                    } else {
                        total += (agreement.getProperty().getPrice()) * (52 - agreement.getContractDate().getDayOfYear() / 7);
                    }
                } else if (agreement.getPeriod().equals(RentalPeriod.FORTNIGHTLY)) {
                    if (agreement.getContractDate().getYear() < currentDate.getYear()) {
                        total += (agreement.getProperty().getPrice()) * 26;
                    } else {
                        total += (agreement.getProperty().getPrice()) * (26 - agreement.getContractDate().getDayOfYear() / 14);
                    }
                } else if (agreement.getPeriod().equals(RentalPeriod.MONTHLY)) {
                    if (agreement.getContractDate().getYear() < currentDate.getYear()) {
                        total += (agreement.getProperty().getPrice()) * 12;
                    } else {
                        total += (agreement.getProperty().getPrice()) * (12 - agreement.getContractDate().getMonthValue());
                    }
                }
            }
            System.out.println("Total: " + total);
        }

        return total;
    }
}
