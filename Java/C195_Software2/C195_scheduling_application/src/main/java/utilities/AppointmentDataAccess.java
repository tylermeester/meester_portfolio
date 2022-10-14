package utilities;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import utilities.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.*;

import static utilities.DBConnection.connection;

public class AppointmentDataAccess {

    /**
     * Inserts a new Appointment object into the database. Appointment_ID is auto-generated in the database
     * and so is not an input.
     *
     * @param title Title
     * @param description Description
     * @param location Location
     * @param type Type
     * @param startTime Start
     * @param endTime End
     * @param customerId Customer_ID
     * @param userId User_ID
     * @param contactId Contact_ID
     * @return true if successful insert, false if failed insert
     * @throws SQLException when accessing database
     */
    public static boolean insertAppointment(String title, String description, String location, String type,
                                            Instant startTime, Instant endTime, int customerId, int userId,
                                            int contactId) throws SQLException {

        DBConnection.openConnection();

        //Converts the Instant objects, startTime/endTime to Timestamps so that mySQL will accept them
        Timestamp startTimestamp = Timestamp.from(startTime);
        Timestamp endTimestamp = Timestamp.from(endTime);

        String sqlStatement = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, " +
                "Customer_ID, User_ID, Contact_ID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, title);
        preparedStatement.setString(2, description);
        preparedStatement.setString(3, location);
        preparedStatement.setString(4, type);
        preparedStatement.setTimestamp(5, startTimestamp);
        preparedStatement.setTimestamp(6, endTimestamp);
        preparedStatement.setInt(7, customerId);
        preparedStatement.setInt(8, userId);
        preparedStatement.setInt(9, contactId);

        int rowsAffected = preparedStatement.executeUpdate();

        if(rowsAffected > 0){
            System.out.println("Insert Successful");
            DBConnection.closeConnection();
            return true;
        }
        else{
            System.out.println("Insert Failed");
            DBConnection.closeConnection();
            return false;
        }

    }


    /**
     * Updates the Appointment object, referenced by the input 'appointmentId', in the database with the new values.
     *
     * @param appointmentId Appointment_ID
     * @param title Title
     * @param description Description
     * @param location Location
     * @param type Type
     * @param startTime Start
     * @param endTime End
     * @param customerId Customer_ID
     * @param userId User_ID
     * @param contactId Contact_ID
     * @return true if successful update, false if failed update
     * @throws SQLException when accessing database
     */
    public static boolean updateAppointment(int appointmentId, String title, String description, String location,
                                            String type, Instant startTime, Instant endTime, int customerId,
                                            int userId, int contactId) throws SQLException {

        DBConnection.openConnection();

        //Converts the Instant objects, startTime/endTime to Timestamps so that mySQL will accept them
        Timestamp startTimestamp = Timestamp.from(startTime);
        Timestamp endTimestamp = Timestamp.from(endTime);

        String sqlStatement = "UPDATE appointments " +
                "SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?, End = ?, " +
                "Customer_ID = ?, User_ID = ?, Contact_ID = ? " +
                "WHERE Appointment_ID = ? ";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, title);
        preparedStatement.setString(2, description);
        preparedStatement.setString(3, location);
        preparedStatement.setString(4, type);
        preparedStatement.setTimestamp(5, startTimestamp);
        preparedStatement.setTimestamp(6, endTimestamp);
        preparedStatement.setInt(7, customerId);
        preparedStatement.setInt(8, userId);
        preparedStatement.setInt(9, contactId);
        preparedStatement.setInt(10, appointmentId);

        int rowsAffected = preparedStatement.executeUpdate();

        if (rowsAffected > 0) {
            System.out.println("Update Successful");
            DBConnection.closeConnection();
            return true;
        } else {
            System.out.println("Update Failed");
            DBConnection.closeConnection();
            return false;
        }
    }

    /**
     * Deletes the Appointment object, referenced by the input 'appointmentId', from the database.
     *
     * @param appointmentId Appointment_ID
     * @return true if successful delete, false if failed delete
     * @throws SQLException when accessing the database
     */
    public static boolean deleteAppointment(int appointmentId) throws SQLException {

        DBConnection.openConnection();

        String sqlStatement = "DELETE FROM appointments " +
                "WHERE Appointment_ID = ?";

        PreparedStatement preparedStatement = DBConnection.connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, appointmentId);

        int rowsAffected = preparedStatement.executeUpdate();

        DBConnection.closeConnection();

        if(rowsAffected > 0){
            System.out.println("Delete Successful");
            DBConnection.closeConnection();
            return true;
        }
        else {
            System.out.println("Delete Failed");
            DBConnection.closeConnection();
            return false;
        }
    }

    /**
     * Gets the Appointment object, referenced by the input 'appointmentId', from the database.
     *
     * @param appointmentId Appointment_ID
     * @return Appointment object
     * @throws SQLException when accessing the database
     */
    public static Appointment getAppointment(int appointmentId) throws SQLException {

        DBConnection.openConnection();

        String sqlStatement = "SELECT * " +
                "FROM appointments " +
                "WHERE Appointment_ID = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, appointmentId);

        ResultSet result = preparedStatement.executeQuery();

        Appointment appointmentResult = null;

        if(result.next()){
            int appointmentID = result.getInt("Appointment_ID");
            String title = result.getString("Title");
            String description = result.getString("Description");
            String location = result.getString("Location");
            String type = result.getString("Type");
            Timestamp startTimestamp = result.getTimestamp("Start");
            Timestamp endTimestamp = result.getTimestamp("End");
            int customerId = result.getInt("Customer_ID");
            int contactId = result.getInt("Contact_ID");
            int userId = result.getInt("User_ID");

            Instant startInstant = startTimestamp.toInstant();
            Instant endInstant = endTimestamp.toInstant();

            appointmentResult = new Appointment(appointmentID, title, description, location, type, startInstant, endInstant,
                    customerId, contactId, userId);
        }

        DBConnection.closeConnection();

        return appointmentResult;
    }

    /**
     * Gets all Appointments from the database.
     *
     * @return ObservableList of all Appointments in the database
     * @throws SQLException when accessing the database
     */
    public static ObservableList<Appointment> getAllAppointments() throws SQLException {

        ObservableList<Appointment> allDBAppointments = FXCollections.observableArrayList();

        DBConnection.openConnection();

        String sqlStatement = "SELECT * FROM appointments";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);

        ResultSet result = preparedStatement.executeQuery();

        while(result.next()){
            int appointmentID = result.getInt("Appointment_ID");
            String title = result.getString("Title");
            String description = result.getString("Description");
            String location = result.getString("Location");
            String type = result.getString("Type");
            Timestamp startTimestamp = result.getTimestamp("Start");
            Timestamp endTimestamp = result.getTimestamp("End");
            int customerId = result.getInt("Customer_ID");
            int contactId = result.getInt("Contact_ID");
            int userId = result.getInt("User_ID");

            Instant startInstant = startTimestamp.toInstant();
            Instant endInstant = endTimestamp.toInstant();

            Appointment appointmentResult = new Appointment(appointmentID, title, description, location, type, startInstant, endInstant,
                    customerId, contactId, userId);

            allDBAppointments.add(appointmentResult);
        }

        DBConnection.closeConnection();

        return allDBAppointments;
    }










}
