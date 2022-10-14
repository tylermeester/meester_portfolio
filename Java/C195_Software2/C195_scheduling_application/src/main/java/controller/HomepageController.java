package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import model.Appointment;
import model.Customer;
import utilities.DBConnection;
import utilities.DynamicTableview;
import utilities.SceneChanger;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.*;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;

import static controller.ModifyCustomerController.selectedCustomerId;
import static controller.ModifyAppointmentController.selectedAppointmentId;
import static utilities.DBConnection.connection;

public class HomepageController implements Initializable {
    /*-------------------------------------------
    ----------------ATTRIBUTES-------------------
    --------------------------------------------*/

    public TableView homepageTableView;
    @FXML
    private GridPane homepageGridPane;
    @FXML
    private Button addAppointmentButton;
    @FXML
    private Button editAppointmentButton;
    @FXML
    private Button deleteAppointmentButton;
    @FXML
    private Button addCustomerButton;
    @FXML
    private Button editCustomerButton;
    @FXML
    private Button deleteCustomerButton;
    @FXML
    private Label timezoneText;


    /*-------------------------------------------
    -----------RADIO BUTTON CONTROLS-------------
    --------------------------------------------*/
    /**
     * Loads all the Appointments in the homepageTableView, sorted chronologically in ascending order by utilizing
     * the DynamicTableView.generateTableView() method. Loads the relevant Customer/Appointment buttons onto the screen.
     *
     * @param actionEvent the ActionEvent initiating the method, clicking on the View All Appointments RadioButton
     * @throws SQLException when accessing the database via the DynamicTableView.generateTableView() method
     */
    public void onActionViewAll(ActionEvent actionEvent) throws SQLException {

        homepageGridPane.getChildren().removeAll(addCustomerButton, editCustomerButton, deleteCustomerButton,
                addAppointmentButton, editAppointmentButton, deleteAppointmentButton);

        homepageGridPane.add(addAppointmentButton, 0, 0, 1, 1);
        homepageGridPane.add(editAppointmentButton, 1, 0, 1, 1);
        homepageGridPane.add(deleteAppointmentButton, 2, 0, 1, 1);

        DBConnection.openConnection();

        homepageTableView.getItems().clear();
        homepageTableView.getColumns().clear();

        String sqlStatement = "SELECT Appointment_ID, Title, Description, Location, Type," +
                "Start, End, Customer_ID, User_ID, Contact_ID " +
                "FROM appointments " +
                "ORDER BY Start ASC";

        DynamicTableview.generateTableView(sqlStatement, homepageTableView);

        DBConnection.closeConnection();
    }

    /**
     * Loads the current months Appointments in the homepageTableView, sorted chronologically in ascending order
     * by utilizing the DynamicTableView.generateTableView() method. Loads the relevant Customer/Appointment
     * buttons onto the screen.
     *
     * @param actionEvent the ActionEvent initiating the method, clicking on the View Monthly Appointments RadioButton
     * @throws SQLException when accessing the database via the DynamicTableView.generateTableView() method
     */
    public void onActionViewMonth(ActionEvent actionEvent) throws SQLException {

        homepageGridPane.getChildren().removeAll(addCustomerButton, editCustomerButton, deleteCustomerButton,
                addAppointmentButton, editAppointmentButton, deleteAppointmentButton);

        homepageGridPane.add(addAppointmentButton, 0, 0, 1, 1);
        homepageGridPane.add(editAppointmentButton, 1, 0, 1, 1);
        homepageGridPane.add(deleteAppointmentButton, 2, 0, 1, 1);


        DBConnection.openConnection();

        //Getting the user's current month as a string
        String string = LocalDate.now().getMonth().toString();

        //Using bind variable and sqlStatement to generate a PreparedStatement to query
        String sqlStatement = "SELECT Appointment_ID, Title, Description, Location, Type," +
                "Start, End, Customer_ID, User_ID, Contact_ID " +
                "FROM appointments " +
                "WHERE monthname(Start) = ? " +
                "ORDER BY Start ASC";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, string);

        //Using overloaded generateTableView method to populate the homepageTableView with Appointment data
        DynamicTableview.generateTableView(preparedStatement, homepageTableView);


        DBConnection.closeConnection();
    }

    /**
     * Loads the current weeks Appointments in the homepageTableView, sorted chronologically in ascending order
     * by utilizing the DynamicTableView.generateTableView() method. Loads the relevant Customer/Appointment
     * buttons onto the screen.
     *
     * @param actionEvent the ActionEvent initiating the method, clicking on the View Weekly Appointments RadioButton
     * @throws SQLException when accessing the database via the DynamicTableView.generateTableView() method
     */
    public void onActionViewWeek(ActionEvent actionEvent) throws SQLException {

        homepageGridPane.getChildren().removeAll(addCustomerButton, editCustomerButton, deleteCustomerButton,
                addAppointmentButton, editAppointmentButton, deleteAppointmentButton);

        homepageGridPane.add(addAppointmentButton, 0, 0, 1, 1);
        homepageGridPane.add(editAppointmentButton, 1, 0, 1, 1);
        homepageGridPane.add(deleteAppointmentButton, 2, 0, 1, 1);

        DBConnection.openConnection();

        //Using bind variable and sqlStatement to generate a PreparedStatement to query
        String sqlStatement = "SELECT Appointment_ID, Title, Description, Location, Type," +
                "Start, End, Customer_ID, User_ID, Contact_ID " +
                "FROM appointments " +
                "WHERE YEARWEEK(Start) = YEARWEEK(NOW()) " +
                "ORDER BY Start ASC";

        DynamicTableview.generateTableView(sqlStatement, homepageTableView);


        DBConnection.closeConnection();


    }

    /**
     * Loads all the Customers in the homepageTableView by utilizing the DynamicTableView.generateTableView() method.
     * Loads the relevant Customer/Appointment buttons onto the screen.
     *
     * @param actionEvent the ActionEvent initiating the method, clicking on the View All Customers RadioButton
     * @throws SQLException when accessing the database via the DynamicTableView.generateTableView() method
     */
    public void onActionViewCustomers(ActionEvent actionEvent) throws SQLException {

        homepageGridPane.getChildren().removeAll(addCustomerButton, editCustomerButton, deleteCustomerButton,
                addAppointmentButton, editAppointmentButton, deleteAppointmentButton);

        homepageGridPane.add(addCustomerButton, 0, 0, 1, 1);
        homepageGridPane.add(editCustomerButton, 1, 0, 1, 1);
        homepageGridPane.add(deleteCustomerButton, 2, 0, 1, 1);


        DBConnection.openConnection();

        String sqlStatement = "SELECT Customer_ID, Customer_Name, Address, Postal_Code, Phone, Division_ID " +
                "FROM customers " +
                "ORDER BY Customer_ID ASC";

        DynamicTableview.generateTableView(sqlStatement, homepageTableView);

        DBConnection.closeConnection();

    }


    /*-------------------------------------------
    ------------APPOINTMENT CONTROLS-------------
    --------------------------------------------*/

    /**
     * Directs the user to the AddAppointment page.
     *
     * @param actionEvent the ActionEvent initiating the method, clicking the Add Appointment button
     * @throws IOException when calling the SceneChanges.changeScene() method
     */
    public void onActionAddAppointment(ActionEvent actionEvent) throws IOException {
        SceneChanger.changeScene("AddAppointment.fxml", actionEvent);
    }

    /**
     * Directs the user to the ModifyAppointment page for the selected Appointment in the homepageTableView.
     * Saves the Appointment that is selected in the homepageTableView to the public static attributes,
     * 'selectedAppointment' and 'selectedAppointmentId' (which exist in the ModifyAppointment controller)
     * for later access. Includes exception handling for situations where the user has not selected an appointment
     * in the homepageTableView.
     *
     * @param actionEvent the ActionEvent initiating the method, clicking the Edit Appointment button
     * @throws IOException when calling the SceneChanges.changeScene() method
     */
    public void onActionEditAppointment(ActionEvent actionEvent) throws IOException, SQLException {

        String appointmentString = null;
        String[] appointmentStringArray;
        int appointmentId = 0;

        try {
            appointmentString = homepageTableView.getSelectionModel().getSelectedItem().toString();
            appointmentStringArray = appointmentString.split(",");
            appointmentId = Integer.parseInt(appointmentStringArray[0].substring(1));

            //Changing the public static attributes (selectedAppointmentId and selectedAppointment) to be able to access
            // the selected object in the ModifyAppointment controller
            selectedAppointmentId = appointmentId;
            ModifyAppointmentController.selectedAppointment = Appointment.getAppointment(selectedAppointmentId);

            SceneChanger.changeScene("ModifyAppointment.fxml", actionEvent);}

        catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please select an appointment to edit!");
            alert.showAndWait();}
    }

    /**
     * Deletes the selected Appointment from the database, utilizing a confirmation alert and presenting a
     * message to the user upon successful deletion of the appointment.
     *
     * @param actionEvent the ActionEvent initiating the method, clicking the Delete Appointment button
     * @throws IOException when calling the SceneChanger.changeScene() method
     */
    public void onActionDeleteAppointment(ActionEvent actionEvent) throws SQLException, IOException {

        DBConnection.openConnection();

        ObservableList<Appointment> allAppointments = Appointment.getAllAppointments();

        String appointmentString = null;
        String[] appointmentStringArray;
        int appointmentId = 0;


        try {
            appointmentString = homepageTableView.getSelectionModel().getSelectedItem().toString();
            appointmentStringArray = appointmentString.split(",");
            appointmentId = Integer.parseInt(appointmentStringArray[0].substring(1));
            Appointment appointmentToDelete = Appointment.getAppointment(appointmentId);
            int customerId = appointmentToDelete.getCustomerId();
            Customer customer = Customer.getCustomer(customerId);
            String customerName = customer.getName();


            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to delete " + customerName + "'s appointment?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Appointment.removeAppointment(appointmentId);

                Alert information = new Alert(Alert.AlertType.INFORMATION, "The " + appointmentToDelete.getType() +
                        " appointment (#" + appointmentId + ") was successfully deleted.");

                Optional<ButtonType> result2 = information.showAndWait();

                DBConnection.closeConnection();

                SceneChanger.changeScene("Homepage.fxml", actionEvent);
            }

        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please select an appointment to delete!");
            alert.showAndWait();
        }
    }
    

    /*-------------------------------------------
    --------------CUSTOMER CONTROLS--------------
    --------------------------------------------*/
    /**
     * Directs the user to the AddCustomer page.
     *
     * @param actionEvent the ActionEvent initiating the method, clicking the Add Customer button
     * @throws IOException when calling the SceneChanger.changeScene() method
     */
    public void onActionAddCustomer(ActionEvent actionEvent) throws IOException {

        SceneChanger.changeScene("AddCustomer.fxml", actionEvent);
    }

    /**
     * Directs the user to the ModifyCustomer page for the selected Customer in the homepageTableView.
     * Saves the Customer that is selected in the homepageTableView to the public static attributes, 'selectedCustomer'
     * and 'selectedCustomerId' (which exist in the ModifyCustomer controller) for later access.
     * Includes exception handling for situations where the user has not selected a customer in the
     * homepageTableView.
     *
     * @param actionEvent the ActionEvent initiating the method, clicking the Edit Customer button
     * @throws IOException when calling the SceneChanger.changeScene() method
     */
    public void onActionEditCustomer(ActionEvent actionEvent) throws IOException, SQLException {

        String customerString = null;
        String[] customerStringArray;
        int customerId = 0;

        try {
            customerString = homepageTableView.getSelectionModel().getSelectedItem().toString();
            customerStringArray = customerString.split(",");
            customerId = Integer.parseInt(customerStringArray[0].substring(1));

            //Changing the public static attributes (selectedCustomerId and selectedCustomer) to be able to access
            // the selected object in the ModifyCustomer controller
            selectedCustomerId = customerId;
            ModifyCustomerController.selectedCustomer = Customer.getCustomer(selectedCustomerId);
            SceneChanger.changeScene("ModifyCustomer.fxml", actionEvent);


        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please select a customer to edit!");
            alert.showAndWait();
        }
    }

    /**
     * Deletes the selected Customer from the database, utilizing a confirmation alert and presenting a
     * message to the user upon successful deletion of the Customer. Includes exception handling for situations
     * where the user has not selected a Customer in the homepageTableView and situations where
     * the selected Customer has Appointments in the database, as per the database requirements.
     *
     * @param actionEvent the ActionEvent initiating the method, clicking the Delete Customer button
     * @throws IOException when calling the SceneChanger.changeScene() method
     */
    public void onActionDeleteCustomer(ActionEvent actionEvent) throws SQLException, IOException {

        DBConnection.openConnection();

        String customerString = null;
        String[] customerStringArray;
        int customerId = 0;

        try {
            customerString = homepageTableView.getSelectionModel().getSelectedItem().toString();
            customerStringArray = customerString.split(",");
            customerId = Integer.parseInt(customerStringArray[0].substring(1));
            ObservableList<Appointment> allAppointments = Appointment.getAllAppointments();
            ObservableList<Appointment> customerAppointments = FXCollections.observableArrayList();

            for (Appointment appointment : allAppointments) {
                if (appointment.getCustomerId() == customerId)
                    customerAppointments.add(appointment);}

            if (!customerAppointments.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Remove all customer appointments before deleting this customer!");
                alert.showAndWait();}

            else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this customer?");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {
                    Customer.removeCustomer(customerId);
                    Alert information = new Alert(Alert.AlertType.INFORMATION, "Customer #" + customerId + " successfully deleted!");
                    Optional<ButtonType> result2 = information.showAndWait();
                }

                SceneChanger.changeScene("Homepage.fxml", actionEvent);
            }

        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please select a customer to delete!");
            alert.showAndWait();
        }

        DBConnection.closeConnection();

    }


    /*-------------------------------------------
    -------------------REPORTS ------------------
    --------------------------------------------*/

    /**
     * Directs the user to the Reports page.
     *
     * @param actionEvent the ActionEvent initiating the method, clicking the Reports button
     * @throws IOException when calling the SceneChanger.changeScene() method
     */
    public void onActionReports(ActionEvent actionEvent) throws IOException {
        SceneChanger.changeScene("Reports.fxml", actionEvent);
    }


    /*-------------------------------------------
    ---------------EXIT PROGRAM -----------------
    --------------------------------------------*/
    /**
     * Exits the program.
     *
     * @param actionEvent the ActionEvent initiating the method, clicking the Exit button
     */
    public void onActionExit(ActionEvent actionEvent) {
        System.exit(0);
    }

    /**
     * Logs the user out of the program and presents them with the Login screen.
     *
     * @param actionEvent the ActionEvent initiating the method, clicking the Logout button
     * @throws IOException when calling the SceneChanger.changeScene() method
     */
    public void onActionLogout(ActionEvent actionEvent) throws IOException {
        SceneChanger.changeScene("Login.fxml", actionEvent);
    }


    /*-------------------------------------------
    -----------------INITIALIZE------------------
    --------------------------------------------*/

    /**
     * Initializes the Homepage and populates the homepageTableView with all appointments in the database. Determines
     * the user's timezone and provides their local timezone and time.
     *
     * @param url URL
     * @param resourceBundle ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        homepageTableView.getItems().clear();
        homepageTableView.getColumns().clear();
        homepageGridPane.getChildren().removeAll(addCustomerButton, editCustomerButton, deleteCustomerButton);

        //Displaying the local time
        ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
        String localTimeString = String.valueOf(LocalTime.now().truncatedTo(ChronoUnit.MINUTES));
        timezoneText.setText("Current Timezone: "+ localZoneId.getDisplayName(TextStyle.FULL, Locale.getDefault()) + "\n" +
                            "Current Time: " + localTimeString);


        DBConnection.openConnection();

        String sqlStatement = "SELECT Appointment_ID, Title, Description, Location, Type," +
                "Start, End, Customer_ID, User_ID, Contact_ID " +
                "FROM appointments " +
                "ORDER BY Start ASC" ;

        try {
            DynamicTableview.generateTableView(sqlStatement, homepageTableView);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



        DBConnection.closeConnection();
    }

}







