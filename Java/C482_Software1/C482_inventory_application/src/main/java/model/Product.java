package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Product {
    /*-------------------------------------------
    ------------PRODUCT ATTRIBUTES---------------
    --------------------------------------------*/
    /**
     * Observable List of all Parts associated with the Product
     */
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    /**
     * Product Id
     */
    private int id;

    /**
     * Product Name
     */
    private String name;

    /**
     * Product Price
     */
    private double price;

    /**
     * Product Inventory
     */
    private int inventory;

    /**
     * Product min
     */
    private int min;

    /**
     * Product Max
     */
    private int max;


    /*------------------------------------------
    ------------PRODUCT CONSTRUCTOR-------------
    -------------------------------------------*/
    /**
     * Product constructor
     * @param id id
     * @param name name
     * @param price price
     * @param inventory inventory
     * @param min min
     * @param max max
     */
    public Product(int id, String name, double price, int inventory, int min, int max) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.inventory = inventory;
        this.min = min;
        this.max = max;
    }

    /*------------------------------------------
    --------------PRODUCT SETTERS---------------
    -------------------------------------------*/
    /**
     * Sets the product Id
     * @param id id
     */
    //Setters
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the product name
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the product price
     * @param price price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Sets the product inventory
     * @param inventory inventory
     */
    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    /**
     * Sets the product min
     * @param min min
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Sets the product max
     * @param max max
     */
    public void setMax(int max) {
        this.max = max;
    }

    /*------------------------------------------
    ---------------PRODUCT GETTERS--------------
    -------------------------------------------*/
    /**
     * Gets the product Id
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the product name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the product price
     * @return price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Gets the product inventory
     * @return inventory
     */
    public int getInventory() {
        return inventory;
    }

    /**
     * Gets the product min
     * @return min
     */
    public int getMin() {
        return min;
    }

    /**
     * Gets the product max
     * @return max
     */
    public int getMax() {
        return max;
    }

    /**
     * Gets an ObservableList of all associated parts with the product
     * @return ObservableList of Parts
     */
    public ObservableList<Part> getAllAssociatedParts(){
        return associatedParts;
    }

    /*-------------------------------------------
    --------ADD/REMOVE ASSOCIATED PARTS----------
    --------------------------------------------*/
    /**
     * Adds an associated part to the product
     * @param part part to be added
     */
    public void addAssociatedPart(Part part){
        associatedParts.add(part);
    }

    /**
     * Deletes a part from the product's associated parts
     * @param part part to be deleted
     * @return true if the part was not removed, and false if it is removed
     */
    public boolean deleteAssociatedPart(Part part){
        //Remove the Part object, part, from associatedParts
        associatedParts.remove(part);

        //If the part does not exist, return false.
        return associatedParts.contains(part);
    }
}
