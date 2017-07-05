package com.goldtek.main.config;

import javafx.scene.control.ButtonBar.ButtonData;

import java.io.IOException;

import com.goldtek.algorithm.Car;
import com.goldtek.algorithm.Depot;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.util.Pair;

public class ConfigDialog {

    
    Dialog<Pair<String, String>> Dialog = new Dialog<>();

    public ConfigDialog() {
        Dialog.setTitle("Algorithm Config");
        Dialog.setHeaderText("Adjust parameters here");
        
        ImageView image = new ImageView(this.getClass().getResource("/images/settings.png").toString());

        image.setFitWidth(50);
        image.setFitHeight(50);
        Dialog.setGraphic(image);

        try {
            Parent content = FXMLLoader.load(getClass().getResource("/com/goldtek/main/ConfigDialog.fxml"));
            Dialog.getDialogPane().setContent(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        ButtonType confirmButtonType = new ButtonType("Confirm", ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
        Dialog.getDialogPane().getButtonTypes().addAll(confirmButtonType, cancelButtonType);
        Dialog.getDialogPane().setPrefSize(600, 480);
    }
    
    public void show() {
        Dialog.showAndWait();
    }
}
