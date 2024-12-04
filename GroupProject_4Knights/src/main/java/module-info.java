module org.example.groupproject_4knights {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.hibernate.orm.core;
    requires java.persistence;
    requires java.naming;
    requires java.sql; // Thêm module java.sql


    opens org.example.groupproject_4knights to javafx.fxml;
    exports org.example.groupproject_4knights;
}