package com.goldtek.main.config;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class ConfigCellCarController implements Initializable {
    @FXML protected ImageView Cancel;
    @FXML protected Text Driver;
    @FXML protected Text VehicleType;
    @FXML protected Text Capacity;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {}
}
