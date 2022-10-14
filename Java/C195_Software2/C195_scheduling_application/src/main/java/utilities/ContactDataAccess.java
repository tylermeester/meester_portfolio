package utilities;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;
import utilities.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static utilities.DBConnection.connection;

public class ContactDataAccess {
    public static boolean insert(String name, String email) throws SQLException {

        DBConnection.openConnection();

        String sqlStatement = "INSERT INTO contacts (Contact_Name, Email) " +
                "VALUES (?, ?) ";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, name);
        preparedStatement.setString(2, email);

        int rowsAffected = preparedStatement.executeUpdate();

        DBConnection.closeConnection();

        if (rowsAffected > 0) {
            System.out.println("Insert Successful");
            return true;
        } else {
            System.out.println("Insert Failed");
            return false;
        }
    }

    public static boolean updateContact(int contactId, String contactName, String contactEmail) throws SQLException {

        DBConnection.openConnection();

        String sqlStatement = "UPDATE contacts  " +
                "SET Contact_Name = ?, Email = ? " +
                "WHERE Contact_ID = ? ";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);

        preparedStatement.setString(1, contactName);
        preparedStatement.setString(2, contactEmail);
        preparedStatement.setInt(3, contactId);

        int rowsAffected = preparedStatement.executeUpdate();

        DBConnection.closeConnection();

        if(rowsAffected > 0){
            System.out.println("Update Successful");
            return true;
        }
        else{
            System.out.println("Update Failed");
            return false;
        }
    }

    public static boolean deleteContact(int contactId) throws SQLException {

        DBConnection.openConnection();

        String sqlStatement = "DELETE FROM contacts " +
                "WHERE Contact_ID = ? ";

        PreparedStatement preparedStatement = DBConnection.connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, contactId);

        int rowsAffected = preparedStatement.executeUpdate();

        DBConnection.closeConnection();

        if (rowsAffected > 0) {
            System.out.println("Delete Successful");
            return true;
        } else {
            System.out.println("Delete Failed");
            return false;
        }

    }

    public static Contact getContact(int contactId) throws SQLException {

        DBConnection.openConnection();

        String sqlStatement = "SELECT *  " +
                "FROM contacts " +
                "WHERE Contact_ID = ? ";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, contactId);

        ResultSet result = preparedStatement.executeQuery();

        Contact contactResult = null;

        if(result.next()){
            int id = result.getInt("Contact_ID");
            String name = result.getString("Contact_Name");
            String email = result.getString("Email");
            contactResult = new Contact(id, name, email);
        }

        DBConnection.closeConnection();

        return contactResult;
    }

    public static ObservableList<Contact> getAllContacts() throws SQLException {

        ObservableList<Contact> allDBContacts = FXCollections.observableArrayList();

        DBConnection.openConnection();

        String sqlStatement = "SELECT * FROM contacts ";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);

        ResultSet result = preparedStatement.executeQuery();

        while(result.next()){
            int contactId = result.getInt("Contact_ID");
            String contactName = result.getString("Contact_Name");
            String contactEmail = result.getString("Email");
            Contact contactResult = new Contact(contactId, contactName, contactEmail);
            allDBContacts.add(contactResult);
        }

        DBConnection.closeConnection();

        return allDBContacts;

    }

}
