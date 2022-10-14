package model;

import utilities.AppointmentDataAccess;
import javafx.collections.ObservableList;
import utilities.TimeUtility;

import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDateTime;

public class Appointment {
    /*-------------------------------------------
    ----------APPOINTMENT ATTRIBUTES-------------
    --------------------------------------------*/
    private int id;
    private String title;
    private String description;
    private String location;
    private String type;
    private Instant startTime;
    private Instant endTime;
    private int customerId;
    private int contactId;
    private int userId;


    /*-------------------------------------------
    ----------APPOINTMENT CONSTRUCTOR------------
    --------------------------------------------*/
    public Appointment(int id, String title, String description, String location, String type, Instant startTime, Instant endTime,
                       int customerId, int contactId, int userId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.customerId = customerId;
        this.contactId = contactId;
        this.userId = userId;
    }


    /*-------------------------------------------
    ------------APPOINTMENT SETTERS--------------
    --------------------------------------------*/
    public void setId(int id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setStartTimeUTC(Instant startTime) {
        this.startTime = startTime;
    }
    public void setEndTimeUTC(Instant endTime) {
        this.endTime = endTime;
    }
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /*-------------------------------------------
    ------------APPOINTMENT GETTERS--------------
    --------------------------------------------*/
    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getLocation() {
        return location;
    }
    public String getType() {
        return type;
    }
    public Instant getStartTimeUTC() {
        return startTime;
    }
    public LocalDateTime getStartTimeLDT(){
        return TimeUtility.convertToLocalDateTime(startTime);
    }
    public Instant getEndTimeUTC() {
        return endTime;
    }
    public LocalDateTime getEndTimeLDT(){
        return TimeUtility.convertToLocalDateTime(endTime);
    }
    public int getCustomerId() {
        return customerId;
    }
    public int getContactId() {
        return contactId;
    }
    public int getUserId() {
        return userId;
    }


    /*------------------------------------------
    --------APPOINTMENT-DATATABLE METHODS-------
    -------------------------------------------*/

    public static boolean addAppointment(String title, String description, String location, String type, Instant startTime, Instant endTime,
                                         int customerId, int userId, int contactId) throws SQLException {

        if(AppointmentDataAccess.insertAppointment(title, description, location, type, startTime, endTime,
                                            customerId, userId, contactId))
            return true;
        else
            return false;
    }

    public static boolean updateAppointment(int appointmentId, String title, String description, String location, String type, Instant startTime, Instant endTime,
                                            int customerId, int userId, int contactId) throws SQLException {

        if(AppointmentDataAccess.updateAppointment(appointmentId, title, description, location, type, startTime, endTime,
                                            customerId, userId, contactId))
            return true;
        else
            return false;
    }

    public static boolean removeAppointment(int appointmentId) throws SQLException {
        if(AppointmentDataAccess.deleteAppointment(appointmentId))
            return true;
        else
            return false;
    }

    public static Appointment getAppointment(int appointmentId) throws SQLException {

        return AppointmentDataAccess.getAppointment(appointmentId);
    }

    public static ObservableList<Appointment> getAllAppointments() throws SQLException {

        return AppointmentDataAccess.getAllAppointments();

    }



}
