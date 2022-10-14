package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import meester.inventory_application.Main;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import static controller.ModifyProduct.productToModify;

public class MainMenu implements Initializable {
    /*-------------------------------------------
    ------------MAIN MENU ATTRIBUTES-------------
    --------------------------------------------*/
    /**
     * TableView that displays all the Parts in the program
     */
    @FXML
    private TableView<Part> partTableView;

    /**
     * Part 'Id' column in partTableView
     */
    @FXML
    private TableColumn<Part, Integer> partIdCol;

    /**
     * Part 'Name' column in partTableView
     */
    @FXML
    private TableColumn<Part, String> partNameCol;

    /**
     * Part 'Inventory' column in partTableView
     */
    @FXML
    private TableColumn<Part, Integer> partInventoryCol;

    /**
     * Part 'Price' Column in partTableView
     */
    @FXML
    private TableColumn<Part, Double> partPriceCol;

    /**
     * TableView that displays all Products in the program
     */
    @FXML
    private TableView<Product> productTableView;

    /**
     * Product 'Id' column in productTableView
     */
    @FXML
    private TableColumn<Product, Integer> productIdCol;

    /**
     * Product 'Name' column in productTableView
     */
    @FXML
    private TableColumn<Product, String> productNameCol;

    /**
     * Product 'Inventory' column in productTableView
     */
    @FXML
    private TableColumn<Product, Integer> productInventoryCol;

    /**
     * Product 'Price' column in productTableView
     */
    @FXML
    private TableColumn<Product, Double> productPriceCol;

    /**
     * Part search TextField
     */
    @FXML
    private TextField searchParts;

    /**
     * Product search TextField
     */
    @FXML
    private TextField searchProducts;

    /**
     * Stage object
     */
    Stage stage;

    public static void changeScenes(String fxmlDocument, ActionEvent event) throws IOException {
        Stage stage;

        //Gets the window/stage of the scene where the button event took place, and casts it to type 'Stage'
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        //Creates a new FXMLLoader object that references the .fxml document for the next scene
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxmlDocument));

        //Creates a new scene using the fxmlLoader object that was just created
        Scene newScene = new Scene(fxmlLoader.load());

        //Changes the scene to 'newScene' which was just created
        stage.setScene(newScene);

        //Shows the stage with the new scene
        stage.show();
    }


    /*------------------------------------------
    -------------------PARTS--------------------
     ------------------------------------------*/
    /**
     * Displays the program's 'Add Part' page
     * RUNTIME ERROR: The program would load an incorrectly sized window for the following screen. This was
     * fixed by specifying the size of the newScene in its instantiation.
     *
     * @param event the ActionEvent that initiates the method, in this case clicking the 'Add Part' button
     * @throws IOException when calling .load() on the FXMLLoader object to generate the newScene
     */
    public void onActionAddPart(ActionEvent event) throws IOException {
        changeScenes("AddPart.fxml", event);
    }


    /**
     * Displays the 'Modify Part' page for the Part that is selected in the partTableView. Sending the selected
     * Part object to the ModifyPart controller requires the ModifyPart controller's public method, sendPart().
     * If no Part is selected an error is thrown.
     * RUNTIME ERROR: Initially the sendPart() method was private and could not be called for in this controller.
     * This was fixed by making the sendPart() method public.
     *
     * @param event the ActionEvent that initiates the method, in this case clicking the 'Modify Part' button
     * @throws IOException when calling .load() on the FXMLLoader object to generate the newScene
     */
    public void onActionModifyPart(ActionEvent event) throws IOException {
        //Creates FXMLLoader object
        FXMLLoader loader = new FXMLLoader();

        //Sets the FXML file location for the loader object
        loader.setLocation(Main.class.getResource("ModifyPart.fxml"));

        //loads the ModifyPart.fxml document
        loader.load();

        //Creates a ModifyPart object and tells the loader to use that controller, this enables the
        // use of the 'sendAnimal' method in the ModifyPart controller
        ModifyPart modifyPartController = loader.getController();

        try {
            //Uses sendPart method to send the part that is selected in the TableView to the ModifyPart page
            modifyPartController.sendPart(partTableView.getSelectionModel().getSelectedItem());

            //Gets the window/stage of the scene where the button event took place, and casts it to type 'Stage'
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

            //Creates a Parent scene
            Parent scene = loader.getRoot();

            //Changes the scene using .setScene() method
            stage.setScene(new Scene(scene));

            //Displays the new scene
            stage.show();
        }

        catch(NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please select a part to modify");
            alert.showAndWait();
        }
    }


    /**
     * Deletes the selected part in the partTableView from Inventory. The program prompts the user for confirmation
     * before deletion. If no part is selected an error is thrown.
     * RUNTIME ERROR: Initially the nested if-else statements would not function correctly because of misplaced
     * curly braces. This was fixed by correctly placing curly braces.
     *
     * @param actionEvent the ActionEvent that initiates the method, in this case clicking the 'Delete Part' button
     */
    public void onActionDeletePart(ActionEvent actionEvent) {

        if (partTableView.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please select a part to delete");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to delete this part?");

            Optional<ButtonType> result = alert.showAndWait();

            ObservableList<Part> allParts = Inventory.getAllParts();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Part part = partTableView.getSelectionModel().getSelectedItem();
                Inventory.deletePart(part);
            }
        }
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
            partTableView.setItems(allParts);
        }

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


    /*------------------------------------------
    ------------------PRODUCTS------------------
    -------------------------------------------*/
    /**
     * Displays the program's 'Add Product' page
     * RUNTIME ERROR: The program would load an incorrectly sized window for the following screen. This was
     * fixed by specifying the size of the newScene in its instantiation.
     *
     * @param event the ActionEvent that initiates the method, in this case clicking the 'Add Product' button
     * @throws IOException when calling .load() on the FXMLLoader object to generate the newScene
     */
    public void onActionAddProduct(ActionEvent event) throws IOException {

        //Gets the window/stage of the scene where the button event took place, and casts it to type 'Stage'
        //This is the original stage
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        //Creates a new FXMLLoader object that references the .fxml document for the next scene
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("AddProduct.fxml"));

        //Creates a new scene using the fxmlLoader object that was just created
        Scene newScene = new Scene(fxmlLoader.load());

        //Changes the scene to 'newScene' which was just created
        stage.setScene(newScene);

        //Shows the stage with the new scene
        stage.show();
    }

    /**
     * Displays the 'Modify Product' page for the Product that is selected in the productTableView.
     * Sending the selected product object to the ModifyProduct controller requires the ModifyProduct controller's
     * public method sendProduct(). If no Product is selected an error is thrown.
     * RUNTIME ERROR: Initially the sendProduct() method was private and could not be called for in this controller.
     * This was fixed by making the sendProduct() method public.
     *
     * @param event the ActionEvent that initiates the method, in this case clicking the 'Modify Product' button
     * @throws IOException when calling .load() on the FXMLLoader object to generate the newScene
     */
    public void onActionModifyProduct(ActionEvent event) throws IOException {

        //Saving the selected item to the ModifyProduct static variable, productToModify
        productToModify = productTableView.getSelectionModel().getSelectedItem();

        //Create FXMLLoader object
        FXMLLoader loader = new FXMLLoader();

        //Setting the FXML file we are going to with the loader object we just created
        loader.setLocation(Main.class.getResource("ModifyProduct.fxml"));

        //loads the setLocation we previously defined
        loader.load();

        //Creates a new ModifyProduct object from the controller for this page
        // this allows the use of methods within that controller
        ModifyProduct modifyProductController = loader.getController();

        try{
        //Calls the sendProduct method from the modifyProductController to send the productToModify to
        // the modifyProduct page
        modifyProductController.sendProduct(productToModify);

        //Creates a Parent object called scene which uses the loader object's 'getRoot'
        Parent scene = loader.getRoot();

        //Makes a new stage object
        Stage stage = new Stage();

        //Sets the title
        stage.setTitle("ModifyProduct");

        //Changes the scene to 'scene' which was just created
        stage.setScene(new Scene(scene));

        //Shows the stage with the new scene
        stage.show();}

        catch(NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please select a product to modify");
            alert.showAndWait();
        }
    }

    /**
     * Deletes the selected Product in the productTableView from Inventory. The program prompts the user for confirmation
     * before deletion. If no product is selected an error is thrown.
     * RUNTIME ERROR: Initially the nested if-else statements would not function correctly because of misplaced
     * curly braces. This was fixed by correctly placing curly braces.
     *
     * @param actionEvent the ActionEvent that initiates the method, in this case clicking the 'Delete Product' button
     */
    public void onActionDeleteProduct(ActionEvent actionEvent) {

        if (productTableView.getSelectionModel().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please select a product to delete");
            alert.showAndWait();}

        else if (!productTableView.getSelectionModel().getSelectedItem().getAllAssociatedParts().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Remove all associated parts before deleting this product");
            alert.showAndWait();}

        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to delete this product?");

            Optional<ButtonType> result = alert.showAndWait();

            ObservableList<Product> allProducts = Inventory.getAllProducts();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Product product = productTableView.getSelectionModel().getSelectedItem();
                Inventory.deleteProduct(product);}
        }
    }


    /*------------------------------------------
    --------PRODUCT SEARCH FUNCTIONALITY--------
    -------------------------------------------*/
    /**
     * Clears the text in the TextField when the TextField is clicked.
     * @param mouseEvent Clicking inside the TextField
     */
    public void onProductSearchClick(MouseEvent mouseEvent) {
        //This MouseEvent clears the text in the TextField when the user selects the TextField to input a value

        searchProducts.setText("");
    }

    /**
     * Searches all Products in Inventory for the inputted Product Name or Product Id after each keystroke.
     * If no results are found all Product objects are displayed in the TableView.
     * RUNTIME ERROR: Initially the selected Product would not be visible in the TableView unless that
     * section of the TableView was already in view. This was fixed by implementing the .scrollTo() method.
     *
     * @param keyEvent a keystroke while typing in the productTableView search bar
     */
    public void onProductKeyRelease(KeyEvent keyEvent) {
        //This KeyEvent filters the search results as the user types into the TextField

        //Gets the text in the search box
        String searchText = searchProducts.getText();

        ObservableList<Product> allProducts = Inventory.getAllProducts();

        ObservableList<Product> matchingProducts = Inventory.lookupProduct(searchText);

        Product productToHighlight = null;

        if(matchingProducts.size() == 0){
            try {
                int id = Integer.parseInt(searchText);
                Product product = Inventory.lookupProduct(id);

                if (product != null)
                    productToHighlight = product;

            }
            catch (NumberFormatException e){
                //Ignore
            }
        }

        if(matchingProducts.isEmpty() && productToHighlight == null){
            productTableView.getSelectionModel().clearSelection();
            productTableView.scrollTo(0);
            productTableView.setItems(allProducts);}

        else if (matchingProducts.size() > 0){
            productTableView.getSelectionModel().clearSelection();
            productTableView.setItems(matchingProducts);
            productTableView.scrollTo(0);}

        else if (productToHighlight != null){
            productTableView.getSelectionModel().select(productToHighlight);
            productTableView.scrollTo(productToHighlight);}
    }

    /**
     * Searches all Products in Inventory for the inputted Product Name or Product Id after each keystroke.
     * If no results are found all Product objects are displayed in the TableView.
     * RUNTIME ERROR: Initially the selected Product would not be visible in the TableView unless that
     * section of the TableView was already in view. This was fixed by implementing the .scrollTo() method.
     *
     * @param actionEvent pressing enter/return after typing in the TextField
     */
    public void onProductSearch(ActionEvent actionEvent) {
        //This ActionEvent filters the search results when the user presses 'Enter'
        //If no results are found, the program displays a dialog box

        //Gets the text in the search box
        String searchText = searchProducts.getText();

        ObservableList<Product> allProducts = Inventory.getAllProducts();

        ObservableList<Product> matchingProducts = Inventory.lookupProduct(searchText);

        Product productToHighlight = null;

        if(matchingProducts.size() == 0){
            try {
                int id = Integer.parseInt(searchText);
                Product product = Inventory.lookupProduct(id);

                if (product != null)
                    productToHighlight = product;

            }
            catch (NumberFormatException e){
                //Ignore
            }
        }

        if(matchingProducts.isEmpty() && productToHighlight == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("No Matching Products Found");
            alert.showAndWait();}

        else if (matchingProducts.size() > 0){
            productTableView.getSelectionModel().clearSelection();
            productTableView.setItems(matchingProducts);
            productTableView.scrollTo(0);}

        else if (productToHighlight != null){
            productTableView.getSelectionModel().select(productToHighlight);
            productTableView.scrollTo(productToHighlight);}
    }



    /**
     * Exits the application.
     *
     * @param actionEvent clicking on the 'Exit' button
     */
    public void onActionExit(ActionEvent actionEvent) {
        System.exit(0);
    }


    /*-------------------------------------------
    ------------------INITIALIZE-----------------
    --------------------------------------------*/
    /**
     * Initializes the partTableView and productTableView with the relevant data.
     *
     * @param url URL
     * @param resourceBundle ResourceBundle
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


        //Populating Products TableView
        productTableView.setItems(Inventory.getAllProducts());
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInventoryCol.setCellValueFactory(new PropertyValueFactory<>("inventory"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        //Sorting the tableView by id
        productTableView.getSortOrder().add(productIdCol);
        productTableView.sort();

    }
}


