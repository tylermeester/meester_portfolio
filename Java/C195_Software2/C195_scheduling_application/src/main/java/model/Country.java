package model;

import utilities.CountryDataAccess;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class Country {
    /*-------------------------------------------
    -------------COUNTRY ATTRIBUTES--------------
    --------------------------------------------*/
    private int countryId;
    private String countryName;

    /*------------------------------------------
    -------------COUNTRY CONSTRUCTOR------------
    -------------------------------------------*/
    public Country(int countryId, String countryName) {
        this.countryId = countryId;
        this.countryName = countryName;
    }

    /*------------------------------------------
    --------------COUNTRY SETTERS---------------
    -------------------------------------------*/
    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    /*------------------------------------------
    --------------COUNTRY SETTERS---------------
    -------------------------------------------*/
    public int getCountryId() {
        return countryId;
    }

    public String getCountryName() {
        return countryName;
    }


    /*------------------------------------------
    ----------COUNTRY-DATATABLE METHODS---------
    -------------------------------------------*/
    public static Country getCountry(int countryId) throws SQLException {

        return CountryDataAccess.getCountry(countryId);
    }

    public static ObservableList<Country> getAllCountries() throws SQLException {

        return CountryDataAccess.getAllCountries();
    }


    @Override
    public String toString(){
        return(countryName);
    }
}
