package com.giangvdm.salesmgmtapp.model;

import java.util.ArrayList;

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
     * @param int customer id
     */
    private int customerId;
    
    /**
     * @param ArrayList<InvoiceItem> list of bought items
     */
    private ArrayList<InvoiceItem> items = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public ArrayList<InvoiceItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<InvoiceItem> items) {
        this.items = items;
    }

    public Invoice(int id, int customerId) {
        this.id = id;
        this.customerId = customerId;
    }
    
}
