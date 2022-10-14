package model;

import utilities.CustomerDataAccess;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.SQLException;

public class Customer {
    /*-------------------------------------------
    ------------CUSTOMER ATTRIBUTES--------------
    --------------------------------------------*/
    private int customerId;
    private String name;
    private String address;
    private String postal;
    private String phone;

    private int divisionId;
    private ObservableList<Appointment> appointmentList = FXCollections.observableArrayList();


    /*------------------------------------------
    ------------CUSTOMER CONSTRUCTOR------------
    -------------------------------------------*/
    public Customer(int appointmentId, String name, String address, String postal, String phone, int divisionId) {
        this.customerId = appointmentId;
        this.name = name;
        this.address = address;
        this.postal = postal;
        this.phone = phone;
        this.divisionId = divisionId;
    }


    /*------------------------------------------
    -------------CUSTOMER SETTERS---------------
    -------------------------------------------*/
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setDivisionId(int divisionId){ this.divisionId = divisionId;}

    public void addAppointment(Appointment appointment) {this.appointmentList.add(appointment);}


    /*------------------------------------------
    -------------CUSTOMER GETTERS---------------
    -------------------------------------------*/
    public int getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPostal() {
        return postal;
    }

    public String getPhone() {
        return phone;
    }

    public int getDivisionId() {return divisionId;}
    public ObservableList<Appointment> getAppointmentList() throws SQLException {

        this.appointmentList =  CustomerDataAccess.getCustomerAppointments(customerId);

        return appointmentList;
    }


    /*------------------------------------------
    ---------CUSTOMER-DATATABLE METHODS---------
    -------------------------------------------*/
    public static boolean addCustomer(String customerName, String address, String postal,
                                      String phone, int divisionId) throws SQLException {

        if(CustomerDataAccess.insertCustomer(customerName, address, postal, phone, divisionId))
            return true;
        else
            return false;
    }

    public static boolean updateCustomer(int customerId, String customerName, String address,
                                         String postal, String phone, int divisionId) throws SQLException {

        if(CustomerDataAccess.updateCustomer(customerId, customerName, address, postal, phone, divisionId)) return true;
        else return false;
    }

    public static boolean removeCustomer(int customerId) throws SQLException {

        if(Customer.getCustomer(customerId).getAppointmentList().isEmpty()) {

            if (CustomerDataAccess.deleteCustomer(customerId))
                return true;
            else
                return false;
        }
        else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Cannot remove customer with upcoming appointments!");
            alert.showAndWait();
            return false;
        }
    }

    public static Customer getCustomer(int customerId) throws SQLException {

        return CustomerDataAccess.getCustomer(customerId);
    }

    public static ObservableList<Customer> getAllCustomers() throws SQLException {

        return CustomerDataAccess.getAllCustomers();
    }



}
