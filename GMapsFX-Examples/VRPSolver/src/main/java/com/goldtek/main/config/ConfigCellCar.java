package com.goldtek.main.config;

import java.io.IOException;

import com.goldtek.algorithm.Car;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ConfigCellCar extends ListCell<Car> {
    ConfigCellCarController controller;
    Parent root;
    
    EventHandler<MouseEvent> mouseHandler;
    
    public ConfigCellCar(EventHandler<MouseEvent> mouseHandler) {
        this.mouseHandler = mouseHandler;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/goldtek/main/ConfigCellCar.fxml"));
            
            root = loader.load();
            controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void updateItem(Car item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            controller.Cancel.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseHandler);
            controller.Cancel.setId("Cancel");
            controller.Driver.setText(item.getName());
            controller.VehicleType.setText(item.getModel());
            controller.Capacity.setText("Load: " + item.getCapacity());
            
            setGraphic(root);
        }
    }
}

