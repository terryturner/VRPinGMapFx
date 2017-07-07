package com.goldtek.main.config;

import java.net.URL;
import java.util.ResourceBundle;

import com.goldtek.algorithm.Car;
import com.goldtek.algorithm.CarModel;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.vehicle.Vehicle;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleType;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
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
    
    VehicleRoutingProblem.Builder mVrpBuilder;
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

//        Cars.add(new Car("Terry", 1, 0, 0, 50));
//        Cars.add(new Car("Peter", 1, 0, 0, 50));
//        Cars.add(new Car("Fred", 1, 0, 0, 50));
//        Cars.add(new Car("Darwin", 1, 0, 0, 50));
//        Cars.add(new Car("Howard", 1, 0, 0, 50));
//        Cars.add(new Car("James", 1, 0, 0, 50));
//        Cars.add(new Car("Jason", 1, 0, 0, 50));
//        Cars.add(new Car("Sena", 1, 0, 0, 50));
//        Cars.add(new Car("Jeff", 1, 0, 0, 50));
//        Cars.add(new Car("Jack", 1, 0, 0, 50));
//        Cars.add(new Car("Derek", 1, 0, 0, 50));
//        Cars.add(new Car("Alex", 1, 0, 0, 50));
//        Cars.add(new Car("Bryan", 1, 0, 0, 50));
//        Cars.add(new Car("Jackie", 1, 0, 0, 50));
        AddVehicle.setOnAction(ActionEventHandler);
        
        VehicleList.setItems(Cars);
        VehicleList.setCellFactory(param -> new ConfigCellCar(MouseEventHandler));
    }
    
    private EventHandler<ActionEvent> ActionEventHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            VehicleDriverInput.getText();
            VehicleTypeBox.getSelectionModel().getSelectedItem();
            
            for (Car car : Cars) {
                if (car.getName().equals(VehicleDriverInput.getText())) {
                    return;
                }
            }
            CarModel model = VehicleTypeBox.getSelectionModel().getSelectedItem();
            Cars.add(new Car(VehicleDriverInput.getText(), model.getModel(), model.getCapacity()));
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

    public void setBuilder(VehicleRoutingProblem.Builder builder) {
        mVrpBuilder = builder;
        updateVehicleTypeBox();
        updateVehicleList();
    }
    
    private void updateVehicleTypeBox() {
        ObservableList<CarModel> vehicleTypes = FXCollections.observableArrayList();
        for (VehicleType type : mVrpBuilder.getAddedVehicleTypes()) {
            vehicleTypes.add(new CarModel(type));
        }
        VehicleTypeBox.setItems(vehicleTypes);
        VehicleTypeBox.getSelectionModel().selectFirst();
    }
    
    private void updateVehicleList() {
        for (Vehicle vehicle : mVrpBuilder.getAddedVehicles()) {
            Cars.add(new Car(vehicle));
        }
    }
}
