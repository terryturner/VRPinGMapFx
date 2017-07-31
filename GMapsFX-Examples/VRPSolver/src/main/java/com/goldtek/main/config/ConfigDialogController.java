package com.goldtek.main.config;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.goldtek.algorithm.Car;
import com.goldtek.algorithm.CarModel;
import com.goldtek.algorithm.Depot;
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
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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
    
    @FXML protected TabPane ServiceTab;
    @FXML protected ListView<Car> VehicleList;
    @FXML protected ListView<ConfigDepot> ServiceListBanqiao;
    @FXML protected ListView<ConfigDepot> ServiceListYonghe;
    @FXML protected ListView<ConfigDepot> ServiceListZhonghe;
    @FXML protected ListView<ConfigDepot> ServiceListTucheng;
    
    private ObservableList<Car> Cars = FXCollections.observableArrayList();
    private ObservableList<ConfigDepot> Depots = FXCollections.observableArrayList();
    private List<ConfigDepot> AllDepots = new ArrayList<>();

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
        
        ServiceListBanqiao.setItems(Depots);
        ServiceListYonghe.setItems(Depots);
        ServiceListZhonghe.setItems(Depots);
        ServiceListTucheng.setItems(Depots);
        ServiceListBanqiao.setCellFactory(param -> new ConfigCellDepot());
        ServiceListYonghe.setCellFactory(param -> new ConfigCellDepot());
        ServiceListZhonghe.setCellFactory(param -> new ConfigCellDepot());
        ServiceListTucheng.setCellFactory(param -> new ConfigCellDepot());

        ServiceTab.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> ob, Tab oldTab, Tab newTab) {
                Depots.clear();
                for (ConfigDepot depot : AllDepots) {
                    if (depot.getAreaNumber().equals(newTab.getId()))
                        Depots.add(depot);
                }
            }
        });
    }
    
    private EventHandler<ActionEvent> ActionEventHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
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
        
        for(ConfigDepot depot : VrpMaker.getInstance().getConfigDepot()) {
            AllDepots.add(depot);
        }
        
        Depots.clear();
        for (ConfigDepot depot : AllDepots) {
            if (depot.getAreaNumber().equals("220"))
                Depots.add(depot);
        }
    }
    
    public ObservableList<Car> getAddedVehicles() {
        return Cars;
    }
    
    public List<Depot> getAddedDepots() {
        List<Depot> Depots = new ArrayList<>();
        for (ConfigDepot depot : AllDepots) {
            if (depot.onProperty().get()) {
                //System.out.println(depot.toString());
                Depots.add(depot);
            }
        }
        return Depots;
    }
    
    private void initVehicleTypeBox() {
        VehicleTypeBox.setItems(VrpMaker.getInstance().getGoldenSampleCarModel());
        VehicleTypeBox.getSelectionModel().selectFirst();
    }
    
    private void initVehicleList() {
        for (Car car : VrpMaker.getInstance().getGoldenSampleCar()) Cars.add(car);
    }

}
