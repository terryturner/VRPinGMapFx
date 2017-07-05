package com.goldtek.main.config;

import java.net.URL;
import java.util.ResourceBundle;

import com.goldtek.algorithm.Car;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

public class ConfigDialogController implements Initializable {
    @FXML protected ListView<Car> VehicleList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO Auto-generated method stub
        ObservableList<Car> Cars = FXCollections.observableArrayList();
        Cars.add(new Car("Terry", 1, 0, 0, 50));
        Cars.add(new Car("Peter", 1, 0, 0, 50));
        VehicleList.setCellFactory(param -> new CarCell());
//        Cars.add(new CarCell());
//        Cars.add(new CarCell());
//        
//        VehicleList.setItems(Cars);
        
    }

}
