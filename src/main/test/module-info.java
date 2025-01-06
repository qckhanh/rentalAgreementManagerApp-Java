module module_name {
    requires org.junit.jupiter.api;
    requires org.rmit.demo;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;

    exports DAOtest to org.junit.platform.commons;
    opens DAOtest to org.junit.platform.commons;

    exports sample;
    opens sample to org.junit.platform.commons;
}