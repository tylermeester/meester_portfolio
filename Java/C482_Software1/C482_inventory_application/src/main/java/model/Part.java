package model;

abstract public class Part {

    //Attribute Fields
    private int id;
    private String name;
    private double price;
    private int inventory;
    private int min;
    private int max;


    //Constructor
    public Part(int partId, String name, double price, int inventory, int min, int max) {

        //Defining the attributes of the Part object
        this.id = partId;
        this.name = name;
        this.price = price;
        this.inventory = inventory;
        this.min = min;
        this.max = max;

    }
    //Setters
    public void setId(int id) {this.id = id;}

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setInventory(int inventory) {
        this.inventory = inventory;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public void setMax(int max) {
        this.max = max;
    }


    //Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getInventory() {
        return inventory;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }
}
