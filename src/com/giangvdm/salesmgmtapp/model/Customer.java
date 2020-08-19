package com.giangvdm.salesmgmtapp.model;

/**
 *
 * @author giangvdm
 */
public class Customer {
    
    /**
     * @param int customer identity number
     */
    private int id;
    
    /**
     * @param String customer name
     */
    private String name;
    
    /**
     * @param String customer address
     */
    private String address;
    
    /**
     * @param enum customer group
     */
    private String group;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Customer(int id, String name, String address, String group) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.group = group;
    }
    
}
