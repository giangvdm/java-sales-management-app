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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

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
                    // only accept lines with valid data
                    if (
                        lineData[3].equals(Customer.groupTypes.WHOLESALE.toString()) ||
                        lineData[3].equals(Customer.groupTypes.RETAIL.toString()) ||
                        lineData[3].equals(Customer.groupTypes.ONLINE.toString())
                    ) {
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
                }
                catch (ArrayIndexOutOfBoundsException | NumberFormatException arrEx) {
                    
                }
                
                // read next line
                line = reader.readLine();
            }
            reader.close();
        }
        catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cannot read Customer data.");
            alert.setContentText("Something went wrong while reading Customer data from file. Please try again!");
            alert.showAndWait();
        }
        
        /** Initiate Customer TableView */
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        table.setItems(customerList);
    }
    
    @FXML
    private void switchToInvoiceCreateView(ActionEvent event) throws IOException {
        Customer selectedCustomer = table.getSelectionModel().getSelectedItem();
        
        if (selectedCustomer != null) {           
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("com/giangvdm/salesmgmtapp/view/InvoiceCreateView.fxml"));
                Parent invoiceCreateView = loader.load();
//                Parent invoiceCreateView = FXMLLoader.load(getClass().getClassLoader().getResource("com/giangvdm/salesmgmtapp/view/InvoiceCreateView.fxml"));
                
                InvoiceCreateController invCreateCtrl = loader.getController();
                invCreateCtrl.setCustomerName(selectedCustomer.getName());
        
                Scene invoiceCreateScene = new Scene(invoiceCreateView);

                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                window.setScene(invoiceCreateScene);
                window.show();
            }
            catch (IOException ioex) {
                System.out.println(ioex.getMessage());
            }
        }
        else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Heads up!");
            alert.setHeaderText("Please select a customer to create an invoice for!");
            alert.showAndWait();
        }
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
        Boolean isDeleteSuccess = false;
        Customer selectedCustomer = table.getSelectionModel().getSelectedItem();
        String lineToDelete = String.join(
                ",",
                Integer.toString(selectedCustomer.getId()),
                selectedCustomer.getName(),
                selectedCustomer.getAddress(),
                selectedCustomer.getGroup()
        );
        
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
                // current not equals
                if (!line.equals(lineToDelete)) {
                    // write to temp file
                    writer.write(line);
                    writer.newLine();
                }
                else {
                    isDeleteSuccess = true;
                }
                // read next line
                line = reader.readLine();
            }
            reader.close();
            writer.close();
            
            oldFile.delete();
            File dup = new File(CUSTOMER_DATA_FILE_PATH);
            newFile.renameTo(dup);
        }
        catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cannot delete selected Customer.");
            alert.setContentText("Something went wrong while deleting selected Customer. Please try again!");
            alert.showAndWait();
            
            return;
        }
        catch (NullPointerException npex) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Heads up!");
            alert.setHeaderText("Please select a customer to delete!");
            alert.showAndWait();
            
            // delete the temp data file
            newFile.delete();
            
            return;
        }
        
        if (isDeleteSuccess) {
            /** Remove record from TableView */
            table.getItems().remove(selectedCustomer);
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Delete Customer successfully!");
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("No Customer to delete!");
            alert.showAndWait();
        }
    }
    
}
