package com.giangvdm.salesmgmtapp.controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author giangvdm
 */
abstract class AbstractController {
    
    @FXML
    abstract void create();
    
    @FXML
    abstract void delete();
    
    @FXML
    private void backToHomeAction(ActionEvent event) throws IOException {
        Parent homeView = FXMLLoader.load(getClass().getClassLoader().getResource("com/giangvdm/salesmgmtapp/view/HomeView.fxml"));
        
        Scene homeScene = new Scene(homeView);
        
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(homeScene);
        window.show();
    }
    
}
