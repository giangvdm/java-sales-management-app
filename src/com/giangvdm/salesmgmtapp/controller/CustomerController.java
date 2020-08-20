package com.giangvdm.salesmgmtapp.controller;

import com.giangvdm.salesmgmtapp.model.Customer;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author giangvdm
 */
public class CustomerController extends AbstractController implements Initializable {
    
    private static int lastCustomerId;
    
    @FXML
    private TableView<Customer> table;
    
    @FXML
    private TableColumn<Customer, Integer> idColumn;
    
    @FXML
    private TableColumn<Customer, String> nameColumn;
    
    @FXML
    private TableColumn<Customer, String> addressColumn;
    
    @FXML
    private TableColumn<Customer, String> groupColumn;
    
    private ObservableList<Customer> customerList;
    
    @FXML
    private TextField customerNameInput;
    
    @FXML
    private TextArea customerAddressInput;
    
    @FXML
    private ComboBox customerGroupInput;
    
    /**
     * @param String Path to Customer data file
     */
    private static final String CUSTOMER_DATA_FILE_PATH = "src/com/giangvdm/salesmgmtapp/data/KH.txt";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        /** Initiate last customer id */
        CustomerController.lastCustomerId = 0;
        
        /** Initiate Customer Group ComboBox */
        customerGroupInput.getItems().removeAll(customerGroupInput.getItems());
        customerGroupInput.getItems().addAll(
                Customer.groupTypes.WHOLESALE,
                Customer.groupTypes.RETAIL,
                Customer.groupTypes.ONLINE
        );
        customerGroupInput.getSelectionModel().select(Customer.groupTypes.WHOLESALE);
        
        /** Read customer data file and initiate data */
        this.customerList = FXCollections.observableArrayList();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(CUSTOMER_DATA_FILE_PATH));
            String line = reader.readLine();
            while (line != null) {
                String[] lineData = line.split(",");
                
                try {
                    Customer cust = new Customer(
                        Integer.parseInt(lineData[0]),
                        lineData[1],
                        lineData[2],
                        lineData[3]
                    );
                    this.customerList.add(cust);
                    // update last customer id
                    if (cust.getId() > CustomerController.lastCustomerId) {
                        CustomerController.lastCustomerId = cust.getId();
                    }
                }
                catch (ArrayIndexOutOfBoundsException arrEx) {
                    
                }
                
                // read next line
                line = reader.readLine();
            }
            reader.close();
        }
        catch (IOException e) {
        
        }
        
        /** Initiate Customer TableView */
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        table.setItems(customerList);
    }
    
    @Override
    public void create() {
        String name = this.customerNameInput.getText().trim();
        String addr = this.customerAddressInput.getText().trim();
        String group = this.customerGroupInput.getValue().toString().trim();
        
        if (name.isEmpty() || addr.isEmpty() || group.isEmpty()) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cannot create new Customer.");
            alert.setContentText("Please fill in all the required fields!");
            alert.showAndWait();
            
            return;
        }
        
        int id = ++CustomerController.lastCustomerId;
        
        Customer cust = new Customer(id, name, addr, group);
        
        /** Write to data file */
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(CUSTOMER_DATA_FILE_PATH, true));
            String line = String.join(",", Integer.toString(id), name, addr, group);
            writer.newLine();
            writer.write(line);
            writer.close();
            
            /** Add to list */
            this.customerList.add(cust);
        }
        catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cannot save new Customer.");
            alert.setContentText("Something went wrong while saving new Customer. Please try again!");
            alert.showAndWait();
            
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Create new Customer successfully!");
        alert.showAndWait();
    }
    
    @Override
    public void delete() {
        Customer selectedCustomer = table.getSelectionModel().getSelectedItem();
        
        /** Delete Customer data from file */
        File oldFile = new File(CUSTOMER_DATA_FILE_PATH);
        File newFile = new File("src/com/giangvdm/salesmgmtapp/data/temp.txt");
        BufferedReader reader;
        BufferedWriter writer;
        try {
            reader = new BufferedReader(new FileReader(oldFile));
            writer = new BufferedWriter(new FileWriter(newFile, true));
            String line = reader.readLine();
            while (line != null) {
                String[] lineData = line.split(",");
                // current not equals
                if (!lineData[0].equals(Integer.toString(selectedCustomer.getId()))) {
                    // write to temp file
                    writer.write(line);
                    writer.newLine();
                }
                // read next line
                line = reader.readLine();
            }
            reader.close();
            writer.close();
            
            oldFile.delete();
            File dup = new File(CUSTOMER_DATA_FILE_PATH);
            newFile.renameTo(dup);
            
            /** Remove record from TableView */
            table.getItems().remove(selectedCustomer);
        }
        catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cannot delete selected Customer.");
            alert.setContentText("Something went wrong while deleteing selected Customer. Please try again!");
            alert.showAndWait();
            
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Delete Customer successfully!");
        alert.showAndWait();
    }
    
}
