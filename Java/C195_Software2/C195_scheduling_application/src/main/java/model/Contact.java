package model;

import utilities.ContactDataAccess;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class Contact {
    /*-------------------------------------------
    -------------CONTACT ATTRIBUTES--------------
    --------------------------------------------*/
    private int id;
    private String name;
    private String email;


    /*------------------------------------------
    -------------CONTACT CONSTRUCTOR------------
    -------------------------------------------*/
    public Contact(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }


    /*------------------------------------------
    --------------CONTACT SETTERS---------------
    -------------------------------------------*/
    public void setId(int id) {this.id = id; }
    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }


    /*------------------------------------------
    --------------CONTACT SETTERS---------------
    -------------------------------------------*/
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }


    /*------------------------------------------
    ----------CONTACT-DATATABLE METHODS---------
    -------------------------------------------*/
    public static boolean addContact(String name, String email) throws SQLException {

        if(ContactDataAccess.insert(name, email))
            return true;
        else
            return false;
    }

    public static boolean updateContact(int contactId, String contactName, String contactEmail) throws SQLException {

        if(ContactDataAccess.updateContact(contactId, contactName, contactEmail))
            return true;
        else
            return false;
    }

    public static boolean removeContact(int contactId) throws SQLException {


        if(ContactDataAccess.deleteContact(contactId))
            return true;

        else
            return false;
    }

    public static Contact getContact(int contactId) throws SQLException {

        return ContactDataAccess.getContact(contactId);
    }

    public static ObservableList<Contact> getAllContacts() throws SQLException {

        return ContactDataAccess.getAllContacts();
    }

}
