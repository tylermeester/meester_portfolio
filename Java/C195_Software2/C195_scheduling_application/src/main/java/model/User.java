package model;

import utilities.UserDataAccess;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class User {
    /*-------------------------------------------
    ---------------USER ATTRIBUTES---------------
    --------------------------------------------*/
    private int id;
    private String userName;
    private String password;


    /*------------------------------------------
    --------------USER CONSTRUCTOR--------------
    -------------------------------------------*/
    public User(int id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
    }


    /*------------------------------------------
    ---------------USER SETTERS-----------------
    -------------------------------------------*/
    public void setId(int id) {
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    /*------------------------------------------
    ---------------USER GETTERS-----------------
    -------------------------------------------*/
    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }


    /*------------------------------------------
    -----------USER-DATATABLE METHODS-----------
    -------------------------------------------*/
    public static User getUser(int userId) throws SQLException {

        return UserDataAccess.getUser(userId);
    }

    public static User getUser(String userName) throws SQLException {

        return UserDataAccess.getUser(userName);
    }
    public static ObservableList<User> getAllUsers() throws SQLException {

        return UserDataAccess.getAllUsers();
    }
}
