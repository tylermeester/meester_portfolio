package utilities;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Division;
import utilities.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static utilities.DBConnection.connection;

public class DivisionDataAccess {
    /**
     * Gets the Division object, referenced by the input 'divisionId', from the database.
     *
     * @param divisionId Division_ID
     * @return Division object
     * @throws SQLException when accessing the database
     */
    public static Division getDivision(int divisionId) throws SQLException {

        DBConnection.openConnection();

        String sqlStatement = "SELECT * " +
                "FROM first_level_divisions " +
                "WHERE Division_ID = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, divisionId);

        ResultSet result = preparedStatement.executeQuery();

        Division divisionResult = null;

        if(result.next()){
            int divisionID = result.getInt("Division_ID");
            String divisionName = result.getString("Division");
            int countryId = result.getInt("Country_ID");
            divisionResult = new Division(divisionID, divisionName, countryId);
        }

        DBConnection.closeConnection();

        return divisionResult;
    }

    /**
     * Gets all Divisions from the database.
     *
     * @return ObservableList of all Divisions in the database
     * @throws SQLException when accessing the database
     */
    public static ObservableList<Division> getAllDivisions() throws SQLException {

        ObservableList<Division> allDBDivisions = FXCollections.observableArrayList();

        DBConnection.openConnection();

        String sqlStatement = "SELECT * FROM first_level_divisions";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);

        ResultSet result = preparedStatement.executeQuery();

        while(result.next()){
            int divisionId = result.getInt("Division_ID");
            String divisionName = result.getString("Division");
            int countryId = result.getInt("Country_ID");
            Division divisionResult = new Division(divisionId, divisionName, countryId);
            allDBDivisions.add(divisionResult);
        }

        DBConnection.closeConnection();

        return allDBDivisions;
    }

    /**
     * Gets all of a Country's Divisions from the database.
     *
     * @param countryId Country_ID
     * @return ObservableList of all Country Divisions from the database
     * @throws SQLException when accessing the database
     */
    public static ObservableList<Division> getAllDivisions(int countryId) throws SQLException {

        ObservableList<Division> countryDivisions = FXCollections.observableArrayList();

        DBConnection.openConnection();

        String sqlStatement = "SELECT * " +
                "FROM first_level_divisions " +
                "WHERE Country_ID = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, countryId);

        ResultSet result = preparedStatement.executeQuery();

        while(result.next()){
            int divisionId = result.getInt("Division_ID");
            String divisionName = result.getString("Division");
            int countryID = result.getInt("Country_ID");
            Division divisionResult = new Division(divisionId, divisionName, countryID);
            countryDivisions.add(divisionResult);
        }

        DBConnection.closeConnection();

        return countryDivisions;
    }
}
