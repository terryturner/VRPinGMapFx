package com.goldtek.main.config;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

public class ConfigDialog {
    final Stage Dialog = new Stage();
    final Window Window;
    
    public ConfigDialog(Window window) {
        Window = window;
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/goldtek/main/ConfigDialog.fxml"));
            
            Dialog.initModality(Modality.APPLICATION_MODAL);
            Dialog.initOwner(Window);
            //VBox dialogVbox = new VBox(20);
            //dialogVbox.getChildren().add(new Text("This is a Dialog"));
            Scene dialogScene = new Scene(root);
            Dialog.setScene(dialogScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
        

    }
    
    public void show() {

        
        Dialog.show();
    }
}
