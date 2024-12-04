module org.example._4knights {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example._4knights to javafx.fxml;
    exports org.example._4knights;
}