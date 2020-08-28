package com.giangvdm.salesmgmtapp.controller;

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
    private TextField productPriceInput;
    
    /**
     * @param String Path to Product data file
     */
    private static final String PRODUCT_DATA_FILE_PATH = "src/com/giangvdm/salesmgmtapp/data/MATHANG.txt";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        /** Initiate last product id */
        ProductController.lastProductId = 0;
        
        /** Read product data file and initiate data */
        this.productList = FXCollections.observableArrayList();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(PRODUCT_DATA_FILE_PATH));
            String line = reader.readLine();
            while (line != null) {
                String[] lineData = line.split(",");
                
                try {
                    if (Float.parseFloat(lineData[2]) >= (float) 0) {
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
                }
                catch (ArrayIndexOutOfBoundsException | NumberFormatException arrEx) {
                    
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
        String price = this.productPriceInput.getText().trim();
        Float realPrice = Float.parseFloat(price);
        
        // all fields are required
        if (name.isEmpty() || price.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cannot create new Product.");
            alert.setContentText("Please fill in all the required fields!");
            alert.showAndWait();
            
            return;
        }
        
        int id = ++ProductController.lastProductId;
        
        try {
            Product prod = new Product(id, name, realPrice);
            
            // price can't be below 0
            if (prod.getPrice() < (float) 0){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Cannot create new Product.");
                alert.setContentText("Price cannot be below 0! Please try again!");
                alert.showAndWait();

                return;
            }
            
            /** Write to data file */
            BufferedWriter writer;
            
            writer = new BufferedWriter(new FileWriter(PRODUCT_DATA_FILE_PATH, true));
            String line = String.join(",", Integer.toString(id), name, Float.toString(realPrice));
            writer.newLine();
            writer.write(line);
            writer.close();
            
            /** Add to list */
            this.productList.add(prod);
        }
        catch (NumberFormatException arrEx) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cannot save new Product.");
            alert.setContentText("Price must be a number. Please try again!");
            alert.showAndWait();
            
            return;
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
        Boolean isDeleteSuccess = false;
        Product selectedProduct = table.getSelectionModel().getSelectedItem();
        
        /** Delete Customer data from file */
        File oldFile = new File(PRODUCT_DATA_FILE_PATH);
        File newFile = new File("src/com/giangvdm/salesmgmtapp/data/temp.txt");
        BufferedReader reader;
        BufferedWriter writer;
        try {
            String lineToDelete = String.join(
                    ",",
                    Integer.toString(selectedProduct.getId()),
                    selectedProduct.getName(),
                    Float.toString(selectedProduct.getPrice())
            );
            
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
            File dup = new File(PRODUCT_DATA_FILE_PATH);
            newFile.renameTo(dup);
        }
        catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cannot delete selected Product.");
            alert.setContentText("Something went wrong while deleting selected Product. Please try again!");
            alert.showAndWait();
            
            return;
        }
        catch (NullPointerException npex) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Heads up!");
            alert.setHeaderText("Please select a product to delete!");
            alert.showAndWait();
            
            // delete the temp data file
            newFile.delete();
            
            return;
        }
        
        if (isDeleteSuccess) {
            /** Remove record from TableView */
            table.getItems().remove(selectedProduct);
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Delete Product successfully!");
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("No Product to delete!");
            alert.showAndWait();
        }
    }
    
}
