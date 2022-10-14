package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import model.Appointment;
import model.Contact;
import utilities.DBConnection;
import utilities.DynamicTableview;
import utilities.SceneChanger;

import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormatSymbols;
import java.util.ResourceBundle;

import static utilities.DBConnection.connection;

public class ReportsController implements Initializable {
    /*-------------------------------------------
    -----------------ATTRIBUTES------------------
    --------------------------------------------*/
    @FXML
    private TableView reportTableView;
    @FXML
    private GridPane reportGridPane;
    @FXML
    private Label appointmentCountLabel;
    @FXML
    private ComboBox<String> locationComboBox;
    @FXML
    private ComboBox<String> monthComboBox;
    @FXML
    private ComboBox<String> typeComboBox;
    @FXML
    private ComboBox<String> contactComboBox;


    /*-------------------------------------------
    -----------RADIO BUTTON CONTROLS-------------
    --------------------------------------------*/

    /**
     * Loads the monthComboBox for the user to select from and hides the other report Combo Boxes.
     *
     * @param actionEvent the ActionEvent that initiates the method, selecting the View By Month radio button
     */
    public void onActionViewMonthRB(ActionEvent actionEvent) {

        DBConnection.openConnection();

        reportTableView.getItems().clear();
        reportTableView.getColumns().clear();
        reportGridPane.getChildren().removeAll(monthComboBox, locationComboBox, typeComboBox, contactComboBox);
        reportGridPane.getChildren().add(monthComboBox);


        String sqlStatement = "SELECT Appointment_ID, Title, Description, Location, Type," +
                "Start, End, Customer_ID, User_ID, Contact_ID " +
                "FROM appointments " +
                "ORDER BY Start ASC";

        try {
            DynamicTableview.generateTableView(sqlStatement, reportTableView);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        appointmentCountLabel.setText("Total appointment count: " + (reportTableView.getItems().size()));

        DBConnection.closeConnection();

    }

    /**
     * Loads the locationComboBox for the user to select from and hides the other report Combo Boxes.
     *
     * @param actionEvent the ActionEvent that initiates the method, selecting the View By Location radio button
     */
    public void onActionViewLocationRB(ActionEvent actionEvent) throws RuntimeException {

        DBConnection.openConnection();

        reportTableView.getItems().clear();
        reportTableView.getColumns().clear();
        reportGridPane.getChildren().removeAll(monthComboBox, locationComboBox, typeComboBox, contactComboBox);
        reportGridPane.getChildren().add(locationComboBox);

        String sqlStatement = "SELECT Appointment_ID, Title, Description, Location, Type," +
                "Start, End, Customer_ID, User_ID, Contact_ID " +
                "FROM appointments " +
                "ORDER BY Start ASC";

        try {
            DynamicTableview.generateTableView(sqlStatement, reportTableView);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        appointmentCountLabel.setText("Total appointment count: " + (reportTableView.getItems().size()));

        DBConnection.closeConnection();


    }

    /**
     * Loads the typeComboBox for the user to select from and hides the other report Combo Boxes.
     *
     * @param actionEvent the ActionEvent that initiates the method, selecting the View By Type radio button
     */
    public void onActionViewTypeRB(ActionEvent actionEvent) {

        DBConnection.openConnection();

        reportTableView.getItems().clear();
        reportTableView.getColumns().clear();
        reportGridPane.getChildren().removeAll(monthComboBox, locationComboBox, typeComboBox, contactComboBox);
        reportGridPane.getChildren().add(typeComboBox);

        String sqlStatement = "SELECT Appointment_ID, Title, Description, Location, Type," +
                "Start, End, Customer_ID, User_ID, Contact_ID " +
                "FROM appointments " +
                "ORDER BY Start ASC";

        try {
            DynamicTableview.generateTableView(sqlStatement, reportTableView);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        appointmentCountLabel.setText("Total appointment count: " + (reportTableView.getItems().size()));

        DBConnection.closeConnection();


    }

    /**
     * Loads the contactComboBox for the user to select from and hides the other report Combo Boxes.
     *
     * @param actionEvent the ActionEvent that initiates the method, selecting the View By Contact radio button
     */
    public void onActionViewContactRB(ActionEvent actionEvent) {

        DBConnection.openConnection();

        reportTableView.getItems().clear();
        reportTableView.getColumns().clear();
        reportGridPane.getChildren().removeAll(monthComboBox, locationComboBox, typeComboBox, contactComboBox);
        reportGridPane.getChildren().add(contactComboBox);

        String sqlStatement = "SELECT Appointment_ID, Title, Description, Location, Type," +
                "Start, End, Customer_ID, User_ID, Contact_ID " +
                "FROM appointments " +
                "ORDER BY Start ASC";

        try {
            DynamicTableview.generateTableView(sqlStatement, reportTableView);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        appointmentCountLabel.setText("Total appointment count: " + (reportTableView.getItems().size()));

        DBConnection.closeConnection();

    }


    /*-------------------------------------------
    -------------COMBO-BOX CONTROLS--------------
    --------------------------------------------*/
    /**
     * Queries the database for all appointments that occur in the month selected by the user.
     *
     * @param actionEvent the ActionEvent that initiates the method, making a selection from the monthComboBox
     * @throws SQLException when accessing the database
     */
    public void onActionSelectMonth(ActionEvent actionEvent) throws SQLException {


        String selectedMonth = monthComboBox.getSelectionModel().getSelectedItem().toString();

        String sqlStatement = "SELECT Appointment_ID, Title, Description, Location, Type," +
                "Start, End, Customer_ID, User_ID, Contact_ID " +
                "FROM appointments " +
                "WHERE monthname(Start)= ? " +
                "ORDER BY Start ASC";

        DBConnection.openConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, selectedMonth);

        DynamicTableview.generateTableView(preparedStatement, reportTableView);

        appointmentCountLabel.setText(selectedMonth +  " appointment count: " + (reportTableView.getItems().size()));

        DBConnection.closeConnection();
    }

    /**
     * Queries the database for all appointments that occur in the location selected by the user.
     *
     * @param actionEvent the ActionEvent that initiates the method, making a selection from the locationComboBox
     * @throws SQLException when accessing the database
     */
    public void onActionSelectLocation(ActionEvent actionEvent) throws SQLException {

        String selectedLocation = locationComboBox.getSelectionModel().getSelectedItem().toString();

        String sqlStatement = "SELECT Appointment_ID, Title, Description, Location, Type," +
                "Start, End, Customer_ID, User_ID, Contact_ID " +
                "FROM appointments " +
                "WHERE Location = ? " +
                "ORDER BY Start ASC";

        DBConnection.openConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, selectedLocation);

        DynamicTableview.generateTableView(preparedStatement, reportTableView);

        appointmentCountLabel.setText(selectedLocation +  " appointment count: " + (reportTableView.getItems().size()));


        DBConnection.closeConnection();
    }

    /**
     * Queries the database for all appointments of the type selected by the user.
     *
     * @param actionEvent the ActionEvent that initiates the method, making a selection from the typeComboBox
     * @throws SQLException when accessing the database
     */
    public void onActionSelectType(ActionEvent actionEvent) throws SQLException {

        String selectedType = typeComboBox.getSelectionModel().getSelectedItem().toString();

        String sqlStatement = "SELECT Appointment_ID, Title, Description, Location, Type," +
                "Start, End, Customer_ID, User_ID, Contact_ID " +
                "FROM appointments " +
                "WHERE Type = ? " +
                "ORDER BY Start ASC";

        DBConnection.openConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, selectedType);

        DynamicTableview.generateTableView(preparedStatement, reportTableView);

        appointmentCountLabel.setText(selectedType +  " appointment count: " + (reportTableView.getItems().size()));

        DBConnection.closeConnection();

    }

    /**
     * Queries the database for all appointments that have the user selected contact.
     *
     * @param actionEvent the ActionEvent that initiates the method, making a selection from the contactComboBox
     * @throws SQLException when accessing the database
     */
    public void onActionSelectContact(ActionEvent actionEvent) throws SQLException {

        String selectedContactName = contactComboBox.getSelectionModel().getSelectedItem().toString();

        int selectedContactId = 0;
        ObservableList<Contact> allContacts = Contact.getAllContacts();

        for(Contact contact : allContacts){
            if(selectedContactName.contains(contact.getName()))
                selectedContactId = contact.getId();
        }


        String sqlStatement = "SELECT Appointment_ID, Title, Description, Location, Type," +
                "Start, End, Customer_ID, User_ID, Contact_ID " +
                "FROM appointments " +
                "WHERE Contact_ID = ? " +
                "ORDER BY Start ASC";

        DBConnection.openConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, String.valueOf(selectedContactId));

        DynamicTableview.generateTableView(preparedStatement, reportTableView);

        appointmentCountLabel.setText(selectedContactName +  " appointment count: " + (reportTableView.getItems().size()));

        DBConnection.closeConnection();
    }


    /*-------------------------------------------
    -----------------HOMEPAGE--------------------
    --------------------------------------------*/
    /**
     * Returns the user to the Homepage
     *
     * @param actionEvent the ActionEvent initiating the method, clicking on the Cancel button.
     * @throws IOException when calling the SceneChanger.changeScene() method.
     */
    public void onActionReturnToHomepage(ActionEvent actionEvent) throws IOException {

        SceneChanger.changeScene("Homepage.fxml", actionEvent);
    }


    /*-------------------------------------------
    -----------------INITIALIZE------------------
    --------------------------------------------*/

    /**
     * Initializes the Reports page by generating a default report for all appointments in the database and populates
     * all the ComboBoxes with the relevant data for later selection by the user.
     *
     * @param url URL
     * @param resourceBundle ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        reportTableView.getItems().clear();
        reportTableView.getColumns().clear();
        reportGridPane.getChildren().removeAll(monthComboBox, locationComboBox, typeComboBox, contactComboBox);

        DBConnection.openConnection();

        String sqlStatement = "SELECT Appointment_ID, Title, Description, Location, Type," +
                "Start, End, Customer_ID, User_ID, Contact_ID " +
                "FROM appointments " +
                "ORDER BY Start ASC";

        try {
            DynamicTableview.generateTableView(sqlStatement, reportTableView);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        reportTableView.setSelectionModel(null);


        //Populating month combobox
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] arrayOfMonthNames = dfs.getMonths();
        for (String month : arrayOfMonthNames) {
            monthComboBox.getItems().add(month);
        }


        //Populating Country combo-box
        ObservableList<Appointment> allAppointments = null;
        try {
            allAppointments = Appointment.getAllAppointments();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        ObservableList<String> uniqueLocations = FXCollections.observableArrayList();

        for(Appointment appointment : allAppointments){
            if(!uniqueLocations.contains(appointment.getLocation())){
                uniqueLocations.add(appointment.getLocation());
            }
        }

        for(String location : uniqueLocations){
            locationComboBox.getItems().add(location);
        }



        //Populating type combobox
        ObservableList<String> uniqueTypes = FXCollections.observableArrayList();

        for(Appointment appointment : allAppointments){
            if(!uniqueTypes.contains(appointment.getType())){
                uniqueTypes.add(appointment.getType());
            }
        }

        for(String type : uniqueTypes){
            typeComboBox.getItems().add(type);
        }


        //Populating contact combobox
        try {
            for(Contact contact : Contact.getAllContacts())
                contactComboBox.getItems().add(contact.getName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        appointmentCountLabel.setText("Total appointment count: " + (reportTableView.getItems().size()));



            DBConnection.closeConnection();

    }


}
