package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import meester.inventory_application.Main;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static model.Inventory.lookupPart;
import static model.Inventory.productId;


public class AddProduct implements Initializable {
    /*-------------------------------------------
    ----------ADD PRODUCT ATTRIBUTES-------------
    --------------------------------------------*/
    /**
     * Stage object
     */
    Stage stage;

    /**
     * Product Name TextField
     */
    @FXML
    private TextField productNameText;

    /**
     * Product Inventory TextField
     */
    @FXML
    private TextField productInventoryText;

    /**
     * Product Price TextField
     */
    @FXML
    private TextField productPriceText;

    /**
     * Prouct Max TextField
     */
    @FXML
    private TextField productMaxText;

    /**
     * Product Min TextField
     */
    @FXML
    private TextField productMinText;

    /**
     * Search Parts TextField
     */
    @FXML
    private TextField searchParts;

    /**
     * Part TableView
     */
    @FXML
    private TableView<Part> partTableView;

    /**
     * Part ID Column in partTableView
     */
    @FXML
    private TableColumn<Part, Integer> partIdCol;

    /**
     * Part Name Column in partTableView
     */
    @FXML
    private TableColumn<Part, String> partNameCol;

    /**
     * Part Inventory Column in partTableView
     */
    @FXML
    private TableColumn<Part, Integer> partInventoryCol;

    /**
     * Part Price Column in partTableView
     */
    @FXML
    private TableColumn<Part, Double> partPriceCol;

    /**
     * ObservableList for the Product's associated parts
     */
    private ObservableList<Part> associatedPartList = FXCollections.observableArrayList();

    /**
     * Associated Part TableView
     */
    @FXML
    private TableView<Part> associatedPartTableView;

    /**
     * Part ID Column in associatedPartTableView
     */
    @FXML
    private TableColumn<Part, Integer> associatedPartIdCol;

    /**
     * Part Name Column in associatedPartTableView
     */
    @FXML
    private TableColumn<Part, String> associatedPartNameCol;

    /**
     * Part Inventory Column in associatedPartTableView
     */
    @FXML
    private TableColumn<Part, Integer> associatedPartInventoryCol;

    /**
     * Part Price Column in associatedPartTableView
     */
    @FXML
    private TableColumn<Part, Double> associatedPartPriceCol;


    /*-------------------------------------------
    -------ADD/REMOVE ASSOCIATED PARTS-----------
    --------------------------------------------*/
    /**
     * Adds the selected Part to the Product's associated parts.
     * @param event clicking the Add button
     */
    @FXML
    void onActionAddAssociatedPart(ActionEvent event) {
        //Method to add the selected part to the associatedPartList

        Part newAssociatedPart = partTableView.getSelectionModel().getSelectedItem();
        associatedPartList.add(newAssociatedPart);
    }

    /**
     * Removes the selected part from the Product's associated parts.
     * @param event clicking on the Remove button.
     */
    @FXML
    private void onActionRemoveAssociatedPart(ActionEvent event) {
        if (associatedPartTableView.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please select an associated part to remove");
            alert.showAndWait();}

        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to remove this associated part?");

            Optional<ButtonType> result = alert.showAndWait();

            ObservableList<Product> allProducts = Inventory.getAllProducts();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Part part = associatedPartTableView.getSelectionModel().getSelectedItem();
                associatedPartList.remove(part);}
        }
    }


    /*-------------------------------------------
    -----------SAVE PRODUCT/CANCEL---------------
    --------------------------------------------*/
    /**
     * Adds a new Product to Inventory using the inputted data. Product Id will be automatically generated.
     * If any of the TextFields are incomplete the program will throw an error.
     *
     * @param event clicking on the Save Product button
     * @throws IOException when loading the scene
     */
    @FXML
    void onActionSaveProduct(ActionEvent event) throws IOException {
        try {
            //Assigning id
            int id = productId;

            //Incrementing the productId for the next use case
            productId++;

            //Product attributes
            String name = productNameText.getText();
            int inventory = Integer.parseInt(productInventoryText.getText());
            double price = Double.parseDouble(productPriceText.getText());
            int max = Integer.parseInt(productMaxText.getText());
            int min = Integer.parseInt(productMinText.getText());

            Product newProduct = new Product(id, name, price, inventory, min, max);

            //For loop to save each part from the associatedPartList into the newProduct
            for (Part part : associatedPartList) {
                newProduct.addAssociatedPart(part);
            }

            double priceOfParts = 0;


            for (Part part : associatedPartList) {
                priceOfParts += part.getPrice();
            }

            if ((priceOfParts < price) && (inventory <= max) && (inventory >= min) && (min < max)) {
                //Adding the newProduct to the Inventory
                Inventory.addProduct(newProduct);

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
                if (priceOfParts > price) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setContentText("Price of the Product must be greater than the price of associated parts");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setContentText("Inventory must be between Min Value and Max Value and " +
                            "Min Value must be less than Max Value");
                    alert.showAndWait();
                }
            }
        }

        catch(NumberFormatException e){
            //Creating error alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please enter a valid value for each text field");
            alert.showAndWait();

            //If this error is thrown this will reset the partId to it's initial value
            productId--;
        }
    }

    /**
     * Cancels the creation of a new Product and returns to the Main Menu.
     *
     * @param event clicking on the Cancel button
     * @throws IOException when loading the scene
     */
    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
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


    /*------------------------------------------
    ----------PART SEARCH FUNCTIONALITY---------
    -------------------------------------------*/
    /**
     * Clears the text in the TextField when the TextField is clicked.
     * @param mouseEvent Clicking inside the TextField
     */
    public void onPartSearchClick(MouseEvent mouseEvent) {
        //This method clears the text in the TextField when the user selects the TextField to input a value
        searchParts.setText("");
    }

    /**
     * Searches all Parts in Inventory for the inputted Part Name or Part Id after each keystroke.
     * If no results are found all Part objects are displayed in the TableView.
     * RUNTIME ERROR: Initially the selected Part would not be visible in the TableView unless that
     * section of the TableView was already in view. This was fixed by implementing the .scrollTo() method.
     *
     * @param keyEvent a keystroke while typing in the partTableView search bar
     */
    public void onPartKeyRelease(KeyEvent keyEvent) {
        //This method filters the search results as the user types into the TextField

        //Gets the text in the search box
        String searchText = searchParts.getText();

        ObservableList<Part> allParts = Inventory.getAllParts();

        ObservableList<Part> matchingParts = Inventory.lookupPart(searchText);

        Part partToHighlight = null;

        if(matchingParts.size() == 0){
            try {
                int id = Integer.parseInt(searchText);
                Part part = Inventory.lookupPart(id);

                if (part != null)
                    partToHighlight = part;

            }
            catch (NumberFormatException e){
                //Ignore
            }
        }

        if(matchingParts.isEmpty() && partToHighlight == null){
            partTableView.getSelectionModel().clearSelection();
            partTableView.scrollTo(0);
            partTableView.setItems(allParts);}

        else if (matchingParts.size() > 0){
            partTableView.getSelectionModel().clearSelection();
            partTableView.setItems(matchingParts);
            partTableView.scrollTo(0);}

        else if (partToHighlight != null){
            partTableView.getSelectionModel().select(partToHighlight);
            partTableView.scrollTo(partToHighlight);}
    }

    /**
     * Searches all Parts in Inventory for the inputted Part Name or Part Id. If no results are found
     * all Part objects are displayed in the TableView.
     * RUNTIME ERROR: Initially the selected Part would not be visible in the TableView unless that
     * section of the TableView was already in view. This was fixed by implementing the .scrollTo() method.
     *
     * @param actionEvent pressing enter/return after typing in the TextField
     */
    public void onPartSearch(ActionEvent actionEvent) {
        //This method filters the search results when the user presses 'Enter'
        //If not results are found, displays a dialogue box

        //Gets the text in the search box
        String searchText = searchParts.getText();

        ObservableList<Part> allParts = Inventory.getAllParts();

        ObservableList<Part> matchingParts = Inventory.lookupPart(searchText);

        Part partToHighlight = null;

        if(matchingParts.size() == 0){
            try {
                int id = Integer.parseInt(searchText);
                Part part = Inventory.lookupPart(id);

                if (part != null)
                    partToHighlight = part;

            }
            catch (NumberFormatException e){
                //Ignore
            }
        }

        if(matchingParts.isEmpty() && partToHighlight == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("No Matching Parts Found");
            alert.showAndWait();}

        else if (matchingParts.size() > 0){
            partTableView.getSelectionModel().clearSelection();
            partTableView.setItems(matchingParts);
            partTableView.scrollTo(0);}

        else if (partToHighlight != null){
            partTableView.getSelectionModel().select(partToHighlight);
            partTableView.scrollTo(partToHighlight);}
    }


    /*-------------------------------------------
    ------------------INITIALIZE-----------------
    --------------------------------------------*/
    /**
     * Initializes the partTableView and the associatedPartTableView and sorts the
     * entries by partId
     *
     * @param url url
     * @param resourceBundle resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Populating Parts TableView
        partTableView.setItems(Inventory.getAllParts());
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInventoryCol.setCellValueFactory(new PropertyValueFactory<>("inventory"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        //Sorting the tableView by id
        partTableView.getSortOrder().add(partIdCol);
        partTableView.sort();


        associatedPartTableView.setItems(associatedPartList);
        associatedPartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartInventoryCol.setCellValueFactory(new PropertyValueFactory<>("inventory"));
        associatedPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        //Sorting the tableView by id
        associatedPartTableView.getSortOrder().add(associatedPartIdCol);
        associatedPartTableView.sort();
    }
}
