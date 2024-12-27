module org.rmit.demo {
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires java.sql;
    requires org.slf4j;
    requires com.fasterxml.classmate;
    requires java.transaction.xa;
    requires org.postgresql.jdbc;
    requires javafx.controls;
//    requires com.pixelduke.fxskins;
//    requires atlantafx.base;


    //model
    opens org.rmit to org.hibernate.orm.core, javafx.fxml;
    opens org.rmit.model.Persons to org.hibernate.orm.core, javafx.fxml;
    opens org.rmit.model.Property to org.hibernate.orm.core, javafx.fxml;
    opens org.rmit.model.Agreement to org.hibernate.orm.core, javafx.fxml;

    //controller
    opens org.rmit.controller.Renter to javafx.fxml;
    opens org.rmit.controller.Start to javafx.fxml;
    opens org.rmit.controller.Owner to javafx.fxml;
    opens org.rmit.controller.Host to javafx.fxml;

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    exports org.rmit;
    exports org.rmit.model;
    //    exports org.rmit.view;
    //    exports org.rmit.controller;


    //model
    exports org.rmit.model.Persons;
    exports org.rmit.model.Property;
    exports org.rmit.model.Agreement;

    //controller
    exports org.rmit.controller.Renter;
    exports org.rmit.controller.Start;
    exports org.rmit.controller.Owner;
    exports org.rmit.controller.Host;


    //view
    exports org.rmit.view.Start;
    exports org.rmit.view.Renter;
    exports org.rmit.view.Owner;
    exports org.rmit.view.Host;



}
