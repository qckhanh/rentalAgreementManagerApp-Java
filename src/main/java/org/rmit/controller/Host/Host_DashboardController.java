package org.rmit.controller.Host;

import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
import javafx.scene.layout.AnchorPane;
import org.rmit.Helper.UIDecorator;
import org.rmit.view.ViewCentral;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Person;
import org.rmit.model.Property.*;
import org.rmit.model.Session;
import org.rmit.view.Start.NOTIFICATION_TYPE;

import java.net.URL;
import java.util.*;

public class Host_DashboardController implements Initializable {
    public TextField search_input;
    public Button search_button;
    public Label welcomeLabel;
//    public Label totalAgreement_label;
//    public Label totalPayments_label;
//    public ListView upcommingPayment_listView;
    public ObjectProperty<Person> currentUser = Session.getInstance().currentUserProperty();

    @FXML
    public PieChart piechart;
    public ObservableList<PieChart.Data> pieChartData;
    public AnchorPane anchorPane;
    Set<Property> manageProperties = ((Host)currentUser.get()).getPropertiesManaged();
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    public BarChart<String, Number> barChart;
    @FXML
    public Button refreshHostDashBoard;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        welcomeLabel.setText("Welcome " + Session.getInstance().getCurrentUser().getName());
        Session.getInstance().getCurrentUser().namePropertyProperty().addListener((observable, oldValue, newValue) ->
                welcomeLabel.setText("Welcome " + newValue)
        );

        setPieChart();
        setBarChart();

        // Update the data button event
        refreshHostDashBoard.setOnAction(event -> {
            updateData();
        });

        // Add hover effect to the refresh button
        refreshHostDashBoard.setOnMouseEntered(event -> {
            refreshHostDashBoard.setStyle("-fx-background-color: #ffcc00; -fx-text-fill: #000000; -fx-cursor: hand;");
        });
        refreshHostDashBoard.setOnMouseExited(event -> {
            refreshHostDashBoard.setStyle(""); // Reset to default style
        });
        decor();
    }

    private void decor(){
        UIDecorator.setNormalButton(refreshHostDashBoard, UIDecorator.REFRESH(), null);
    }

    private void updateData() {
        // Update PieChart data
        pieChartData = createPieChartData();
        piechart.setData(pieChartData);
        applyPieChartHoverEffects();

        // Update BarChart data
        barChart.getData().clear();
        setBarChart();
        ViewCentral.getInstance().getStartViewFactory().pushNotification(NOTIFICATION_TYPE.SUCCESS, anchorPane, "Data has been updated successfully!");
    }

    /* Main function to set the graphs  on the dashboard */
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

        // Add hover effect to the pie chart:
        applyPieChartHoverEffects();

        // Thêm animation khi hiển thị
        piechart.setAnimated(true);
        piechart.setStartAngle(90);
        piechart.setClockwise(true);
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

        // Map để lưu trữ tooltip cho mỗi bar
        Map<Node, Tooltip> tooltipMap = new HashMap<>();

        // Add Hover effect and tooltip to the bars:
        for (XYChart.Series<String, Number> series : barChart.getData()) {
            for (XYChart.Data<String, Number> data : series.getData()) {
                // Create the tooltip for each bar
                Tooltip tooltip = new Tooltip(
                        String.format("%s\nProperty Type: %s\nNumber of Properties: %d",
                                series.getName(),
                                data.getXValue(),
                                data.getYValue().intValue()
                        )
                );

                tooltipMap.put(data.getNode(), tooltip);

                data.getNode().setOnMouseEntered(event -> {
                    // Scaling effect when hover
                    data.getNode().setScaleX(1.1);
                    data.getNode().setScaleY(1.1);

                    // Add Flow Effect
                    data.getNode().setEffect(new Glow(0.5));

                    // Display the Tooltip
                    Tooltip.install(data.getNode(), tooltipMap.get(data.getNode()));
                });

                data.getNode().setOnMouseExited(event -> {
                    // Return to normal size when not hover
                    data.getNode().setScaleX(1);
                    data.getNode().setScaleY(1);

                    // Remove Glow Effect
                    data.getNode().setEffect(null);

                    // Remove Tooltip
                    Tooltip.uninstall(data.getNode(), tooltipMap.get(data.getNode()));
                });
            }
        }
    }

    /* Helpers method to create the Bar Chart */
    // Functions to get and calculate the BarChart's Data:
    private int countRentedResidentialProperties() {
        int numberOfRentedResidentialProperties = 0;

        // Count the number of Rented Residential Properties managed by this host:
        Set<Property> manageProperty = ((Host)currentUser.get()).getPropertiesManagedProperty();
        for (Property property : manageProperty) {
            if (property instanceof ResidentialProperty && property.getStatusProperty().equals(PropertyStatus.RENTED)) {
                numberOfRentedResidentialProperties++;
                System.out.println(property.getId());
            }
        }

//        System.out.println("Rented R: " + numberOfRentedResidentialProperties);
        return numberOfRentedResidentialProperties;
    }

    private int countAvailableResidentialProperties() {
        int numberOfAvailableResidentialProperties = 0;

        // Count the number of Available Residential Properties managed by this host:
        Set<Property> manageProperty = ((Host)currentUser.get()).getPropertiesManagedProperty();
        for (Property property : manageProperty) {
            if (property instanceof ResidentialProperty && property.getStatusProperty().equals(PropertyStatus.AVAILABLE)) {
                numberOfAvailableResidentialProperties++;
                System.out.println(property.getId());
            }
        }

//        System.out.println("Available R: " + numberOfAvailableResidentialProperties);
        return numberOfAvailableResidentialProperties;
    }

    private int countRentedCommercialProperties() {
        int numberOfRentedCommercialProperties = 0;

        // Count the number of Rented Commercial Properties managed by this host:
        Set<Property> manageProperty = ((Host)currentUser.get()).getPropertiesManagedProperty();
        for (Property property : manageProperty) {
            if (property instanceof CommercialProperty && property.getStatusProperty().equals(PropertyStatus.RENTED)) {
                numberOfRentedCommercialProperties++;
                System.out.println(property.getId());
            }
        }

//        System.out.println("Rented C: " + numberOfRentedCommercialProperties);

        return numberOfRentedCommercialProperties;
    }

    private int countAvailableCommercialProperties() {
        int numberOfAvailableCommercialProperties = 0;

        // Count the number of Available Commercial Properties managed by this host:
        Set<Property> manageProperty = ((Host)currentUser.get()).getPropertiesManagedProperty();
        for (Property property : manageProperty) {
            if (property instanceof CommercialProperty && property.getStatusProperty().equals(PropertyStatus.AVAILABLE)) {
                numberOfAvailableCommercialProperties++;
                System.out.println(property.getId());
            }
        }

//        System.out.println("Available C: " + numberOfAvailableCommercialProperties);

        return numberOfAvailableCommercialProperties;
    }
    
    /* Helpers method to create the Pie Chart */
    // Functions to get and calculate the PieChart's Data:
    private int countResidentialProperties() {
        // Write Code:
        int numberOfResidentialProperties = 0;

        // Count the number of Residential Properties managed by this host:
        Set<Property> manageProperty = ((Host)currentUser.get()).getPropertiesManaged();
        for (Property property : manageProperty) {
            if (property.getType().equals(PropertyType.RESIDENTIAL)) {
                numberOfResidentialProperties++;
            }
        }

//        System.out.println("R: " + numberOfResidentialProperties);

        return numberOfResidentialProperties;
    }

    private int countCommercialProperties() {
        // Write Code:
        int numberOfCommercialProperties = 0;

        // Count the number of Commercial Properties managed by this host:
        for (Property property : manageProperties) {
            if (property.getType().equals(PropertyType.COMMERCIAL)) {
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

    // UI Hover Effects for the Pie Chart:
    private void applyPieChartHoverEffects() {
        // Map to save the tooltip for each data section:
        Map<Node, Tooltip> tooltipMap = new HashMap<>();

        for (PieChart.Data data : pieChartData) {
            String percentage = String.format("%.1f%%", (data.getPieValue() / getTotalValue() * 100));
            String formattedName = String.format("%s %.1f (%s)",
                    data.getName(),
                    data.getPieValue(),
                    percentage
            );

            data.setName(formattedName);

            // Create tooltip
            Tooltip tooltip = new Tooltip(
                    String.format("%s\nValue: %.1f\nPercentage: %.1f%%",
                            data.getName().split(" ")[0],
                            data.getPieValue(),
                            (data.getPieValue() / getTotalValue() * 100)
                    )
            );
            tooltipMap.put(data.getNode(), tooltip);

            // Add Hover Effect to each data section:
            data.getNode().setOnMouseEntered(event -> {
                // Create zoom effect when hover
                data.getNode().setScaleX(1.1);
                data.getNode().setScaleY(1.1);

                // Add glow effect
                data.getNode().setEffect(new Glow(0.5));

                // Display tooltip
                Tooltip.install(data.getNode(), tooltipMap.get(data.getNode()));
            });

            data.getNode().setOnMouseExited(event -> {
                // Return to normal size when not hover
                data.getNode().setScaleX(1);
                data.getNode().setScaleY(1);

                // Remove glow effect
                data.getNode().setEffect(null);

                // Remove tooltip
                Tooltip.uninstall(data.getNode(), tooltipMap.get(data.getNode()));
            });
        }
    }
}