module org.rmit.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.rmit.demo to javafx.fxml;
    exports org.rmit.demo;
}