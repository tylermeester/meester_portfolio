package utilities;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;
import utilities.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static utilities.DBConnection.connection;


public class UserDataAccess {
    public static boolean insertUser(String userName, String password) throws SQLException {

        DBConnection.openConnection();

        String sqlStatement = "INSERT INTO users (User_Name, Password) " +
                "VALUES (?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, userName);
        preparedStatement.setString(2, password);

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

    public static boolean updateUser(int userId, String userName, String password) throws SQLException {

        DBConnection.openConnection();

        String sqlStatement = "UPDATE users " +
                "SET User_Name = ?, Password = ? " +
                "WHERE User_ID = ? ";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);

        preparedStatement.setString(1, userName);
        preparedStatement.setString(2, password);
        preparedStatement.setInt(3, userId);

        int rowsAffected = preparedStatement.executeUpdate();

        if(rowsAffected > 0){
            System.out.println("Update Successful");
            DBConnection.closeConnection();
            return true;
        }
        else {
            System.out.println("Update Failed");
            DBConnection.closeConnection();
            return false;
        }

    }

    public static boolean deleteUser(int userId) throws SQLException{
        DBConnection.openConnection();

        String sqlStatement = "DELETE FROM users " +
                "WHERE User_ID = ?";

        PreparedStatement preparedStatement = DBConnection.connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, userId);

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

    public static boolean deleteUser(String userName) throws SQLException{
        DBConnection.openConnection();

        String sqlStatement = "DELETE FROM users " +
                "WHERE User_Name = ?";

        PreparedStatement preparedStatement = DBConnection.connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, userName);

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
     * Gets the User object, referenced by 'userId', from the database.
     *
     * @param userId User_ID
     * @return User object
     * @throws SQLException when accessing database
     */
    public static User getUser(int userId) throws SQLException {

        DBConnection.openConnection();

        String sqlStatement = "SELECT * " +
                "FROM users " +
                "WHERE User_ID = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, userId);

        ResultSet result = preparedStatement.executeQuery();

        User userResult = null;

        if(result.next()){
            int userID = result.getInt("User_ID");
            String userName = result.getString("User_Name");
            String password = result.getString("Password");
            userResult = new User(userID, userName, password);
        }

        DBConnection.closeConnection();

        return userResult;
    }

    /**
     * Gets the User object, referenced by 'userName', from the database.
     *
     * @param userName User_Name
     * @return User object
     * @throws SQLException when accessing the database
     */
    public static User getUser(String userName) throws SQLException {

        DBConnection.openConnection();

        String sqlStatement = "SELECT * " +
                "FROM users " +
                "WHERE User_Name = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setString(1, userName);

        ResultSet result = preparedStatement.executeQuery();

        User userResult = null;

        if(result.next()){
            int userID = result.getInt("User_ID");
            String username = result.getString("User_Name");
            String password = result.getString("Password");
            userResult = new User(userID, username, password);
        }

        DBConnection.closeConnection();

        return userResult;
    }

    /**
     * Gets all Users from the database.
     *
     * @return ObservableList of all Users
     * @throws SQLException when accessing database
     */
    public static ObservableList<User> getAllUsers() throws SQLException {

        ObservableList<User> allDBUsers = FXCollections.observableArrayList();

        DBConnection.openConnection();

        String sqlStatement = "SELECT * FROM Users";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);

        ResultSet result = preparedStatement.executeQuery();

        while(result.next()){
            int userId = result.getInt("User_ID");
            String userName = result.getString("User_Name");
            String password = result.getString("Password");
            User userResult = new User(userId, userName, password);
            allDBUsers.add(userResult);
        }

        DBConnection.closeConnection();

        return allDBUsers;
    }

}
