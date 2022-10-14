package utilities;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Appointment;
import model.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

import static utilities.DBConnection.connection;

public class CustomerDataAccess {

    /**
     * Inserts a new Customer object into the database. Customer_ID is auto-generated in the database
     * and is not an input.
     *
     * @param customerName Name
     * @param address Address
     * @param postal Postal
     * @param phone Phone
     * @param divisionId Division_ID
     * @return true if successful insert, false if failed insert
     * @throws SQLException when accessing the database
     */
    public static boolean insertCustomer(String customerName, String address, String postal, String phone, int divisionId) throws SQLException {

        DBConnection.openConnection();

        String sqlStatement = "INSERT INTO customers (Customer_Name, Address, Postal_Code, Phone, Division_ID) " +
                "VALUES (?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, customerName);
        preparedStatement.setString(2, address);
        preparedStatement.setString(3, postal);
        preparedStatement.setString(4, phone);
        preparedStatement.setInt(5, divisionId);

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
     * Updates the Customer object, referenced by the input 'customerId', in the database with the new values.
     *
     * @param customerId Customer_ID
     * @param customerName Name
     * @param address Address
     * @param postal Postal
     * @param phone Phone
     * @param divisionId Division_ID
     * @return true if successful update, false if failed update
     * @throws SQLException when accessing database
     */
    public static boolean updateCustomer(int customerId, String customerName, String address, String postal, String phone, int divisionId) throws SQLException {

        DBConnection.openConnection();

        String sqlStatement = "UPDATE customers " +
                "SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? " +
                "WHERE Customer_ID = ? ";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, customerName);
        preparedStatement.setString(2, address);
        preparedStatement.setString(3, postal);
        preparedStatement.setString(4, phone);
        preparedStatement.setInt(5, divisionId);
        preparedStatement.setInt(6, customerId);

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
     * Deletes the Customer object, referenced by the input 'customerId' from the database.
     *
     * @param customerId Customer_ID
     * @return true if successful delete, false if failed delete
     * @throws SQLException when accessing the database
     */
    public static boolean deleteCustomer(int customerId) throws SQLException{
        DBConnection.openConnection();

        String sqlStatement = "DELETE FROM customers " +
                "WHERE Customer_ID = ?";

        PreparedStatement preparedStatement = DBConnection.connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, customerId);

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
     * Gets the Customer object, referenced by the input 'customerId', from the database.
     *
     * @param customerId Customer_ID
     * @return Customer object
     * @throws SQLException when accessing the database
     */
    public static Customer getCustomer(int customerId) throws SQLException {

        DBConnection.openConnection();

        String sqlStatement = "SELECT * " +
                "FROM customers " +
                "WHERE Customer_ID = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, customerId);

        ResultSet result = preparedStatement.executeQuery();

        Customer customerResult = null;

        if(result.next()){
            int customerID = result.getInt("Customer_ID");
            String customerName = result.getString("Customer_Name");
            String address = result.getString("Address");
            String postal = result.getString("Postal_Code");
            String phone = result.getString("Phone");
            int divisionId = result.getInt("Division_ID");
            customerResult = new Customer(customerID, customerName, address, postal, phone, divisionId);
        }

        DBConnection.closeConnection();

        return customerResult;
    }

    /**
     * Gets all Customers from the database.
     *
     * @return ObservableList of all Customers in the database
     * @throws SQLException when accessing the database
     */
    public static ObservableList<Customer> getAllCustomers() throws SQLException {

        ObservableList<Customer> allDBCustomers = FXCollections.observableArrayList();

        DBConnection.openConnection();

        String sqlStatement = "SELECT * FROM customers";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);

        ResultSet result = preparedStatement.executeQuery();

        while(result.next()){
            int customerId = result.getInt("Customer_ID");
            String customerName = result.getString("Customer_Name");
            String address = result.getString("Address");
            String postal = result.getString("Postal_Code");
            String phone = result.getString("Phone");
            int divisionId = result.getInt("Division_ID");

            Customer customerResult = new Customer(customerId, customerName, address, postal, phone, divisionId);
            allDBCustomers.add(customerResult);
        }

        DBConnection.closeConnection();

        return allDBCustomers;
    }

    /**
     * Gets all the Customer's appointments.
     *
     * @param customerId Customer_ID
     * @return ObservableList of Appointments that have a matching customerId
     * @throws SQLException when accessing the database
     */
    public static ObservableList<Appointment> getCustomerAppointments(int customerId) throws SQLException {

        ObservableList<Appointment> allCustomerAppointments = FXCollections.observableArrayList();

        DBConnection.openConnection();

        String sqlStatement = "SELECT * " +
                "FROM appointments " +
                "WHERE Customer_ID = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, customerId);

        ResultSet result = preparedStatement.executeQuery();


        while(result.next()){
            int appointmentID = result.getInt("Appointment_ID");
            String title = result.getString("Title");
            String description = result.getString("Description");
            String location = result.getString("Location");
            String type = result.getString("Type");
            Timestamp startTimestamp = result.getTimestamp("Start");
            Timestamp endTimestamp = result.getTimestamp("End");
            int customerID = result.getInt("Customer_ID");
            int contactId = result.getInt("Contact_ID");
            int userId = result.getInt("User_ID");

            Instant startInstant = startTimestamp.toInstant();
            Instant endInstant = endTimestamp.toInstant();

            allCustomerAppointments.add(new Appointment(appointmentID, title, description, location, type, startInstant, endInstant,
                    customerId, contactId, userId));
        }

        DBConnection.closeConnection();

        return allCustomerAppointments;
    }

    }










