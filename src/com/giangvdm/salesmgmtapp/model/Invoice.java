package com.giangvdm.salesmgmtapp.model;

/**
 *
 * @author giangvdm
 */
public class Invoice {
    
    /**
     * @param int invoice id
     */
    private int id;
    
    /**
     * @param String customer name
     */
    private String customerName;
    
    /**
     * @param int number of items bought
     */
    private int numberOfItems;
    
    /**
     * @param float total
     */
    private float total;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public int getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(int numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public Invoice(int id, String customerName, int numberOfItems, float total) {
        this.id = id;
        this.customerName = customerName;
        this.numberOfItems = numberOfItems;
        this.total = total;
    }
    
}
