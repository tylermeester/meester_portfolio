package model;

public class Outsourced extends Part {
    /*------------------------------------------
    -----------OUTSOURCED ATTRIBUTES------------
    -------------------------------------------*/
    /**
     * Company Name
     */
    private String companyName;

    /*------------------------------------------
    -----------OUTSOURCED CONSTRUCTOR------------
    -------------------------------------------*/
    /**
     * Constructor for Outsourced Part Object
     * @param id id
     * @param name name
     * @param price price
     * @param inventory inventory
     * @param min min
     * @param max max
     * @param companyName companyName
     */
    public Outsourced(int id, String name, double price, int inventory, int min, int max, String companyName) {
        super(id, name, price, inventory, min, max);
        this.companyName = companyName;
    }

    /*------------------------------------------
    -------------OUTSOURCED SETTERS-------------
    -------------------------------------------*/
    /**
     * Sets the companyName of the Part
     * @param companyName companyName
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /*------------------------------------------
    -------------OUTSOURCED GETTERS-------------
    -------------------------------------------*/
    /**
     * Gets the company name of the part
     * @return companyName
     */
    public String getCompanyName() {
        return companyName;
    }
}
