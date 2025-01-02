package org.rmit.controller.Host;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import org.rmit.model.Agreement.AgreementStatus;
import org.rmit.model.Agreement.RentalAgreement;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Person;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Property.CommercialProperty;
import org.rmit.model.Property.Property;
import org.rmit.model.Property.PropertyStatus;
import org.rmit.model.Property.ResidentialProperty;
import org.rmit.model.Session;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.IntStream;

public class Host_DashboardController implements Initializable {
    public TextField search_input;
    public Button search_button;
    public Label welcomeLabel;
//    public Label totalAgreement_label;
//    public Label totalPayments_label;
//    public ListView upcommingPayment_listView;
    public ObjectProperty<Person> currentUser = Session.getInstance().currentUserProperty();
    public ObservableList<PieChart.Data> pieChartData;
    public PieChart piechart;

    NumberAxis xAxis = new NumberAxis();
    NumberAxis yAxis = new NumberAxis();
    public BarChart barChart;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        welcomeLabel.setText("Welcome " + Session.getInstance().getCurrentUser().getName());
        Session.getInstance().getCurrentUser().namePropertyProperty().addListener((observable, oldValue, newValue) ->
                welcomeLabel.setText("Welcome " + newValue)
        );

        // Print Out the Piechart:
        setPiechart();
//        setBarChart();
        int a = countRentedResidentialProperties();
        int b = countAvailableResidentialProperties();
        int c = countRentedCommercialProperties();
        int d = countAvailableCommercialProperties();
    }

    // function to set the bar chart
    private void setPiechart() {
        pieChartData = createPieChartData();

        // Set the chart size
        piechart.setMinSize(300, 300);
        piechart.setMaxSize(400, 400);

        // Set the chart data
        piechart.setData(pieChartData);
        piechart.setTitle("Property Type Distribution");

        // Set the chart properties [LEGEND]
        piechart.setLegendSide(Side.RIGHT);
        piechart.setLegendVisible(true);

        // Set the chart properties [LABELS]
        piechart.setLabelsVisible(true);
        piechart.setLabelLineLength(10);

        // Set the chart properties [ANGLE]
        piechart.setStartAngle(60);
        piechart.setClockwise(true);

        // Set the chart properties [TOOLTIP]
        for (PieChart.Data data : pieChartData) {
            String percentage = String.format("%.1f%%", (data.getPieValue() / getTotalValue() * 100));
            data.setName(data.getName() + "\n" + percentage);

            Tooltip tooltip = new Tooltip(
                    String.format("%s\nValue: %.0f", data.getName(), data.getPieValue())
            );
            Tooltip.install(data.getNode(), tooltip);
        }
    }
    private void setBarChart(){
        // Set the Axis Labels:
        xAxis.setLabel("Type of Property");
        yAxis.setLabel("Number of Properties");

        // Create the Bar Chart:
//        barChart = new BarChart(xAxis, yAxis);

        // Set the Chart Title:
        barChart.setTitle("Number of Rented Properties vs Available Properties");
    }


    /* Helpers method to create the Bar Chart */
    // Calculate Bar Chart Data:
    private int countRentedResidentialProperties() {
        // Write Code:
        int numberOfRentedResidentialProperties = 0;

        System.out.println(">> Rented Residential Properties: ");
        // Count the number of Rented Residential Properties managed by this host:
        Set<Property> manageProperty = ((Host)currentUser.get()).getPropertiesManagedProperty();
        for (Property property : manageProperty) {
            if (property instanceof ResidentialProperty && property.getStatusProperty().equals(PropertyStatus.RENTED)) {
                numberOfRentedResidentialProperties++;
                System.out.println(property.getId());
            }
        }

        System.out.println("Rented R: " + numberOfRentedResidentialProperties);

        return numberOfRentedResidentialProperties;
    }

    private int countAvailableResidentialProperties() {
        int numberOfAvailableResidentialProperties = 0;

        System.out.println(">> Available Residential Properties: ");
        // Count the number of Available Residential Properties managed by this host:
        Set<Property> manageProperty = ((Host)currentUser.get()).getPropertiesManagedProperty();
        for (Property property : manageProperty) {
            if (property instanceof ResidentialProperty && property.getStatusProperty().equals(PropertyStatus.AVAILABLE)) {
                numberOfAvailableResidentialProperties++;
                System.out.println(property.getId());
            }
        }

        System.out.println("Available R: " + numberOfAvailableResidentialProperties);
        return numberOfAvailableResidentialProperties;
    }

    private int countRentedCommercialProperties() {
        int numberOfRentedCommercialProperties = 0;

        System.out.println(">> Rented Commercial Properties: ");
        // Count the number of Rented Commercial Properties managed by this host:
        Set<Property> manageProperty = ((Host)currentUser.get()).getPropertiesManagedProperty();
        for (Property property : manageProperty) {
            if (property instanceof CommercialProperty && property.getStatusProperty().equals(PropertyStatus.RENTED)) {
                numberOfRentedCommercialProperties++;
                System.out.println(property.getId());
            }
        }

        System.out.println("Rented C: " + numberOfRentedCommercialProperties);

        return numberOfRentedCommercialProperties;
    }

    private int countAvailableCommercialProperties() {
        int numberOfAvailableCommercialProperties = 0;

        System.out.println(">> Available Commercial Properties: ");
        // Count the number of Available Commercial Properties managed by this host:
        Set<Property> manageProperty = ((Host)currentUser.get()).getPropertiesManagedProperty();
        for (Property property : manageProperty) {
            if (property instanceof CommercialProperty && property.getStatusProperty().equals(PropertyStatus.AVAILABLE)) {
                numberOfAvailableCommercialProperties++;
                System.out.println(property.getId());
            }
        }

        System.out.println("Available C: " + numberOfAvailableCommercialProperties);

        return numberOfAvailableCommercialProperties;
    }

    /* Helpers method to create the Pie Chart */
    // Count the number of Residential Properties managed by this host:
    private int countResidentialProperties() {
        // Write Code:
        int numberOfResidentialProperties = 0;

        // Count the number of Residential Properties managed by this host:
        Set<Property> manageProperty = ((Host)currentUser.get()).getPropertiesManaged();
        for (Property property : manageProperty) {
            if (property instanceof ResidentialProperty) {
                numberOfResidentialProperties++;
            }
        }

//        System.out.println("R: " + numberOfResidentialProperties);

        return numberOfResidentialProperties;
    }

    // Count the number of Commercial Properties managed by this host:
    private int countCommercialProperties() {
        // Write Code:
        int numberOfCommercialProperties = 0;

        // Count the number of Commercial Properties managed by this host:
        Set<Property> manageProperty = ((Host)currentUser.get()).getPropertiesManaged();
        for (Property property : manageProperty) {
            if (!(property instanceof ResidentialProperty)) {
                numberOfCommercialProperties++;
            }
        }

//        System.out.println("C: " + numberOfCommercialProperties);
        return numberOfCommercialProperties;
    }

    // Create Pie Chart Data:
    private ObservableList<PieChart.Data> createPieChartData() {
        // Write Code:
        ObservableList<PieChart.Data> data;
        int numberOfResidentialProperties = countResidentialProperties();
        int numberOfCommercialProperties = countCommercialProperties();

        if (numberOfResidentialProperties == 0 && numberOfCommercialProperties == 0) {
            data = FXCollections.observableArrayList(
                    new PieChart.Data("No Data", 1)
            );
        }
        else {
            data = FXCollections.observableArrayList(
                    new PieChart.Data("Residential Properties", numberOfResidentialProperties),
                    new PieChart.Data("Commercial Properties", numberOfCommercialProperties)
            );
        }
        return data;
    }

    // Get the total value of the Pie Chart:
    private double getTotalValue() {
        // Write Code:
        double totalValue = 0;
        for (PieChart.Data data : pieChartData) {
            totalValue += data.getPieValue();
        }
        return totalValue;
    }
}

