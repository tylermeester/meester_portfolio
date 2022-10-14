package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.Appointment;
import model.User;
import utilities.SceneChanger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class LoginController implements Initializable {
    /*-------------------------------------------
    ---------------- ATTRIBUTES------------------
    --------------------------------------------*/
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;
    @FXML
    private Label userNameLabel;
    @FXML
    private Label passwordLabel;
    @FXML
    private Button loginButtonLabel;
    @FXML
    private Button exitButtonLabel;
    @FXML
    private ComboBox languageComboBox;
    @FXML
    private Label timeZoneLabel;
    private boolean french = false;

    private String loginAttempt;


    /*-------------------------------------------
    ------------------METHODS--------------------
    --------------------------------------------*/
    /**
     * Checks the validity of the user inputted login information in the database. Displays language appropriate error
     * messages depending on whether the username is invalid or the password is invalid. If the login information
     * is valid, the user is taken to the Homepage screen. Utilizes a lambda expression to efficiently identify whether
     * the local variable, userName, matches any Usernames in the stream of all Users (derived from the ObservableList
     * of Users, allUsers).
     *
     * @param actionEvent the ActionEvent initiating the method, clicking the 'Login' button
     * @throws IOException when calling .load() on the FXML loader object via the SceneChange.changeScenes() method
     * @throws SQLException when accessing the database via the SceneChanger.changeScenes() method
     */
    public void onActionLogin(ActionEvent actionEvent) throws IOException, SQLException {

        String filename = "login_activity.txt", loginAttempt;
        FileWriter fileWriter = new FileWriter(filename, true);
        PrintWriter outputFile = new PrintWriter(fileWriter);

        ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
        LocalTime localTime = LocalTime.now();
        LocalDate localDate = LocalDate.now();
        ZonedDateTime localStartTimeZDT = ZonedDateTime.of(localDate, localTime, localZoneId);
        Instant instantTime = localStartTimeZDT.toInstant();

        //Gets the text from the textFields and instantiates a User object for later use
        ResourceBundle frenchResourceBundle = ResourceBundle.getBundle("bundle/login", Locale.FRENCH);
        String userName = usernameTextField.getText();
        String password = passwordTextField.getText();
        User loggedInUser;

        //Instantiates observable array lists of allUsernames and allPasswords
        ObservableList<String> allUsernames = FXCollections.observableArrayList();
        ObservableList<String> allPasswords = FXCollections.observableArrayList();

        //Loops through all Users and adds each user's userName and password to the respective observable array list
        for (User user : User.getAllUsers()) {
            allUsernames.add(user.getUserName());
            allPasswords.add(user.getPassword());}

        //Looks for the entered username/password combo, and displays a custom information alert with
        //Upcoming user appointments if there are any appointments within 15 minutes. If there are no
        //upcoming appointments, a default message is displayed
        if((allUsernames.stream().anyMatch(u -> u.equals(userName))) //LAMBDA EXPRESSION
                && (User.getUser(userName).getPassword().equals(password))){

            loggedInUser = User.getUser(userName);
            SceneChanger.changeScene("Homepage.fxml", actionEvent);

            //Adding attempt to the "login_activity.txt" file
            loginAttempt = "USERNAME: " +  userName + ", PASSWORD: " + password + ", UTC TIMESTAMP: " + instantTime +
                    ", STATUS: login success";
            outputFile.println(loginAttempt);
            outputFile.close();

            ObservableList<Appointment> allAppointments = null;
            ObservableList<Appointment> upcomingAppointments = FXCollections.observableArrayList();

            try {allAppointments = Appointment.getAllAppointments();
            } catch (SQLException e) {throw new RuntimeException(e);
            }

            //Loops through allAppointments and identifies user appointments within 15 minutes of the user login
            for(Appointment appointment : allAppointments){

                Instant appointmentStart = appointment.getStartTimeUTC();
                int appointmentUserId = appointment.getUserId();


                if(((instantTime.until(appointmentStart, ChronoUnit.MINUTES) <= 15) &&  (instantTime.until(appointmentStart, ChronoUnit.MINUTES) > 0)) &&
                        (appointmentUserId == loggedInUser.getId())){
                    upcomingAppointments.add(appointment);
                }
            }

            if(french){
                String appointmentNoticeString = frenchResourceBundle.getString("youHave") +
                        upcomingAppointments.size() +
                        frenchResourceBundle.getString("appointmentsIn15") +
                        "\n \n";

                //Loops through all upcomingAppointments and adds the ID, Date,and Start Time to the appointmentNoticeString
                for (Appointment appointment : upcomingAppointments) {
                    String stringToAdd = frenchResourceBundle.getString("appointmentId") + appointment.getId() + "\n" +
                            frenchResourceBundle.getString("appointmentTime") + appointment.getStartTimeLDT().toLocalTime() + "\n" +
                            frenchResourceBundle.getString("appointmentDate") + appointment.getStartTimeLDT().toLocalDate() + "\n \n";

                    appointmentNoticeString = appointmentNoticeString + stringToAdd;
                }

                //If upcomingAppointments is not empty, display the appointmentNoticeString with all upcoming appointments
                if (!upcomingAppointments.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, appointmentNoticeString);
                    alert.setTitle("Information");
                    alert.setHeaderText(frenchResourceBundle.getString("upcomingAppointments"));
                    Optional<ButtonType> result = alert.showAndWait();
                }

            }
            else {
                //Creating a string, appointmentNoticeString, to display in the upcoming appointment information alert
                String appointmentNoticeString = "You have " + upcomingAppointments.size() + " appointment(s) within 15 minutes: \n \n";

                //Loops through all upcomingAppointments and adds the ID, Date,and Start Time to the appointmentNoticeString
                for (Appointment appointment : upcomingAppointments) {
                    String stringToAdd = "Appointment ID: " + appointment.getId() + "\n" +
                            "Appointment Time: " + appointment.getStartTimeLDT().toLocalTime() + "\n" +
                            "Appointment Date: " + appointment.getStartTimeLDT().toLocalDate() + "\n \n";

                    appointmentNoticeString = appointmentNoticeString + stringToAdd;
                }

                //If upcomingAppointments is not empty, display the appointmentNoticeString with all upcoming appointments
                if (!upcomingAppointments.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, appointmentNoticeString);
                    alert.setTitle("Information");
                    alert.setHeaderText("Upcoming Appointments!");
                    Optional<ButtonType> result = alert.showAndWait();
                }
            }


            //If upcomingAppointments is empty, display a default message
            if (upcomingAppointments.isEmpty()){
                if(french){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, frenchResourceBundle.getString("noUpcomingAppointments"));
                    alert.setTitle("Information");
                    alert.setHeaderText("Information");
                    Optional<ButtonType> result = alert.showAndWait();
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "You do not have any upcoming appointments.");
                    alert.setTitle("Information");
                    alert.setHeaderText("Information");
                    Optional<ButtonType> result = alert.showAndWait();}
                }

        }

        //If the username is recognized, but the password is not recognized, display an error alert and add the
        //entered login information into a text document
        else if ((allUsernames.stream().anyMatch(u -> u.equals(userName)))
                && !(User.getUser(userName).getPassword().equals(password))){

            //Adding attempt to the "login_activity.txt" file
            loginAttempt = "USERNAME: " +  userName + ", PASSWORD: " + password + ", UTC TIMESTAMP: " + instantTime +
                    ", STATUS: login failed";
            outputFile.println(loginAttempt);
            outputFile.close();


            if(french){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(frenchResourceBundle.getString("errorDialog"));
                alert.setHeaderText(frenchResourceBundle.getString("errorDialog"));
                alert.setContentText(frenchResourceBundle.getString("invalidPassword"));
                alert.showAndWait();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Invalid Password!");
                alert.showAndWait();
            }
        }

        //If the username is not recognized display an error alert and add the
        // entered login information into a text document
        else if(!(allUsernames.stream().anyMatch(u -> u.equals(userName)))){

            //Adding attempt to the "login_activity.txt" file
            loginAttempt = "USERNAME: " +  userName + ", PASSWORD: " + password + ", UTC TIMESTAMP: " + instantTime +
                    ", STATUS: login failed";
            outputFile.println(loginAttempt);
            outputFile.close();

            if(french){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle(frenchResourceBundle.getString("errorDialog"));
                alert.setHeaderText(frenchResourceBundle.getString("errorDialog"));
                alert.setContentText(frenchResourceBundle.getString("invalidUsername"));
                alert.showAndWait();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Username not found!");
                alert.showAndWait();
            }
        }
    }

    /**
     * Exits the application
     *
     * @param actionEvent the ActionEvent initiating the method, clicking the 'Exit' button
     */
    public void onActionExit(ActionEvent actionEvent) {
        System.exit(0);
    }

    /**
     * Changes the language on the login screen between English and French.
     *
     * @param actionEvent the ActionEvent initiating the method, clicking an option in the languageComboBox
     */
    public void onActionLanguage(ActionEvent actionEvent) {

        String selectedLanguage = (String) languageComboBox.getSelectionModel().getSelectedItem();

        if(selectedLanguage.equals("French")){
            french = true;
            ResourceBundle frenchResourceBundle = ResourceBundle.getBundle("bundle/login", Locale.FRENCH);
            userNameLabel.setText(frenchResourceBundle.getString("userNameLabel"));
            passwordLabel.setText(frenchResourceBundle.getString("passwordLabel"));
            loginButtonLabel.setText(frenchResourceBundle.getString("loginButtonLabel"));
            exitButtonLabel.setText(frenchResourceBundle.getString("exitButtonLabel"));
            languageComboBox.setPromptText(frenchResourceBundle.getString("languageComboBoxLabel"));
        }
        else if(selectedLanguage.equals("English")){
            french = false;
            ResourceBundle englishResourceBundle = ResourceBundle.getBundle("bundle/login", Locale.ENGLISH);
            userNameLabel.setText(englishResourceBundle.getString("userNameLabel"));
            passwordLabel.setText(englishResourceBundle.getString("passwordLabel"));
            loginButtonLabel.setText(englishResourceBundle.getString("loginButtonLabel"));
            exitButtonLabel.setText(englishResourceBundle.getString("exitButtonLabel"));
            languageComboBox.setPromptText(englishResourceBundle.getString("languageComboBoxLabel"));
        }
    }


    /*-------------------------------------------
    -----------------INITIALIZE------------------
    --------------------------------------------*/

    /**
     * Initializes the login page. Populates the languageComboBox, saves the user's Locale for language and timezone
     * purposes, loads the language resource bundles, and sets the language and timezone  on the page accordingly.
     *
     * @param url URL
     * @param resourceBundle ResourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ZoneId localZoneId = ZoneId.of(TimeZone.getDefault().getID());
        timeZoneLabel.setText(localZoneId.getDisplayName(TextStyle.FULL, Locale.getDefault()));

        ResourceBundle frenchResourceBundle = ResourceBundle.getBundle("bundle/login", Locale.FRENCH);
        ResourceBundle englishResourceBundle = ResourceBundle.getBundle("bundle/login", Locale.ENGLISH);

        languageComboBox.getItems().add("English");
        languageComboBox.getItems().add("French");

        Locale userLocale = Locale.getDefault();

        if(userLocale.getLanguage().equals("fr")){
            languageComboBox.getSelectionModel().select(1);
            french = true;
            userNameLabel.setText(frenchResourceBundle.getString("userNameLabel"));
            passwordLabel.setText(frenchResourceBundle.getString("passwordLabel"));
            loginButtonLabel.setText(frenchResourceBundle.getString("loginButtonLabel"));
            exitButtonLabel.setText(frenchResourceBundle.getString("exitButtonLabel"));
            languageComboBox.setPromptText(frenchResourceBundle.getString("languageComboBoxLabel"));
            timeZoneLabel.setText(localZoneId.getDisplayName(TextStyle.FULL, Locale.FRENCH));
        }
        else{
            languageComboBox.getSelectionModel().select(0);
            french = false;
            userNameLabel.setText(englishResourceBundle.getString("userNameLabel"));
            passwordLabel.setText(englishResourceBundle.getString("passwordLabel"));
            loginButtonLabel.setText(englishResourceBundle.getString("loginButtonLabel"));
            exitButtonLabel.setText(englishResourceBundle.getString("exitButtonLabel"));
            languageComboBox.setPromptText(englishResourceBundle.getString("languageComboBoxLabel"));
            timeZoneLabel.setText(localZoneId.getDisplayName(TextStyle.FULL, Locale.ENGLISH));

        }


    }


}
