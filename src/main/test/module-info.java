module module_name {
    requires org.junit.jupiter.api;
    requires org.rmit.demo;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires org.mockito;
    requires net.synedra.validatorfx;
    requires javafx.controls;

    exports InputValidatorTest to org.mockito, org.junit.platform.commons;

    exports DAOtest to org.junit.platform.commons;
    opens DAOtest to org.junit.platform.commons;

    exports sample;
    opens sample to org.junit.platform.commons;

    opens InputValidatorTest to org.junit.platform.commons;

}