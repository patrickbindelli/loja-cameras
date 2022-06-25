module br.edu.femass {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    requires org.slf4j;
    requires slf4j.reload4j;
    requires static lombok;
    requires org.postgresql.jdbc;
    requires java.sql;
    requires net.synedra.validatorfx;
    requires com.dlsc.formsfx;

    opens br.edu.femass to javafx.fxml;
    opens br.edu.femass.controller to javafx.fxml;
    opens br.edu.femass.model to javafx.base;
    exports br.edu.femass;
}