package com.giangvdm.salesmgmtapp.model;

/**
 *
 * @author giangvdm
 */
public class Item {
    
    /**
     * @param int product id
     */
    private int productId;
    
    /**
     * @param String product name
     */
    private String productName;
    
    /**
     * @param float item unit price
     */
    private float price;

    /**
     * @param int quantity
     */
    private int quantity;
    
    /**
     * @param float row total
     */
    private float rowTotal;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getRowTotal() {
        return rowTotal;
    }

    public void setRowTotal(float rowTotal) {
        this.rowTotal = rowTotal;
    }

    public Item(int productId, String productName, float price, int quantity, float rowTotal) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.rowTotal = rowTotal;
    }
}
