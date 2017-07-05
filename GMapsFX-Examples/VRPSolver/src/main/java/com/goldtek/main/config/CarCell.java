package com.goldtek.main.config;

import java.io.IOException;

import com.goldtek.algorithm.Car;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;

public class CarCell extends ListCell<Car> {
    Parent root;
    public CarCell() {
        try {
            root = FXMLLoader.load(getClass().getResource("/com/goldtek/main/CarCell.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void updateItem(Car item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            setGraphic(root);
        }
//        if (empty || item == null) {
//            System.out.println("empty");
//            setGraphic(null);
//        } else {
//            System.out.println("set");
//            setGraphic(root);
//        }
    }
}

