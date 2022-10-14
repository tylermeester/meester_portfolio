package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {
    /*------------------------------------------
    -----------INVENTORY ATTRIBUTES-------------
    -------------------------------------------*/
    /**
     * Public Static attribute for partId
     */
    public static int partId = 1;

    /**
     * Public Static attribute for productID
     */
    public static int productId = 1;

    /**
     * ObservableList of all parts in Inventory
     */
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();

    /**
     * ObservableList of all products in Inventory
     */
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();


    /*------------------------------------------
    -------------ADD TO INVENTORY---------------
    -------------------------------------------*/
    /**
     * Adds the inputted Part to Inventory
     * @param newPart the part to be added
     */
    public static void addPart(Part newPart){
        allParts.add(newPart);
    }

    /**
     * Adds the inputted Product to Inventory
     * @param newProduct the product to be inputted
     */
    public static void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }



    /*------------------------------------------
    --------------LOOKUP INVENTORY--------------
    -------------------------------------------*/
    /**
     * Looks up a Part in Inventory by partId.
     * @param partId the partId to be searched for
     * @return the Part result. If no Part found, returns null.
     */
    public static Part lookupPart(int partId){

        ObservableList<Part> allParts = Inventory.getAllParts();

        for(Part part : allParts) {
            if (part.getId() == partId)
                return part;
        }

        return null;
    }

    /**
     * Looks up a Product in Inventory by productId
     * @param productId the Product to be searched for
     * @return the Product result. If no Product found, returns null.
     */
    public static Product lookupProduct(int productId){

        ObservableList<Product> allProducts = Inventory.getAllProducts();


        //For-loop that goes through allProducts to find a matching productId
        for(Product product : allProducts) {
            if (product.getId() == productId)
                return product;
        }

        return null;

    }

    /**
     * Searches for a Part in Inventory by name or partial name.
     * RUNTIME ERROR: Initially this method output a single Part object and did not meet the project
     * requirements. This issue was fixed by outputting an ObservableList instead.
     *
     * @param partName String that is to be searched for
     * @return ObservableList of all matching Part objects.
     */
    public static ObservableList<Part> lookupPart(String partName){
        //Creates two ObservableLists, one of all parts in the Inventory, and another
        // for the parts that have partially matching names
        ObservableList<Part> selectedParts = FXCollections.observableArrayList();
        ObservableList<Part> allParts = Inventory.getAllParts();

        //For loop that iterates through allParts and checks if any part names
        // contain the inputted partialName
        for (Part part : allParts) {
            if (part.getName().contains(partName)) {
                selectedParts.add(part);
            }
        }

        //Returns the ObservableList of parts with partially matching names
        return selectedParts;
    }

    /**
     * Searches for a Product in Inventory by name or partial name.
     * RUNTIME ERROR: Initially this method output a single Product object and did not meet the project requirements
     * when utilized by the onProductSearch() method. This issue was fixed by outputting an ObservableList instead.
     *
     * @param productName String that is to be searched for
     * @return ObservableList of all matching Product objects.
     */
    public static ObservableList<Product> lookupProduct(String productName){

        //Instantiating an empty ObservableList, lookupProductList, to add Product objects to
        ObservableList<Product> selectedProducts = FXCollections.observableArrayList();
        ObservableList<Product> allProducts = Inventory.getAllProducts();

        //For loop that iterates through allProducts and checks if any product names
        // contain the inputted partialName
        for (Product product : allProducts) {
            if (product.getName().contains(productName)) {
                selectedProducts.add(product);
            }
        }

        //Returns the ObservableList of products with partially matching names
        return selectedProducts;
    }


    /*------------------------------------------
    --------------UPDATE INVENTORY--------------
    -------------------------------------------*/
    /**
     * Updates the Part with the inputted Id with a new Part object
     * @param id the Id of the part to be updated
     * @param newPart the updated Part
     */
    public static void updatePart(int id, Part newPart){

        int index = -1;

        for(Part part : Inventory.getAllParts()){
            index++;

            if(part.getId() == id){
                Inventory.getAllParts().set(index, newPart);
            }
        }
    }

    /**
     * Updates the Product with the inputted Id with a new Product object
     * @param index the Id of the part to be updated
     * @param selectedProduct the updated Part
     */
    public static void updateProduct(int index, Product selectedProduct){

        for(Product product : allProducts){
            if(product == selectedProduct)
                allProducts.set(index, selectedProduct);
        }
    }


    /*------------------------------------------
    --------------DELETE INVENTORY--------------
    -------------------------------------------*/
    /**
     * Deletes the inputted part
     * @param selectedPart the part to be deleted
     * @return false if no part deleted
     */
    public static boolean deletePart(Part selectedPart){

       if(allParts.contains(selectedPart))
           return allParts.remove(selectedPart);
       else
           return false;
    }

    /**
     * Deletes the inputted Product
     * @param selectedProduct the Product to be deleted
     * @return false if no Product is deleted
     */
    public static boolean deleteProduct(Product selectedProduct){

        if(allProducts.contains(selectedProduct))
            return allProducts.remove(selectedProduct);
        else
            return false;
    }


    /*------------------------------------------
    --------------INVENTORY GETTERS-------------
    -------------------------------------------*/
    /**
     * Gets all Parts in Inventory
     * @return allParts ObservableList of Parts
     */
    public static ObservableList<Part> getAllParts(){
        return allParts;
    }

    /**
     * Gets all Products in Inventory
     * @return allProducts ObservableList of Product
     */
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }

}
