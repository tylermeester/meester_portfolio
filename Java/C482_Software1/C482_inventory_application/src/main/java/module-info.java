module meester.inventory_application {
    requires javafx.controls;
    requires javafx.fxml;


    opens meester.inventory_application to javafx.fxml;
    exports meester.inventory_application;
    exports controller;
    opens controller to javafx.fxml;

    //Needed to add this in order to create tableview in main menu
    opens model to javafx.base;
}