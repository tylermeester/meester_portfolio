package model;

import utilities.DivisionDataAccess;
import javafx.collections.ObservableList;

import java.sql.SQLException;

public class Division {
    /*-------------------------------------------
    -------------DIVISION ATTRIBUTES-------------
    --------------------------------------------*/
    private int divisionId;
    private String divisionName;
    private int countryId;


    /*------------------------------------------
    -------------DIVISION CONSTRUCTOR-----------
    -------------------------------------------*/
    public Division(int divisionId, String divisionName, int countryId) {
        this.divisionId = divisionId;
        this.divisionName = divisionName;
        this.countryId = countryId;
    }


    /*------------------------------------------
    --------------DIVISION SETTERS--------------
    -------------------------------------------*/
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }


    /*------------------------------------------
    --------------DIVISION GETTERS--------------
    -------------------------------------------*/
    public int getDivisionId() {
        return divisionId;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public int getCountryId() {
        return countryId;
    }



    /*------------------------------------------
    ---------DIVISION-DATATABLE METHODS---------
    -------------------------------------------*/
    public static Division getDivision(int divisionId) throws SQLException {

        return DivisionDataAccess.getDivision(divisionId);
    }
    public static ObservableList<Division> getAllDivisions() throws SQLException {

        return DivisionDataAccess.getAllDivisions();
    }

    public static ObservableList<Division> getAllDivisions(int countryId) throws SQLException {

        return DivisionDataAccess.getAllDivisions(countryId);
    }


    @Override
    public String toString(){
        return(divisionName);
    }



}
