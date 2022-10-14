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
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class ModifyPart implements Initializable {

    /*-------------------------------------------
    ---------MODIFY PART ATTRIBUTES--------------
    --------------------------------------------*/
    /**
     * Stage object
     */
    Stage stage;

    /**
     * GridPane
     */
    @FXML
    private GridPane modPartGrid;

    /**
     * In House Radio Button
     */
    @FXML
    private RadioButton modPartInHouseRB;

    /**
     * Outsourced Radio Button
     */
    @FXML
    private RadioButton modPartOutsourceRB;

    /**
     * Part ID Textfield
     */
    @FXML
    private TextField partIdText;

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
     * Part Min TextField
     */
    @FXML
    private TextField partMinText;

    /**
     * Part Max TextField
     */
    @FXML
    private TextField partMaxText;

    /**
     * Part Machine ID Label
     */
    @FXML
    private Label partMachineIdLabel;

    /**
     * Part Machine ID TextField
     */
    @FXML
    private TextField partMachineIdText;

    /**
     * Part Company Name Label
     */
    @FXML
    private Label partCompanyNameLabel;

    /**
     * Part Company Name TextField
     */
    @FXML
    private TextField partCompanyNameText;


    /*-------------------------------------------
    ----------------RADIO BUTTONS----------------
    --------------------------------------------*/
    /**
     * Changes the available TextFields when the In-House radio button is selected
     * @param actionEvent Clicking on the InHouse RadioButton
     */
    public void onInHouse(ActionEvent actionEvent) {

        modPartGrid.getChildren().removeAll(partMachineIdLabel, partCompanyNameLabel, partMachineIdText, partCompanyNameText);

        modPartGrid.add(partMachineIdLabel, 0, 5, 1, 1);
        modPartGrid.add(partMachineIdText, 1, 5, 1, 1);
    }

    /**
     * Changes the available TextFields when the Outsourced radio button is selected
     * @param actionEvent Clicking on the Outsourced RadioButton
     */
    public void onOutsourced(ActionEvent actionEvent) {

        modPartGrid.getChildren().removeAll(partMachineIdLabel, partCompanyNameLabel, partMachineIdText, partCompanyNameText);

        modPartGrid.add(partCompanyNameLabel, 0, 5, 1, 1);
        modPartGrid.add(partCompanyNameText, 1, 5, 1, 1);
    }


    /*-------------------------------------------
    --------SEND PART BETWEEN CONTROLLERS--------
    --------------------------------------------*/
    /**
     * Sends the selected Part data values from another controller to the ModifyPart controller.
     * Depending on whether the Part is an instanceof InHouse or Outsourced, the relevant data will be forwarded.
     *
     * @param part the part to be sent
     */
    public void sendPart(Part part){

        //If-else statement to determine which subclass the part is and send the appropriate data
        if(part instanceof InHouse) {
            modPartInHouseRB.setSelected(true);
            modPartGrid.add(partMachineIdLabel, 0, 5, 1, 1);
            modPartGrid.add(partMachineIdText, 1, 5, 1, 1);
            partMachineIdText.setText(String.valueOf(((InHouse) part).getMachineId()));
        }
        else {
            modPartOutsourceRB.setSelected(true);
            modPartGrid.add(partCompanyNameLabel, 0, 5, 1, 1);
            modPartGrid.add(partCompanyNameText, 1, 5, 1, 1);
            partCompanyNameText.setText(((Outsourced) part).getCompanyName());
        }

        //Setting the TextFields to have the part data
        partIdText.setText(String.valueOf(part.getId()));
        partNameText.setText(part.getName());
        partInventoryText.setText(String.valueOf(part.getInventory()));
        partPriceText.setText(String.valueOf(part.getPrice()));
        partMinText.setText(String.valueOf(part.getMin()));
        partMaxText.setText(String.valueOf(part.getMax()));
    }


    /*-------------------------------------------
    --------------SAVE PART/CANCEL---------------
    --------------------------------------------*/
    /**
     * Updates the Part with the user inputted values. Depending on whether the InHouse or Outsourced RadioButton
     * is selected the MachineId or CompanyName will be saved. If any of the TextFields are incomplete the
     * program will throw an error.
     *
     * @param event clicking on the Save Part button
     * @throws IOException when loading the scene
     */
    public void onModifyPartSave(ActionEvent event) throws IOException {

        try {
            //Determines the id value of the oldPart by reading the textField value.
            // This id will be the same for the updatedPart
            int id = Integer.parseInt(partIdText.getText());

            //Locates and deletes the oldPart object by using the id, which is unchanging
            Inventory.deletePart(Inventory.lookupPart(id));

            //Saves the new textField values as attributes
            String name = partNameText.getText();
            int inventory = Integer.parseInt(partInventoryText.getText());
            double price = Double.parseDouble(partPriceText.getText());
            int max = Integer.parseInt(partMaxText.getText());
            int min = Integer.parseInt(partMinText.getText());

            //Initializes the machineId and companyName attributes for future use
            int machineId;
            String companyName;

            //creates empty instances of InHouse/Outsourced Part objects for future use
            InHouse updatedInHousePart = null;
            Outsourced updatedOutsourcedPart = null;

            if ((inventory <= max) && (inventory >= min) && (min < max)) {

                //Depending on which radio button is selected (In House/Outsourced) the relevant attributes will be
                // instantiated and a new Part object will be created and added to allParts,
                // the Inventory static observable list
                if (modPartInHouseRB.isSelected()) {
                    machineId = Integer.parseInt(partMachineIdText.getText());
                    updatedInHousePart = new InHouse(id, name, price, inventory, min, max, machineId);
                    Inventory.addPart(updatedInHousePart);
                } else if (modPartOutsourceRB.isSelected()) {
                    companyName = partCompanyNameText.getText();
                    updatedOutsourcedPart = new Outsourced(id, name, price, inventory, min, max, companyName);
                    Inventory.addPart(updatedOutsourcedPart);
                }

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
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Inventory must be between Min Value and Max Value and " +
                        "Min Value must be less than Max Value");
                alert.showAndWait();
            }
        }
        catch (NumberFormatException e){
            //Creating error alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please enter a valid value in each field");
            alert.showAndWait();
        }
    }

    /**
     * Cancels the part modification and returns to the Main Menu.
     *
     * @param event clicking on the Cancel button
     * @throws IOException when loading the scene
     */
    public void onModifyPartCancel(ActionEvent event) throws IOException {

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
     * Initializes the modPartGrid by removing the InHouse/Outsourced specific label and TextField. These will
     * be added again, depending on whether the user selects the InHouse or Outsourced RadioButtons.
     * Also makes the ID TextField uneditable.
     *
     * @param url url
     * @param resourceBundle resourceBundle
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Makes the partId uneditable
        partIdText.setEditable(false);

        //Sets the default fields by removing MachineId and CompanyName.
        // Depending on which radio button is selected, the relevant label and text will be added again
        modPartGrid.getChildren().removeAll(partMachineIdLabel, partCompanyNameLabel, partMachineIdText, partCompanyNameText);

    }

}
