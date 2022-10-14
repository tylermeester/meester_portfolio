package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import model.Appointment;
import model.Contact;
import model.Customer;
import utilities.DBConnection;
import utilities.SceneChanger;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class AddAppointmentController implements Initializable {
    /*-------------------------------------------
    ---------------- ATTRIBUTES------------------
    --------------------------------------------*/
    @FXML
    private TextField appointmentTitleText;
    @FXML
    private TextField appointmentDescriptionText;
    @FXML
    private TextField appointmentLocationText;
    @FXML
    private TextField appointmentTypeText;
    @FXML
    private ComboBox contactNameComboBox;
    @FXML
    private TextField userIdText;
    @FXML
    private TextField customerIdText;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox startTimeComboBox;
    @FXML
    private ComboBox endTimeComboBox;


    /*-------------------------------------------
    ------------------METHODS--------------------
    --------------------------------------------*/
    /**
     * Saves the user inputted appointment information and creates a new appointment in the database. Start Time and
     * End Time are selected using ComboBoxes which display the business's hours of operation (EST 8:00 - 22:00) in
     * the user's local time. Start/End Time entries are checked to ensure Start Time is before End Time and that
     * the customer has no conflicting appointments. Exception handling ensures that database primary key/foreign key
     * values are inputted by the user. Other inputs are permitted to be left blank by the user for situational
     * flexibility.
     *
     * @param actionEvent the ActionEvent initiating the method, clicking on the Save Appointment button.
     * @throws SQLException when accessing the database
     * @throws IOException when calling the SceneChanger.changeScene() method
     */
    public void onActionSaveAppointment(ActionEvent actionEvent) throws SQLException, IOException {

        DBConnection.openConnection();

        try {

            //Saving the new attributes
            String appointmentTitle = appointmentTitleText.getText();
            String appointmentDescription = appointmentDescriptionText.getText();
            String appointmentLocation = appointmentLocationText.getText();
            String appointmentType = appointmentTypeText.getText();
            LocalDate selectedDate = datePicker.getValue();
            int customerId = Integer.parseInt(customerIdText.getText());
            int userId = Integer.parseInt(userIdText.getText());

            //Get the contactId for the contact name that is selected
            String contactString = contactNameComboBox.getSelectionModel().getSelectedItem().toString();
            int contactId = 0;
            for (Contact contact : Contact.getAllContacts()) {
                if (contact.getName().contains(contactString)) {
                    contactId = contact.getId();
                }
            }

            //get the customer's appointment times
            Customer selectedCustomer = Customer.getCustomer(customerId);
            ObservableList<Appointment> allAppointments = Appointment.getAllAppointments();
            ObservableList<Appointment> customerAppointments = FXCollections.observableArrayList();
            ObservableList<Appointment> conflictingAppointments = FXCollections.observableArrayList();

            for (Appointment appointment : allAppointments) {
                if ((appointment.getCustomerId() == customerId))
                    customerAppointments.add(appointment);
            }

            //User's local ZoneId
            ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());

            //Converting combobox entry for startTime to Instant for database entry
            LocalTime localSelectedStartTime = (LocalTime) startTimeComboBox.getSelectionModel().getSelectedItem();
            ZonedDateTime localStartTimeZDT = ZonedDateTime.of(selectedDate, localSelectedStartTime, localZoneId);
            Instant instantStartTime = localStartTimeZDT.toInstant();

            //Converting combobox entry for endTime to Instant for database entry
            LocalTime localSelectedEndTime = (LocalTime) endTimeComboBox.getSelectionModel().getSelectedItem();
            ZonedDateTime localEndTimeZDT = ZonedDateTime.of(selectedDate, localSelectedEndTime, localZoneId);
            Instant instantEndTime = localEndTimeZDT.toInstant();

            LocalDateTime newStart = localStartTimeZDT.toLocalDateTime();
            LocalDateTime newEnd = localEndTimeZDT.toLocalDateTime();

            //Testing for conflicting appointments
            for (Appointment existingAppointment : customerAppointments) {

                LocalDateTime existingStart = existingAppointment.getStartTimeLDT();
                LocalDateTime existingEnd = existingAppointment.getEndTimeLDT();

                //if the new appointment start >= existing appointment start && new appointment start < existing appointment end
                if ((newStart.equals(existingStart) || newStart.isAfter(existingStart)) && (newStart.isBefore(existingEnd))) {
                    conflictingAppointments.add(existingAppointment);
                }

                //if the new appointment end > existing appointment start && new appointment end <= existing appointment end
                else if ((newEnd.isAfter(existingStart)) && ((newEnd.isBefore(existingEnd)) || (newEnd.equals(existingEnd)))) {
                    conflictingAppointments.add(existingAppointment);
                }

                //if the new appointment start <= existing appointment start && new appointment end >= existing appointment end
                else if ((newStart.isBefore(existingStart) || newStart.equals(existingStart)) && ((newEnd.isAfter(existingEnd)) || (newEnd.equals(existingEnd)))) {
                    conflictingAppointments.add(existingAppointment);
                }
            }

            //check if this appointment time interferes
            if (conflictingAppointments.isEmpty() && newStart.isBefore(newEnd)){
                //Update appointment
                Appointment.addAppointment(appointmentTitle, appointmentDescription, appointmentLocation, appointmentType,
                        instantStartTime, instantEndTime, customerId, userId, contactId);

                //Change scenes
                SceneChanger.changeScene("Homepage.fxml", actionEvent);
            }


            else if(conflictingAppointments.isEmpty() && !newStart.isBefore(newEnd)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("The start time must be before the end time!");
                alert.showAndWait();
            }

            else if (!conflictingAppointments.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("The customer already has an appointment scheduled during that time!");
                alert.showAndWait();
            }
        }

        catch(NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please enter a valid value in each text field!");
            alert.showAndWait();
        }

        DBConnection.closeConnection();
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
     * Initializes the Add Appointment page and populates the form with the relevant ComboBox data. The User ComboBox
     * is populated with all Users in the database, and the Start/End Time ComboBoxes are populated with the business's
     * hours of operation (EST 8:00 - 22:00) in the user's local time.
     *
     * @param url URL
     * @param resourceBundle ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Getting the system's current date to be able to use ZonedDateTime
        LocalDate currentDate = LocalDate.now();

        //ZoneId of business time zone
        ZoneId businessZoneId = ZoneId.of("America/New_York");

        //LocalTime objects for business start and end times
        LocalTime businessStart = LocalTime.of(8,0);
        LocalTime businessEnd = LocalTime.of(22, 0);

        //Converting the business start and end times to ZonedDateTime objects by using the business ZoneId
        ZonedDateTime businessStartZDT = ZonedDateTime.of(currentDate, businessStart, businessZoneId);
        ZonedDateTime businessEndZDT = ZonedDateTime.of(currentDate, businessEnd, businessZoneId);

        //Getting the user's local ZoneId
        ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());

        //Converting the ZonedDateTime business Start/End to the user's local ZonedDateTime
        ZonedDateTime localStartZDT = businessStartZDT.withZoneSameInstant(localZoneId);
        ZonedDateTime localEndZDT = businessEndZDT.withZoneSameInstant(localZoneId);

        //Populating startTimeComboBox
        LocalTime startTimeLocal = localStartZDT.toLocalTime();
        LocalTime endTimeLocal = localEndZDT.toLocalTime();

        while(startTimeLocal.isBefore(endTimeLocal.plusSeconds(1))){
            startTimeComboBox.getItems().add(startTimeLocal);
            startTimeLocal = startTimeLocal.plusMinutes(30);
        }

        //Selecting the first ComboBox option
        startTimeComboBox.getSelectionModel().select(0);


        //Populating endTimeComboBox
        startTimeLocal = localStartZDT.toLocalTime();
        endTimeLocal = localEndZDT.toLocalTime();

        while(startTimeLocal.isBefore(endTimeLocal.plusSeconds(1))){
            endTimeComboBox.getItems().add(startTimeLocal);
            startTimeLocal = startTimeLocal.plusMinutes(30);
        }

        //Selecting the second ComboBox option
        endTimeComboBox.getSelectionModel().select(1);

        //Filling the contactNameComboBox with the names of all contacts in the database
        try {
            for(Contact contact : Contact.getAllContacts())
            contactNameComboBox.getItems().add(contact.getName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }


}
