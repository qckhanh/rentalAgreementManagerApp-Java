package org.rmit.controller.Admin;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
import org.rmit.database.*;
import org.rmit.model.Agreement.AgreementStatus;
import org.rmit.model.Agreement.Payment;
import org.rmit.model.Agreement.RentalAgreement;
import org.rmit.model.ModelCentral;
import org.rmit.model.Persons.Admin;
import org.rmit.model.Persons.Host;
import org.rmit.model.Persons.Owner;
import org.rmit.model.Persons.Renter;
import org.rmit.model.Property.*;
import org.rmit.model.Session;

import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class Admin_DashboardController implements Initializable {
    public TextField search_input;
    public Button search_button;
    public Label welcomeLabel;
    public Label totalAgreement_label;
    public Label totalPayments_label;
    public ListView upcommingPayment_listView;

    @FXML
    public PieChart pieChartPersonObject;
    public ObservableList<PieChart.Data> pieChartData;

    @FXML
    public LineChart<String, Number> lineChart;

    @FXML
    public PieChart pieChartPropertyObject;
    public ObservableList<PieChart.Data> pieChartDataProperty;

    @FXML
    public Label approxYearRevenue;

    @FXML
    public Button refreshAdminDashBoard;

    LocalDate currentDate = LocalDate.now();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        welcomeLabel.setText("Welcome " + Session.getInstance().getCurrentUser().getName());
        Session.getInstance().getCurrentUser().namePropertyProperty().addListener((observable, oldValue, newValue) ->
                welcomeLabel.setText("Welcome " + newValue)
        );

        // Display the Graph:
        setPieChartPersonObject();
        setLineGraph();
        setPieChartPropertyObject();
        setEstimatedYearlyRevenue();

        // Add event handler to the refresh button
        refreshAdminDashBoard.setOnAction(event -> updateData());

        // Add hover effect to the refresh button
        refreshAdminDashBoard.setOnMouseEntered(event -> {
            refreshAdminDashBoard.setStyle("-fx-background-color: #ffcc00; -fx-text-fill: #000000; -fx-cursor: hand;");
        });

        refreshAdminDashBoard.setOnMouseExited(event -> {
            refreshAdminDashBoard.setStyle(""); // Reset to default style
        });
    }


    /* Functions to set up the Graphs */
    private void setPieChartPersonObject() {
        pieChartData = createPieChartDataPeople();
        pieChartPersonObject.setData(pieChartData);

        pieChartPersonObject.setTitle("Account's Role Distribution");
        pieChartPersonObject.setMinSize(300,300);
        pieChartPersonObject.setMaxSize(400,400);

        // Đặt legend bên phải và hiện
        pieChartPersonObject.setLegendSide(Side.BOTTOM);
        pieChartPersonObject.setLegendVisible(true);

        // Hiển thị labels
        pieChartPersonObject.setLabelsVisible(false);
        pieChartPersonObject.setLabelLineLength(10);

        pieChartPersonObject.setStartAngle(60);
        pieChartPersonObject.setClockwise(true);

        applyPieChartHoverEffects();
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
        lineChart.getYAxis().setLabel("Revenue (VND)");

        // Set the size of the Line Graph:
        lineChart.setMinSize(450, 300);
        lineChart.setMaxSize(450, 300);

        // Add animation:
        lineChart.setAnimated(true);

        // Set the legend to the right and visible:
        lineChart.setLegendSide(Side.BOTTOM);
        lineChart.setLegendVisible(true);

        // Map để lưu trữ tooltip cho mỗi điểm dữ liệu
        Map<Node, Tooltip> tooltipMap = new HashMap<>();

        // Thêm hiệu ứng hover cho series
        Node seriesLine = revenueSeries.getNode();
        seriesLine.setOnMouseEntered(event -> {
            seriesLine.setStyle("-fx-stroke-width: 4px;"); // Làm dày đường line khi hover
            seriesLine.setEffect(new Glow(0.8)); // Thêm hiệu ứng phát sáng
        });

        seriesLine.setOnMouseExited(event -> {
            seriesLine.setStyle("-fx-stroke-width: 2px;"); // Trở về độ dày bình thường
            seriesLine.setEffect(null); // Xóa hiệu ứng
        });

        // Thêm tooltip và hiệu ứng cho từng điểm dữ liệu
        for (XYChart.Data<String, Number> data : revenueSeries.getData()) {
            // Tạo tooltip cho mỗi điểm
            Tooltip tooltip = new Tooltip(
                    String.format("Year: %s\nRevenue: %s VND",
                            data.getXValue(),
                            formatCurrency(data.getYValue().doubleValue())
                    )
            );

            // Đợi cho node được tạo ra
            Platform.runLater(() -> {
                Node dataNode = data.getNode();
                if (dataNode != null) {
                    tooltipMap.put(dataNode, tooltip);

                    // Thêm hiệu ứng hover cho điểm dữ liệu
                    dataNode.setOnMouseEntered(event -> {
                        // Phóng to điểm
                        dataNode.setScaleX(1.5);
                        dataNode.setScaleY(1.5);

                        // Thêm hiệu ứng phát sáng
                        dataNode.setEffect(new Glow(0.8));

                        // Hiển thị tooltip
                        Tooltip.install(dataNode, tooltipMap.get(dataNode));
                    });

                    dataNode.setOnMouseExited(event -> {
                        // Trở về kích thước bình thường
                        dataNode.setScaleX(1);
                        dataNode.setScaleY(1);

                        // Xóa hiệu ứng
                        dataNode.setEffect(null);

                        // Ẩn tooltip
                        Tooltip.uninstall(dataNode, tooltipMap.get(dataNode));
                    });
                }
            });
        }

        // Tùy chọn: Thêm style cho series
        revenueSeries.getNode().setStyle(
                "-fx-stroke: #2196f3; " + // Màu của line
                        "-fx-stroke-width: 2px;"   // Độ dày mặc định của line
        );
    }

    private void setPieChartPropertyObject() {
        pieChartDataProperty = createPieChartDataProperty();
        pieChartPropertyObject.setData(pieChartDataProperty);

        pieChartPropertyObject.setTitle("Property Type Distribution");
        pieChartPropertyObject.setMinSize(250, 250);
        pieChartPropertyObject.setMaxSize(250, 250);

        pieChartPropertyObject.setLegendSide(Side.BOTTOM);
        pieChartPropertyObject.setLegendVisible(true);
        pieChartPropertyObject.setLabelLineLength(10);
        pieChartPropertyObject.setLabelsVisible(false);

        pieChartPropertyObject.setStartAngle(60);
        pieChartPropertyObject.setClockwise(true);

        applyPieChartHoverEffects();
    }

    private void setEstimatedYearlyRevenue(){
        double revenue = calculateEstimatedYearlyRevenue();
        approxYearRevenue.setText(formatCurrency(revenue) + " VND");
    }

    private void updateData() {
        // Update PieChart data for Person Object
        pieChartData = createPieChartDataPeople();
        pieChartPersonObject.setData(pieChartData);
        applyPieChartHoverEffects();

        // Update LineChart data
        setLineGraph();

        // Update PieChart data for Property Object
        pieChartDataProperty = createPieChartDataProperty();
        pieChartPropertyObject.setData(pieChartDataProperty);
        applyPieChartHoverEffects();

        // Update Estimated Yearly Revenue
        setEstimatedYearlyRevenue();
    }

    /* Helpers method for the Person Objects Pie Chart */
    private int countSystemNumberOfOwner() {
//        OwnerDAO ownerDAO = new OwnerDAO();
        List<Owner> owners = ModelCentral.getInstance().getAdminViewFactory().getAllOwner();
        if (owners == null) return 0;
//        System.out.println("Number of Owners: " + owners.size());
        return owners.size();
    }

    private int countSystemNumberOfRenter() {
//        RenterDAO renterDAO = new RenterDAO();
        List<Renter> renters = ModelCentral.getInstance().getAdminViewFactory().getAllRenter();
        if (renters == null) return 0;
//        System.out.println("Number of Renters: " + renters.size());
        return renters.size();
    }

    private int countSystemNumberOfHost() {
        List<Host> host = ModelCentral.getInstance().getAdminViewFactory().getAllHost();
        if (host == null) return 0;
        return host.size();
    }

    private int countSystemNumberOfAdmin() {
        AdminDAO adminDAO = new AdminDAO();
        List<Admin> admins = ModelCentral.getInstance().getAdminViewFactory().getAllAdmin();
        if (admins == null) return 0;
//        System.out.println("Number of Admins: " + admins.size());
        return admins.size();
    }

    private ObservableList<PieChart.Data> createPieChartDataPeople(){
        ObservableList<PieChart.Data> data;
        int numberOfHost = countSystemNumberOfHost();
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

    private double getTotalValuePerson(){
        double total = 0;
        for (PieChart.Data data : pieChartData){
            total += data.getPieValue();
        }
        return total;
    }

    /* Helpers method for the Property Objects Pie Chart */
    private int countCommercialProperties(){
        List<CommercialProperty> commercialProperties = ModelCentral.getInstance().getAdminViewFactory().getAllCommercialProperty();

        if (commercialProperties == null) return 0;
        System.out.println("Number of Commercial Properties: " + commercialProperties.size());
        return commercialProperties.size();
    }

    private int countResidentialProperties(){
        List<ResidentialProperty> residentialProperties = ModelCentral.getInstance().getAdminViewFactory().getAllResidentialProperty();

        if (residentialProperties == null) return 0;
        System.out.println("Number of Residential Properties: " + residentialProperties.size());
        return residentialProperties.size();
    }

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

    /* Helpers method for the Line Graph */
    private double calculatePastYearlyRevenue(int year){
        double total = 0;
        List<Payment> payments = ModelCentral.getInstance().getAdminViewFactory().getAllPayment();

        if (payments == null) return 0;
        for (Payment payment : payments){
            if (payment.getDate().getYear() == year){
                total += payment.getAmount();
            }
        }
        return total;
    }

    /* Helpers method for the Estimated Yearly Revenue */
    private double calculateEstimatedYearlyRevenue() {
        List<RentalAgreement> agreements = ModelCentral.getInstance().getAdminViewFactory().getAllRentalAgreement();

        double total = 0;
        if (agreements.isEmpty()) {return 0;}
        else {
            for (RentalAgreement agreement : agreements) {
                if (agreement == null || agreement.getStatus() == null) {
                    total += 0;
                }
                else {
                    if (agreement.getStatus().equals(AgreementStatus.ACTIVE) || agreement.getStatus().equals(AgreementStatus.NEW)) {
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
                                total += (agreement.getProperty().getPrice()) * (52 - (double) agreement.getContractDate().getDayOfYear() / 7);
                            }
                        } else if (agreement.getPeriod().equals(RentalPeriod.FORTNIGHTLY)) {
                            if (agreement.getContractDate().getYear() < currentDate.getYear()) {
                                total += (agreement.getProperty().getPrice()) * 26;
                            } else {
                                total += (agreement.getProperty().getPrice()) * (26 - (double) agreement.getContractDate().getDayOfYear() / 14);
                            }
                        } else if (agreement.getPeriod().equals(RentalPeriod.MONTHLY)) {
                            if (agreement.getContractDate().getYear() < currentDate.getYear()) {
                                total += (agreement.getProperty().getPrice()) * 12;
                            } else {
                                total += (agreement.getProperty().getPrice()) * (12 - agreement.getContractDate().getMonthValue());
                            }
                        }
                    }
                }
            }
        }
        return total;
    }

    /* Other UI Helper Methods */
    private String formatCurrency(double amount) {
        if (amount == 0) return "0";

        // Process Negative Values
        boolean isNegative = amount < 0;
        amount = Math.abs(amount);


        double trillion = 1_000_000_000_000.0;
        double billion = 1_000_000_000.0;
        double million = 1_000_000.0;
        double thousand = 1_000.0;

        String result;
        if (amount >= trillion) {
            result = String.format("%.1fT", amount / trillion);
        } else if (amount >= billion) {
            result = String.format("%.1fB", amount / billion);
        } else if (amount >= million) {
            result = String.format("%.1fM", amount / million);
        } else if (amount >= thousand) {
            result = String.format("%.1fK", amount / thousand);
        } else {
            result = String.format("%.1f", amount);
        }

        result = result.endsWith(".0") ? result.substring(0, result.length() - 2) : result;

        return isNegative ? "-" + result : result;
    }

    private void applyPieChartHoverEffects() {
        Map<Node, Tooltip> tooltipMap = new HashMap<>();

        for (PieChart.Data data : pieChartData) {
            String percentage = String.format("%.1f%%", (data.getPieValue() / getTotalValuePerson() * 100));
            String formattedName = String.format("%s %.1f (%s)",
                    data.getName(),
                    data.getPieValue(),
                    percentage
            );
            data.setName(formattedName);

            Tooltip tooltip = new Tooltip(
                    String.format("%s\nNumber: %.1f\nPercentage: %.1f%%",
                            data.getName().split(" ")[0],
                            data.getPieValue(),
                            (data.getPieValue() / getTotalValuePerson() * 100)
                    )
            );
            tooltipMap.put(data.getNode(), tooltip);

            data.getNode().setOnMouseEntered(event -> {
                data.getNode().setScaleX(1.1);
                data.getNode().setScaleY(1.1);
                data.getNode().setEffect(new Glow(0.5));
                Tooltip.install(data.getNode(), tooltipMap.get(data.getNode()));
            });

            data.getNode().setOnMouseExited(event -> {
                data.getNode().setScaleX(1);
                data.getNode().setScaleY(1);
                data.getNode().setEffect(null);
                Tooltip.uninstall(data.getNode(), tooltipMap.get(data.getNode()));
            });
        }
    }

}