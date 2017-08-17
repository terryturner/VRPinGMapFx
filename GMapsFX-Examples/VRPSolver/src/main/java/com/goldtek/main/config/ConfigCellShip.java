package com.goldtek.main.config;

import java.io.IOException;

import com.goldtek.algorithm.Car;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ConfigCellShip extends ListCell<ConfigShipment> {
    @FXML private ImageView Cancel;
    @FXML private Text ShipmentContent;
    private Parent root;
    
    EventHandler<MouseEvent> mouseHandler;
    
    public ConfigCellShip(EventHandler<MouseEvent> mouseHandler) {
        this.mouseHandler = mouseHandler;
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/goldtek/main/ConfigCellShip.fxml"));
        fxmlLoader.setController(this);
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    @Override
    protected void updateItem(ConfigShipment item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null) {
            Cancel.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseHandler);
            Cancel.setId("DeleteShip");

            ShipmentContent.setText(item.toString());
            ShipmentContent.setText(String.format("From: %s To: %s Amount: %d", item.getPickup().getName(), item.getDropoff().getName(), item.getAmount()));
            setGraphic(root);
        }
    }
}

