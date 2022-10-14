package utilities;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;
import utilities.DBConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static utilities.DBConnection.connection;

public class CountryDataAccess {

    /**
     * Gets the Country object, referenced by the input 'countryId', from the database.
     *
     * @param countryId Country_ID
     * @return Country object
     * @throws SQLException when accessing database
     */
    public static Country getCountry(int countryId) throws SQLException {

        DBConnection.openConnection();

        String sqlStatement = "SELECT * " +
                "FROM countries " +
                "WHERE Country_ID = ?";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);
        preparedStatement.setInt(1, countryId);

        ResultSet result = preparedStatement.executeQuery();

        Country countryResult = null;

        if(result.next()){
            int countryID = result.getInt("Country_ID");
            String countryName = result.getString("Country");
            countryResult = new Country(countryID, countryName);
        }

        DBConnection.closeConnection();

        return countryResult;
    }

    /**
     * Gets all the Countries from the database.
     *
     * @return ObservableList of all countries
     * @throws SQLException when accessing database
     */
    public static ObservableList<Country> getAllCountries() throws SQLException {

        ObservableList<Country> allDBCountries = FXCollections.observableArrayList();

        DBConnection.openConnection();

        String sqlStatement = "SELECT * FROM countries";

        PreparedStatement preparedStatement = connection.prepareStatement(sqlStatement);

        ResultSet result = preparedStatement.executeQuery();

        while(result.next()){
            int countryId = result.getInt("Country_ID");
            String countryName = result.getString("Country");
            Country countryResult = new Country(countryId, countryName);
            allDBCountries.add(countryResult);
        }

        DBConnection.closeConnection();

        return allDBCountries;
    }

}
