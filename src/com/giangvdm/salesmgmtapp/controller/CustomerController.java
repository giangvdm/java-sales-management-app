package com.giangvdm.salesmgmtapp.controller;

import com.giangvdm.salesmgmtapp.model.Customer;
import java.io.BufferedReader;
import java.io.FileReader;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 *
 * @author giangvdm
 */
public class CustomerController implements Initializable {
    
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
    
    /**
     * @param String Path to Customer data file
     */
    private static final String CUSTOMER_DATA_FILE_PATH = "src/com/giangvdm/salesmgmtapp/data/KH.txt";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.customerList = FXCollections.observableArrayList();
        
        /** Read customer data file and initiate data */
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
        
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        groupColumn.setCellValueFactory(new PropertyValueFactory<>("group"));
        table.setItems(customerList);
    }
    
    @FXML
    private void backToHomeAction(ActionEvent event) throws IOException {
        Parent homeView = FXMLLoader.load(getClass().getClassLoader().getResource("com/giangvdm/salesmgmtapp/view/HomeView.fxml"));
        
        Scene homeScene = new Scene(homeView);
        
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(homeScene);
        window.show();
    }
}
