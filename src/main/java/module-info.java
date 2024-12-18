module org.rmit.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires java.sql;
    requires org.slf4j;
    requires com.fasterxml.classmate;
    requires java.transaction.xa;
    requires org.postgresql.jdbc;
    requires java.desktop;  // Add this

    // Open all packages that contain entities to Hibernate
//    opens org.rmit.demo to javafx.fxml, org.hibernate.orm.core;
//    opens org.rmit.demo.FXMLs to javafx.fxml;
//
//    opens org.rmit.model to org.hibernate.orm.core;
//    exports org.rmit to javafx.graphics;
//    opens org.rmit.model.Persons to org.hibernate.orm.core;
//    opens org.rmit.model.Property to org.hibernate.orm.core;
//    opens org.rmit.model.Agreement to org.hibernate.orm.core;


    opens org.rmit to org.hibernate.orm.core, javafx.fxml;
    opens org.rmit.model.Persons to org.hibernate.orm.core, javafx.fxml;
    opens org.rmit.model.Property to org.hibernate.orm.core, javafx.fxml;
    opens org.rmit.model.Agreement to org.hibernate.orm.core, javafx.fxml;
    opens org.rmit.controller to javafx.fxml;
    opens org.rmit.controller.Renter to javafx.fxml;

    exports org.rmit;
    exports org.rmit.model;
    exports org.rmit.model.Persons;
    exports org.rmit.controller;
    exports org.rmit.controller.Renter;
    exports org.rmit.model.Property;
    exports org.rmit.model.Agreement;
    exports org.rmit.view;


}
