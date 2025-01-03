package org.rmit.controller.Host;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
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

    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;

    @FXML
    public BarChart<String, Number> barChart;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        welcomeLabel.setText("Welcome " + Session.getInstance().getCurrentUser().getName());
        Session.getInstance().getCurrentUser().namePropertyProperty().addListener((observable, oldValue, newValue) ->
                welcomeLabel.setText("Welcome " + newValue)
        );

        // Print Out the Piechart:
        setPieChart();
        setBarChart();

        // for debugging purposes
//        int a = countRentedResidentialProperties();
//        int b = countAvailableResidentialProperties();
//        int c = countRentedCommercialProperties();
//        int d = countAvailableCommercialProperties();
    }

    // function to set the bar chart
    private void setPieChart() {
        pieChartData = createPieChartData();

        // Set the chart size
        piechart.setMinSize(300, 300);
        piechart.setMaxSize(400, 400);

        // Set the chart data
        piechart.setData(pieChartData);
        piechart.setTitle("Property Type Distribution");

        // Set the legend to the bottom
        piechart.setLegendSide(Side.BOTTOM);
        piechart.setLegendVisible(true);

        piechart.setLabelsVisible(false);

        pieChartData.forEach(data -> {
            data.getNode().setOnMouseDragEntered(event -> {
                piechart.setEffect(new Glow(0.5));
            });
        });

        for (PieChart.Data data : pieChartData){
            String percentage = String.format("%.1f%%", (data.getPieValue() / getTotalValue() * 100));
            String formattedName = String.format("%s %.1f (%s)",
                    data.getName(),
                    data.getPieValue(),
                    percentage
            );

            data.setName(formattedName);
        }
    }

    private void setBarChart(){
        // Set the Axis Labels:
        xAxis.setLabel("Property Type");
        yAxis.setLabel("Number of Properties");

        // Set the Chart's title:
        barChart.setTitle("Rented vs Available Properties");

        // Create the series for Rented Properties:
        XYChart.Series<String, Number> rentedSeries = new XYChart.Series<>();
        rentedSeries.setName("Rented Properties");
        rentedSeries.getData().add(new XYChart.Data<>("Residential", countRentedResidentialProperties()));
        rentedSeries.getData().add(new XYChart.Data<>("Commercial", countRentedCommercialProperties()));

        // Create the series for Available Properties:
        XYChart.Series<String, Number> availableSeries = new XYChart.Series<>();
        availableSeries.setName("Available Properties");
        availableSeries.getData().add(new XYChart.Data<>("Residential", countAvailableResidentialProperties()));
        availableSeries.getData().add(new XYChart.Data<>("Commercial", countAvailableCommercialProperties()));

        // Add the series to the Bar Chart:
        barChart.getData().addAll(rentedSeries, availableSeries);

        // Set the Bar Chart size:
        barChart.setMinSize(300, 300);
        barChart.setMaxSize(400, 400);

        // Customize the x-axis:
        xAxis.setTickLabelRotation(0);

        // Remove grid lines for cleaner looks:
        barChart.setHorizontalGridLinesVisible(false);
        barChart.setVerticalGridLinesVisible(false);

        // Add Hover effect to the bars:
        for (XYChart.Series<String, Number> series : barChart.getData()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                data.getNode().setOnMouseEntered(event -> {
                    data.getNode().setEffect(new Glow(0.5));
                });
                data.getNode().setOnMouseExited(event -> {
                    data.getNode().setEffect(null);
                });
            }
        }
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
        double totalValue = 0;
        for (PieChart.Data data : pieChartData) {
            totalValue += data.getPieValue();
        }
        return totalValue;
    }
}

