module org.rmit.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires java.sql;
    requires java.xml;
    requires org.slf4j;
    requires com.fasterxml.classmate;
    requires java.transaction.xa;
    requires org.postgresql.jdbc;  // Add this

    // Open all packages that contain entities to Hibernate
    opens org.rmit.demo to javafx.fxml, org.hibernate.orm.core;
    opens org.rmit.model to org.hibernate.orm.core;
    exports org.rmit to javafx.graphics;

}
