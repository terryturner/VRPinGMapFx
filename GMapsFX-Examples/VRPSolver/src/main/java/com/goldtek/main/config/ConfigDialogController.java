package com.goldtek.main.config;

import java.net.URL;
import java.util.ResourceBundle;

import com.goldtek.algorithm.Car;
import com.goldtek.algorithm.CarModel;
import com.goldtek.algorithm.VrpMaker;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class ConfigDialogController implements Initializable {
    @FXML protected TitledPane VehiclePane;
    @FXML protected TitledPane ServicePane;
    @FXML protected ChoiceBox<CarModel> VehicleTypeBox;
    @FXML protected TextField VehicleDriverInput;
    @FXML protected Button AddVehicle;
    @FXML protected ListView<Car> VehicleList;
    
    ObservableList<Car> Cars = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        VehiclePane.expandedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue && ServicePane.isExpanded())
                    ServicePane.setExpanded(false);
            }
        });
        ServicePane.expandedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue && VehiclePane.isExpanded())
                    VehiclePane.setExpanded(false);
            }
        });

        AddVehicle.setOnAction(ActionEventHandler);
        
        VehicleList.setItems(Cars);
        VehicleList.setCellFactory(param -> new ConfigCellCar(MouseEventHandler));
    }
    
    private EventHandler<ActionEvent> ActionEventHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            VehicleDriverInput.getText();
            
            for (Car car : Cars) {
                if (car.getName().equalsIgnoreCase(VehicleDriverInput.getText())) {
                    return;
                }
            }
            CarModel model = VehicleTypeBox.getSelectionModel().getSelectedItem();
            Car car = new Car(VehicleDriverInput.getText(), model.getModel(), model.getCapacity()); 
            Cars.add(car);
        }
    };

    private EventHandler<MouseEvent> MouseEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (event.getSource() instanceof ImageView && ((ImageView)event.getSource()).getId().equals("Cancel")) {
                Cars.remove(VehicleList.getSelectionModel().getSelectedIndex());
                VehicleList.getSelectionModel().clearSelection();
            }
        }
    };

    public void setBuilder() {
        initVehicleTypeBox();
        initVehicleList();
    }
    
    public void updateVehicleList() {
        VrpMaker.getInstance().setCars(Cars);
    }
    
    private void initVehicleTypeBox() {
        VehicleTypeBox.setItems(VrpMaker.getInstance().getGoldenSampleCarModel());
        VehicleTypeBox.getSelectionModel().selectFirst();
    }
    
    private void initVehicleList() {
        for (Car car : VrpMaker.getInstance().getGoldenSampleCar()) Cars.add(car);
    }

}
