package com.giangvdm.salesmgmtapp.controller;

import com.giangvdm.salesmgmtapp.model.Customer;
import com.giangvdm.salesmgmtapp.model.Product;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author giangvdm
 */
public class ProductController extends AbstractController implements Initializable {
    
    private static int lastProductId;
    
    @FXML
    private TableView<Product> table;
    
    @FXML
    private TableColumn<Product, Integer> idColumn;
    
    @FXML
    private TableColumn<Product, String> nameColumn;
    
    @FXML
    private TableColumn<Product, Float> priceColumn;
    
    private ObservableList<Product> productList;
    
    @FXML
    private TextField productNameInput;
    
    @FXML
    private TextField productPrice;
    
    /**
     * @param String Path to Product data file
     */
    private static final String PRODUCT_DATA_FILE_PATH = "src/com/giangvdm/salesmgmtapp/data/MATHANG.txt";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        /** Initiate last product id */
        ProductController.lastProductId = 0;
        
        /** Read customer data file and initiate data */
        this.productList = FXCollections.observableArrayList();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(PRODUCT_DATA_FILE_PATH));
            String line = reader.readLine();
            while (line != null) {
                String[] lineData = line.split(",");
                
                try {
                    Product prod = new Product(
                        Integer.parseInt(lineData[0]),
                        lineData[1],
                        Float.parseFloat(lineData[2])
                    );
                    this.productList.add(prod);
                    // update last product id
                    if (prod.getId() > ProductController.lastProductId) {
                        ProductController.lastProductId = prod.getId();
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
        
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        table.setItems(productList);
    }
    
    @Override
    public void create() {
        String name = this.productNameInput.getText().trim();
        String price = this.productPrice.getText().trim();
        
        if (name.isEmpty() || price.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cannot create new Product.");
            alert.setContentText("Please fill in all the required fields!");
            alert.showAndWait();
            
            return;
        }
        
        int id = ++ProductController.lastProductId;
        
        Product prod = new Product(id, name, Float.parseFloat(price));
        
        /** Write to data file */
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter(PRODUCT_DATA_FILE_PATH, true));
            String line = String.join(",", Integer.toString(id), name, price);
            writer.newLine();
            writer.write(line);
            writer.close();
            
            /** Add to list */
            this.productList.add(prod);
        }
        catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cannot save new Product.");
            alert.setContentText("Something went wrong while saving new Product. Please try again!");
            alert.showAndWait();
            
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Create new Product successfully!");
        alert.showAndWait();
    }
    
    @Override
    public void delete() {
        Product selectedProduct = table.getSelectionModel().getSelectedItem();
        
        /** Delete Customer data from file */
        File oldFile = new File(PRODUCT_DATA_FILE_PATH);
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
                if (!lineData[0].equals(Integer.toString(selectedProduct.getId()))) {
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
            File dup = new File(PRODUCT_DATA_FILE_PATH);
            newFile.renameTo(dup);
            
            /** Remove record from TableView */
            table.getItems().remove(selectedProduct);
        }
        catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cannot delete selected Product.");
            alert.setContentText("Something went wrong while deleteing selected Product. Please try again!");
            alert.showAndWait();
            
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Delete Product successfully!");
        alert.showAndWait();
    }
    
}
