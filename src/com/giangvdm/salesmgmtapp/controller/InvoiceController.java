package com.giangvdm.salesmgmtapp.controller;

import com.giangvdm.salesmgmtapp.model.Invoice;
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
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author giangvdm
 */
public class InvoiceController extends AbstractController implements Initializable {
    
    private static int lastInvoiceId;
    
    @FXML
    private TableView<Invoice> table;
    
    @FXML
    private TableColumn<Invoice, Integer> idColumn;
    
    @FXML
    private TableColumn<Invoice, String> customerNameColumn;
    
    @FXML
    private TableColumn<Invoice, Integer> numberOfItemsColumn;
    
    @FXML
    private TableColumn<Invoice, Float> totalColumn;
    
    private ObservableList<Invoice> invoiceList;
    
    /**
     * @param String Path to Customer data file
     */
    private static final String INVOICE_DATA_FILE_PATH = "src/com/giangvdm/salesmgmtapp/data/HOADON.txt";
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /** Initiate last customer id */
        InvoiceController.lastInvoiceId = 0;
        
        /** Read customer data file and initiate data */
        this.invoiceList = FXCollections.observableArrayList();
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(INVOICE_DATA_FILE_PATH));
            String line = reader.readLine();
            while (line != null) {
                String[] lineData = line.split(",");
                
                try {
                    Invoice inv = new Invoice(
                        Integer.parseInt(lineData[0]),
                        lineData[1],
                        Integer.parseInt(lineData[2]),
                        Float.parseFloat(lineData[3])
                    );
                    this.invoiceList.add(inv);
                    // update last customer id
                    if (inv.getId() > InvoiceController.lastInvoiceId) {
                        InvoiceController.lastInvoiceId = inv.getId();
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cannot read Invoice data.");
            alert.setContentText("Something went wrong while reading Invoice data from file. Please try again!");
            alert.showAndWait();
        }
        
        /** Initiate Customer TableView */
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        numberOfItemsColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfItems"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<>("total"));
        table.setItems(invoiceList);
    }

    @Override
    public void create() {
        
    }

    @Override
    public void delete() {
        Invoice selectedInvoice = table.getSelectionModel().getSelectedItem();
        
        /** Delete Customer data from file */
        File oldFile = new File(INVOICE_DATA_FILE_PATH);
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
                if (!lineData[0].equals(Integer.toString(selectedInvoice.getId()))) {
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
            File dup = new File(INVOICE_DATA_FILE_PATH);
            newFile.renameTo(dup);
            
            /** Remove record from TableView */
            table.getItems().remove(selectedInvoice);
        }
        catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Cannot delete selected Invoice.");
            alert.setContentText("Something went wrong while deleting selected Invoice. Please try again!");
            alert.showAndWait();
            
            return;
        }
        catch (NullPointerException npex) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Heads up!");
            alert.setHeaderText("Please select an invoice to delete!");
            alert.showAndWait();
            
            // delete the temp data file
            newFile.delete();
            
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Delete Invoice successfully!");
        alert.showAndWait();
    }
    
}
