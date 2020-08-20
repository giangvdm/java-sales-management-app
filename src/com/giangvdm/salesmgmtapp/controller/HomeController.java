package com.giangvdm.salesmgmtapp.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 *
 * @author giangvdm
 */
public class HomeController implements Initializable {
    
    @FXML
    private Label label;
    
    @FXML
    private void exitAction(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }
    
    @FXML
    private void switchToCustomerView(ActionEvent event) throws IOException {
        Parent customerView = FXMLLoader.load(getClass().getClassLoader().getResource("com/giangvdm/salesmgmtapp/view/CustomerView.fxml"));
        
        Scene customerScene = new Scene(customerView);
        
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(customerScene);
        window.show();
    }
    
    @FXML
    private void switchToProductView(ActionEvent event) throws IOException {
        Parent productView = FXMLLoader.load(getClass().getClassLoader().getResource("com/giangvdm/salesmgmtapp/view/ProductView.fxml"));
        
        Scene productScene = new Scene(productView);
        
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(productScene);
        window.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO
    }
}
