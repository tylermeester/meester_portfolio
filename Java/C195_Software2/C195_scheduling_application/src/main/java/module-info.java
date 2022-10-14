module meester.scheduling_application {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens meester.scheduling_application to javafx.fxml;
    exports meester.scheduling_application;
    exports controller;
    opens controller to javafx.fxml;
    opens model to javafx.base;
}