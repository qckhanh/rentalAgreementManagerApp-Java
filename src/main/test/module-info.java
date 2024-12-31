module module_name {
    requires org.junit.jupiter.api;
    exports sample;
    opens sample to org.junit.platform.commons;
}