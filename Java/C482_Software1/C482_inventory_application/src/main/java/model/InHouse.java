package model;

public class InHouse extends Part {
    /*------------------------------------------
    ------------IN HOUSE ATTRIBUTES-------------
    -------------------------------------------*/
    /**
     * Machine ID
     */
    private int machineId;

    /*------------------------------------------
    ------------IN HOUSE CONSTRUCTOR------------
    -------------------------------------------*/
    /**
     * Constructor for InHouse Part Object
     * @param id id
     * @param name name
     * @param price price
     * @param inventory inventory
     * @param min min
     * @param max max
     * @param machineId machineId
     */
    public InHouse(int id, String name, double price, int inventory, int min, int max, int machineId) {
        super(id, name, price, inventory, min, max);
        this.machineId = machineId;
    }

    /*------------------------------------------
    --------------IN HOUSE SETTER---------------
    -------------------------------------------*/
    /**
     * Sets the machineId for the part
     * @param machineId machineId
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    /*------------------------------------------
    --------------IN HOUSE GETTER---------------
    -------------------------------------------*/
    /**
     * Gets the MachineId for the part
     * @return machineId
     */
    public int getMachineId() {
        return machineId;
    }
}
