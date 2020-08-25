package com.giangvdm.salesmgmtapp.controller;

import com.giangvdm.salesmgmtapp.model.Invoice;
import com.giangvdm.salesmgmtapp.model.Item;
import com.giangvdm.salesmgmtapp.model.Product;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author giangvdm
 */
public class InvoiceCreateController extends AbstractController implements Initializable {
    
    private static int lastInvoiceId;
    
    @FXML
    private TableView<Item> itemTable;

    @FXML
    private TableColumn<Item, Integer> productIdColumn;

    @FXML
    private TableColumn<Item, String> productNameColumn;

    @FXML
    private TableColumn<Item, Float> priceColumn;
    
    @FXML
    private TableColumn<Item, Integer> quantityColumn;
    
    @FXML
    private TableColumn<Item, Float> rowTotalColumn;

    private ObservableList<Item> itemList;
    
    @FXML
    private TextField customerNameInput;
    
    @FXML
    private ComboBox productInput;
    
    @FXML
    private TextField quantityInput;
    
    /**
     * @param String Path to Product data file
     */
    private static final String PRODUCT_DATA_FILE_PATH = "src/com/giangvdm/salesmgmtapp/data/MATHANG.txt";
    
    /**
     * @param String Path to Customer data file
     */
    private static final String INVOICE_DATA_FILE_PATH = "src/com/giangvdm/salesmgmtapp/data/HOADON.txt";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        /** Initiate last product id */
        InvoiceCreateController.lastInvoiceId = 0;
        
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(INVOICE_DATA_FILE_PATH));
            String line = reader.readLine();
            while (line != null) {
                String[] lineData = line.split(",");
                
                try {
                    if (Integer.parseInt(lineData[2]) > 0 && Float.parseFloat(lineData[3]) > (float) 0) {
                        Invoice inv = new Invoice(
                            Integer.parseInt(lineData[0]),
                            lineData[1],
                            Integer.parseInt(lineData[2]),
                            Float.parseFloat(lineData[3])
                        );
                        // update last invoice id
                        if (inv.getId() > InvoiceCreateController.lastInvoiceId) {
                            InvoiceCreateController.lastInvoiceId = inv.getId();
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
        
        /** Initiate item table view */
        this.itemList = FXCollections.observableArrayList();
        
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        rowTotalColumn.setCellValueFactory(new PropertyValueFactory<>("rowTotal"));

        /** Initiate Customer Group ComboBox */
        this.productInput.getItems().removeAll(this.productInput.getItems());
        
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
                        String productOption = prod.getId() + " - " + prod.getName();
                        this.productInput.getItems().add(productOption);
                    }
                }
                catch (ArrayIndexOutOfBoundsException | NumberFormatException ex) {
                    
                }
                
                // read next line
                line = reader.readLine();
            }
            reader.close();
        }
        catch (IOException e) {
        
        }
    }
    
    @FXML
    public void addProduct() {
        String productOption = this.productInput.getValue().toString().trim();
        String quantityInput = this.quantityInput.getText().trim();
        
        // search for product by id
        String[] parts = productOption.split(" - ", 2);
        Product prod = null;
        try {
            int productId = Integer.parseInt(parts[0]);
            BufferedReader reader;
            reader = new BufferedReader(new FileReader(PRODUCT_DATA_FILE_PATH));
            String line = reader.readLine();
            while (line != null) {
                String[] lineData = line.split(",");
                
                if (Integer.parseInt(lineData[0]) == productId) {
                    prod = new Product(
                        Integer.parseInt(lineData[0]),
                        lineData[1],
                        Float.parseFloat(lineData[2])
                    );
                    break;
                }
                
                // read next line
                line = reader.readLine();
            }
            reader.close();
        }
        catch (ArrayIndexOutOfBoundsException | NumberFormatException | IOException ex) {
            System.out.println(ex.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cannot add Product.");
            alert.setContentText("Something when wrong while trying to add the Product! Please try again!");
            alert.showAndWait();
            
            return;
        }
        
        // process quantity and row total
        int realQty = 1;
        try {
            realQty = Integer.parseInt(quantityInput);
        }
        catch (NumberFormatException nfex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cannot add Product.");
            alert.setContentText("Invalid quantity! Please fill in integer value only!");
            alert.showAndWait();
            
            return;
        }
        
        float rowTotal; // calculate row total
        rowTotal = prod.getPrice() * realQty;
        
        Item item;
        item = new Item(
            prod.getId(),
            prod.getName(),
            prod.getPrice(),
            realQty,
            rowTotal
        );
        
        this.itemList.add(item);
        itemTable.setItems(itemList);
    }
    
    @FXML
    public void removeProduct() {
        Item selectedItem = this.itemTable.getSelectionModel().getSelectedItem();
        
        if (selectedItem != null) {
            this.itemList.remove(selectedItem);
        }
    }
    
    @Override
    public void create() {
        if (this.itemList.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cannot submit Invoice.");
            alert.setContentText("Please add at least one (1) Product!");
            alert.showAndWait();
        }
        else {
            String customerName = this.customerNameInput.getText();
            float total = 0;
            int totalQty = 0;
            
            ArrayList<Item> items = new ArrayList<>(this.itemList);
            
            for (Item item : items) {
                totalQty += item.getQuantity();
                total += item.getRowTotal();
            }
            
            try {
                String newInvoiceId = Integer.toString(InvoiceCreateController.lastInvoiceId + 1);
                String totalQtyStr = Integer.toString(totalQty);
                String totalStr = Float.toString(total);

                /** Write to data file */
                BufferedWriter writer;

                writer = new BufferedWriter(new FileWriter(INVOICE_DATA_FILE_PATH, true));
                String invLine = String.join(",", newInvoiceId, customerName, totalQtyStr, totalStr);
                writer.newLine();
                writer.write(invLine);
                writer.close();
            }
            catch (NumberFormatException arrEx) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Cannot save new Invoice.");
                alert.setContentText("Something went wrong while saving new Invoice. Please try again!");
                alert.showAndWait();

                return;
            }
            catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Cannot save new Product.");
                alert.setContentText("Something went wrong while saving new Invoice. Please try again!");
                alert.showAndWait();

                return;
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Create new Invoice successfully!");
            alert.showAndWait();
        }
    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setCustomerName(String name) {
        this.customerNameInput.setText(name);
    }
}
