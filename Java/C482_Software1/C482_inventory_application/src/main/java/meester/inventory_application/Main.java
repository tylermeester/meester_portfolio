package meester.inventory_application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;

import static model.Inventory.partId;
import static model.Inventory.productId;


/**
 * FUTURE ENHANCEMENT: When a Part is deleted that is in a Product's associatedPartList, the deleted Part still exists
 * in the Product's associatedPartList. This application might be improved by also removing the deleted part
 * from all associatedPartsList(s) upon part deletion, or at the very least indicating to the user whether parts in
 * the associatedPartList are still existent or not.
 *
 */
public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MainMenu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 500);
        stage.setTitle("Inventory Application");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        //Populating application with part data
        InHouse engine = new InHouse(partId, "engine", 1000, 6, 1, 20, 2);
        Inventory.addPart(engine);
        partId++;

        InHouse transmission = new InHouse(partId, "transmission", 1500, 8, 1, 20, 3);
        Inventory.addPart(transmission);
        partId++;

        InHouse windshield = new InHouse(partId, "windshield", 300, 10, 1, 20, 1);
        Inventory.addPart(windshield);
        partId++;

        Outsourced steering_wheel = new Outsourced(partId,"steering wheel", 200, 15, 1, 20, "Steer Clear");
        Inventory.addPart(steering_wheel);
        partId++;

        Outsourced seat = new Outsourced(partId, "seat", 95, 10, 1,20, "Take A Seat");
        Inventory.addPart(seat);
        partId++;

        Outsourced stereo = new Outsourced(partId, "stereo", 150, 15, 1, 20, "Sounds About Right");
        Inventory.addPart(stereo);
        partId++;

        Outsourced battery = new Outsourced(partId, "battery", 500, 12, 1, 20, "Amped Up");
        Inventory.addPart(battery);
        partId++;

        Outsourced mirror = new Outsourced(partId, "mirror", 100, 13, 1, 20, "I.C.U");
        Inventory.addPart(mirror);
        partId++;


        //Populating application with product data
        Product ford = new Product(productId, "Ford", 30000, 5, 1,10);
        Inventory.addProduct(ford);
        productId++;

        Product chevy = new Product(productId, "Chevrolet", 25000, 3, 1, 10);
        Inventory.addProduct(chevy);
        productId++;

        Product toyota = new Product(productId, "Toyota", 20000, 5, 1, 10);
        Inventory.addProduct(toyota);
        productId++;

        Product nissan = new Product(productId, "Nissan", 22000, 9, 1, 10);
        Inventory.addProduct(nissan);
        productId++;

        Product tesla = new Product(productId, "Tesla", 35000, 2, 1, 10);
        Inventory.addProduct(tesla);
        productId++;






        launch();
    }
}