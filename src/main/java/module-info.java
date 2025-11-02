/*
module com.ldal.pigeonapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.sql;
    requires jakarta.mail;

    opens com.ldal.pigeonapp to javafx.fxml;
    exports com.ldal.pigeonapp;
}
*/

module com.ldal.pigeonapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.mail;
    requires jakarta.activation;
    requires org.controlsfx.controls;
    requires java.sql;

    exports com.ldal.pigeonapp;
    opens com.ldal.pigeonapp to javafx.fxml;
}