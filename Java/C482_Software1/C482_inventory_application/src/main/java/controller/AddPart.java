package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import meester.inventory_application.Main;
import model.InHouse;
import model.Inventory;
import model.Outsourced;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static model.Inventory.partId;


public class AddPart implements Initializable {

    /*-------------------------------------------
    ------------ADD PART ATTRIBUTES--------------
    --------------------------------------------*/
    /**
     * Stage object
     */
    Stage stage;

    /**
     * GridPane
     */
    @FXML
    private GridPane addPartGrid;

    /**
     * Part Name TextField
     */
    @FXML
    private TextField partNameText;

    /**
     * Part Inventory TextField
     */
    @FXML
    private TextField partInventoryText;

    /**
     * Part Price TextField
     */
    @FXML
    private TextField partPriceText;

    /**
     * Part Max TextField
     */
    @FXML
    private TextField partMaxText;

    /**
     * Part Min TextField
     */
    @FXML
    private TextField partMinText;

    /**
     * Part Machine Id Label
     * Is displayed depending on InHouse/Outsourced RadioButton selection
     */
    @FXML
    private Label partMachineIdLabel;

    /**
     * Part Machine Id TextField
     * Is displayed depending on InHouse/Outsourced RadioButton selection
     */
    @FXML
    private TextField partMachineIdText;

    /**
     * Part CompanyName Label
     * Is displayed depending on InHouse/Outsourced RadioButton selection
     */
    @FXML
    private Label partCompanyNameLabel;

    /**
     * Part CompanyName TextField
     * Is displayed depending on InHouse/Outsourced RadioButton selection
     */
    @FXML
    private TextField partCompanyNameText;

    /**
     * Boolean used for logic to determine whether a part is InHouse or Outsourced
     */
    boolean inHouse;
    boolean Outsourced;


    /*-------------------------------------------
    ----------------RADIO BUTTONS----------------
    --------------------------------------------*/
    /**
     * Changes the available TextFields when the In-House radio button is selected
     * @param actionEvent Clicking on the InHouse RadioButton
     */
    @FXML
    void onInHouse(ActionEvent actionEvent) {

        addPartGrid.getChildren().removeAll(partMachineIdLabel, partCompanyNameLabel, partMachineIdText, partCompanyNameText);

        addPartGrid.add(partMachineIdLabel, 0, 5, 1, 1);
        addPartGrid.add(partMachineIdText, 1, 5, 1, 1);

        //changes inHouse boolean to true
        inHouse = true;

    }

    /**
     * Changes the available TextFields when the Outsourced radio button is selected
     * @param actionEvent Clicking on the Outsourced RadioButton
     */
    @FXML
    void onOutsourced(ActionEvent actionEvent) {

        addPartGrid.getChildren().removeAll(partMachineIdLabel, partCompanyNameLabel, partMachineIdText, partCompanyNameText);

        addPartGrid.add(partCompanyNameLabel, 0, 5, 1, 1);
        addPartGrid.add(partCompanyNameText, 1, 5, 1, 1);

        //Changes inHouse to false
        Outsourced = true;
    }


    /*-------------------------------------------
    --------------SAVE PART/CANCEL---------------
    --------------------------------------------*/
    /**
     * Adds a new Part to Inventory using the inputted data. partId will be automatically generated and incremented.
     * Depending on whether the InHouse or Outsourced RadioButton is selected the MachineId or CompanyName
     * will be saved. If any of the TextFields are incomplete the program will throw an error.
     *
     * @param event clicking on the Save Part button
     * @throws IOException when loading the scene
     */
    @FXML
    void onAddPartSave(ActionEvent event) throws IOException {

        try {
            //Assigning id
            int id = partId;

            //Part attributes
            String name = partNameText.getText();
            int inventory = Integer.parseInt(partInventoryText.getText());
            double price = Double.parseDouble(partPriceText.getText());
            int max = Integer.parseInt(partMaxText.getText());
            int min = Integer.parseInt(partMinText.getText());


            //InHouse and Outsourced Fields
            int machineId;
            String companyName = partCompanyNameText.getText();


            if ((inventory <= max) && (inventory >= min) && (min < max)){
                //if-else-if statement to determine InHouse/Outsourced and assign value
                if (inHouse) {
                    machineId = Integer.parseInt(partMachineIdText.getText());
                    Inventory.addPart(new InHouse(id, name, price, inventory, max, min, machineId));
                } else if (Outsourced)
                    Inventory.addPart(new Outsourced(id, name, price, inventory, max, min, companyName));

                //Incrementing the partId for the next use case
                partId++;

                //Gets the window/stage of the scene where the button event took place, and casts it to type 'Stage'
                //This is the original stage
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

                //Creates a new FXMLLoader object that references the .fxml document for the next scene
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MainMenu.fxml"));

                //Creates a new scene using the fxmlLoader object that was just created
                Scene newScene = new Scene(fxmlLoader.load(), 1000, 500);

                //Changes the scene to 'newScene' which was just created
                stage.setScene(newScene);

                //Shows the stage with the new scene
                stage.show();}
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Inventory must be between Min Value and Max Value and " +
                        "Min Value must be less than Max Value");
                alert.showAndWait();
            }
        }

        catch(NumberFormatException e){
            //Creating error alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please enter a valid value for each text field");
            alert.showAndWait();
        }
    }

    /**
     * Cancels the creation of a new part and returns to the Main Menu.
     *
     * @param event clicking on the Cancel button
     * @throws IOException when loading the scene
     */
    @FXML
    void onAddPartCancel(ActionEvent event) throws IOException {

        Stage stage;

        //Gets the window/stage of the scene where the button event took place, and casts it to type 'Stage'
        //This is the original stage
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        //Creates a new FXMLLoader object that references the .fxml document for the next scene
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MainMenu.fxml"));

        //Creates a new scene using the fxmlLoader object that was just created
        Scene newScene = new Scene(fxmlLoader.load(), 1000, 500);

        //Changes the scene to 'newScene' which was just created
        stage.setScene(newScene);

        //Shows the stage with the new scene
        stage.show();
    }



    /*-------------------------------------------
    ------------------INITIALIZE-----------------
    --------------------------------------------*/

    /**
     * Initializes the addPartGrid by removing the InHouse/Outsourced specific label and TextField. These will
     * be added again, depending on whether the user selects the InHouse or Outsourced RadioButtons.
     *
     * @param url url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Sets the default fields by removing MachineId and CompanyName
        addPartGrid.getChildren().removeAll(partMachineIdLabel, partCompanyNameLabel, partMachineIdText, partCompanyNameText);
        inHouse = false;
        Outsourced = false;

    }

}



