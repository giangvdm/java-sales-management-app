package com.giangvdm.salesmgmtapp.controller;

import com.giangvdm.salesmgmtapp.model.Invoice;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 *
 * @author giangvdm
 */
public class InvoiceController implements Initializable {
    
    @FXML
    private TableView<Invoice> table;
    
    @FXML
    private TableColumn<Invoice, Integer> idColumn;
    
    @FXML
    private TableColumn<Invoice, Integer> customerIdColumn;
    
    private ObservableList<Invoice> invoiceList;
    
    /**
     * @param String Path to Invoice data file
     */
    private static final String INVOICE_DATA_FILE_PATH = "src/com/giangvdm/salesmgmtapp/data/HOADON.txt";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
}
