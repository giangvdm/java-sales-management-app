/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.giangvdm.salesmgmtapp.model;

/**
 *
 * @author giangvdm
 */
public class InvoiceItem {
    
    /**
     * @param int invoice item id
     */
    private int id;
    
    /**
     * @param int product id
     */
    private int productId;
    
    /**
     * @param int quantity bought
     */
    private int quantity;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public InvoiceItem(int id, int productId, int quantity) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
    }
    
}
