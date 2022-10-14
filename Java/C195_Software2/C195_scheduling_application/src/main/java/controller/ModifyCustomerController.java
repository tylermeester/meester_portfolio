package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import model.Country;
import model.Customer;
import model.Division;
import utilities.DBConnection;
import utilities.SceneChanger;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ModifyCustomerController implements Initializable {
    /*-------------------------------------------
    -----------------ATTRIBUTES------------------
    --------------------------------------------*/
    //Used public static attributes in order to send customer from the Homepage to ModifyCustomer page
    public static Customer selectedCustomer;
    public static int selectedCustomerId;

    @FXML
    private TextField customerIdText;
    @FXML
    private TextField customerNameText;
    @FXML
    private TextField customerAddressText;
    @FXML
    private TextField customerPostalText;
    @FXML
    private TextField customerPhoneText;
    @FXML
    private ComboBox<Country> countryComboBox;
    @FXML
    private ComboBox<Division> divisionComboBox;
    private ObservableList<Country> allCountries = FXCollections.observableArrayList();
    private ObservableList<Division> allDivisions = FXCollections.observableArrayList();
    private ObservableList<Division> selectedDivisions = FXCollections.observableArrayList();


    /*-------------------------------------------
    ------------------METHODS--------------------
    --------------------------------------------*/
    /**
     * References the selected Country's associated First-Level Divisions from the database and populates
     * the divisionComboBox.
     *
     * @param actionEvent the ActionEvent initiating the method, choosing a country in the countryComboBox.
     */
    public void onActionCountryComboBox(ActionEvent actionEvent) {

        //Clears the divisionComboBox to get it read for the updated options
        divisionComboBox.getSelectionModel().clearSelection();
        divisionComboBox.getSelectionModel().select(0);
        selectedDivisions.clear();

        //Gets the country that is selected in the countryComboBox
        Country selectedCountry = countryComboBox.getSelectionModel().getSelectedItem();

        //Loops through allDivisions, if the division has a countryId equal to the
        // countryId that was selected by the user then that division is added
        // to selectedDivisions
        for(Division division : allDivisions){
            if(division.getCountryId() == selectedCountry.getCountryId()){
                selectedDivisions.add(division);
            }
        }

        //Sets the divisionComboBox to have the correct options
        divisionComboBox.setItems(selectedDivisions);
    }

    /**
     * Saves the user inputted Customer information and updates the Customer in the database. Checks to ensure that
     * all TextFields have data and that the Country and Division ComboBoxes have been selected.
     *
     * @param actionEvent the ActionEvent initiating the method, clicking the Edit Customer button
     * @throws SQLException when accessing the database
     * @throws IOException when calling the SceneChanger.changeScene() method
     */
    public void onActionSaveCustomer(ActionEvent actionEvent) throws SQLException, IOException {

        DBConnection.openConnection();
        try {

            int customerId = Integer.parseInt(customerIdText.getText());
            String customerName = customerNameText.getText();
            String customerAddress = customerAddressText.getText();
            String customerPostal = customerPostalText.getText();
            String customerPhone = customerPhoneText.getText();
            int customerDivisionId = 0;

            Division newDivision = divisionComboBox.getSelectionModel().getSelectedItem();

            for (Division division : allDivisions) {
                if (division.getDivisionId() == newDivision.getDivisionId())
                    customerDivisionId = division.getDivisionId();
            }


            //Checks to ensure the TextFields are filled out
            if(customerName.isEmpty() || customerAddress.isEmpty() ||
                    customerPostal.isEmpty() || customerPhone.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Please enter a valid value in each text field!");
                alert.showAndWait();
            }
            else {
                Customer.updateCustomer(customerId, customerName, customerAddress, customerPostal, customerPhone, customerDivisionId);

                DBConnection.closeConnection();

                SceneChanger.changeScene("Homepage.fxml", actionEvent);
            }
        }
        catch(NullPointerException | NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please enter a valid value in each text field!");
            alert.showAndWait();
        }
    }

    /**
     * Returns the user to the Homepage, ignoring any user inputted data.
     *
     * @param actionEvent the ActionEvent initiating the method, clicking on the Cancel button.
     * @throws IOException when calling the SceneChanger.changeScene() method.
     */
    public void onActionCancel(ActionEvent actionEvent) throws IOException {

        SceneChanger.changeScene("Homepage.fxml", actionEvent);
    }


    /*-------------------------------------------
    -----------------INITIALIZE------------------
    --------------------------------------------*/
    /**
     * Initializes the ModifyCustomer page and populates the TextFields with the associated customer's data and the
     * Country and Division ComboBoxes with the relevant data.
     *
     * @param url URL
     * @param resourceBundle ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        DBConnection.openConnection();

        //List of all countries
        try {allCountries = Country.getAllCountries();
        } catch (SQLException e) {
            throw new RuntimeException(e);}

        //List of all Divisions
        try {allDivisions = Division.getAllDivisions();}
        catch (SQLException e) {throw new RuntimeException(e);}

        customerIdText.setText(String.valueOf(selectedCustomer.getCustomerId()));
        customerNameText.setText(selectedCustomer.getName());
        customerAddressText.setText(selectedCustomer.getAddress());
        customerPostalText.setText(selectedCustomer.getPostal());
        customerPhoneText.setText(selectedCustomer.getPhone());

        //Loops through allDivisions to save only the divisions of the selected country
        for(Division division : allDivisions){
            try { if(division.getCountryId() == Division.getDivision(selectedCustomer.getDivisionId()).getCountryId())
                    selectedDivisions.add(division);}
            catch (SQLException e) {throw new RuntimeException(e);}
        }

        divisionComboBox.setItems(selectedDivisions);
        divisionComboBox.setPromptText("Select a state/province");
        divisionComboBox.setVisibleRowCount(5);

        Division customerDivision;
        try {customerDivision = Division.getDivision(selectedCustomer.getDivisionId());
        } catch (SQLException e) {throw new RuntimeException(e);}

        divisionComboBox.getSelectionModel().select(customerDivision);



        //Loads the Customer's Initial Country
        try {allCountries = Country.getAllCountries();
        } catch (SQLException e) {
            throw new RuntimeException(e);}

        countryComboBox.setItems(allCountries);
        countryComboBox.setPromptText("Select a country");
        countryComboBox.setVisibleRowCount(5);

        Country customerCountry;
        try {customerCountry = Country.getCountry(customerDivision.getCountryId());
        } catch (SQLException e) {
            throw new RuntimeException(e);}


        countryComboBox.getSelectionModel().select(customerCountry);


        DBConnection.closeConnection();

    }

}
