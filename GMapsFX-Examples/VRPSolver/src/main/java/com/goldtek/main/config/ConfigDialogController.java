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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
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
    @FXML protected TitledPane ShipmentPane;
    @FXML protected ChoiceBox<CarModel> VehicleTypeBox;
    @FXML protected ComboBox<ConfigDepot> PickAShipMenu;
    @FXML protected ComboBox<ConfigDepot> SendAShipMenu;
    
    @FXML protected TextField VehicleDriverInput;
    @FXML protected TextField ShipAmount;
    @FXML protected Button AddVehicle;
    @FXML protected Button AddShipment;
    
    @FXML protected TabPane ServiceTab;
    @FXML protected ListView<Car> VehicleList;
    @FXML protected ListView<ConfigDepot> ServiceListBanqiao;
    @FXML protected ListView<ConfigDepot> ServiceListYonghe;
    @FXML protected ListView<ConfigDepot> ServiceListZhonghe;
    @FXML protected ListView<ConfigDepot> ServiceListTucheng;
    @FXML protected ListView<ConfigShipment> ShipmentList;
    
    private ObservableList<Car> Cars = FXCollections.observableArrayList();
    private ObservableList<ConfigDepot> Depots = FXCollections.observableArrayList();
    private ObservableList<ConfigShipment> Shipments = FXCollections.observableArrayList();
    private List<ConfigDepot> AllDepots = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        VehiclePane.expandedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue && (ServicePane.isExpanded() || ShipmentPane.isExpanded())) {
                    ServicePane.setExpanded(false);
                    ShipmentPane.setExpanded(false);
                }
            }
        });
        ServicePane.expandedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue && (VehiclePane.isExpanded() || ShipmentPane.isExpanded())) {
                    VehiclePane.setExpanded(false);
                    ShipmentPane.setExpanded(false);
                }
            }
        });
        ShipmentPane.expandedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue && (VehiclePane.isExpanded() || ServicePane.isExpanded())) {
                    VehiclePane.setExpanded(false);
                    ServicePane.setExpanded(false);
                }
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
        
        ObservableList<ConfigDepot> fromList = FXCollections.observableArrayList();
        ObservableList<ConfigDepot> toList = FXCollections.observableArrayList();
        PickAShipMenu.getItems().addAll(fromList);
        SendAShipMenu.getItems().addAll(toList);
        AddShipment.setOnAction(ActionEventHandler);
        ShipAmount.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    ShipAmount.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        ShipmentList.setItems(Shipments);
        ShipmentList.setCellFactory(param -> new ConfigCellShip(MouseEventHandler));
    }
    
    private EventHandler<ActionEvent> ActionEventHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (event.getSource() instanceof Button) {
                Button btn = (Button) event.getSource();
                if (AddVehicle.getId().equals(btn.getId())) {
                    if(VehicleDriverInput.getText().isEmpty()){
                        showAlert("Please insert a driver name!!!");
                    } else {
                        VehicleDriverInput.getText();
                        
                        for (Car car : Cars) {
                            if (car.getName().equalsIgnoreCase(VehicleDriverInput.getText())) {
                                showAlert("The driver is already existed!!!");
                                return;
                            }
                        }
                        CarModel model = VehicleTypeBox.getSelectionModel().getSelectedItem();
                        Car car = new Car(VehicleDriverInput.getText(), model.getModel(), model.getCapacity());
                        Cars.add(car);
                    }                    
                } else if (AddShipment.getId().equals(btn.getId())) {
                    int amount = 0;
                    try {
                        amount = Integer.parseInt(ShipAmount.getText());
                    } catch (NumberFormatException e) {
                        
                    } finally {
                        if (amount > 0 && PickAShipMenu.getValue() != null && SendAShipMenu.getValue() != null && !PickAShipMenu.getValue().isSameLocation(SendAShipMenu.getValue())) {
                            Shipments.add(new ConfigShipment(PickAShipMenu.getValue(), SendAShipMenu.getValue(), amount));
                        } else {
                            showAlert("Please check conditions again!");
                        }
                    }
                }
            }
            
        }
    };

    private EventHandler<MouseEvent> MouseEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (event.getSource() instanceof ImageView && ((ImageView)event.getSource()).getId().equals("DeleteCar")) {
                Cars.remove(VehicleList.getSelectionModel().getSelectedIndex());
                VehicleList.getSelectionModel().clearSelection();
            } else if (event.getSource() instanceof ImageView && ((ImageView)event.getSource()).getId().equals("DeleteShip")) {
                Shipments.remove(ShipmentList.getSelectionModel().getSelectedIndex());
                ShipmentList.getSelectionModel().clearSelection();
            }
        }
    };

    public void setBuilder() {
        initVehicleTypeBox();
        initVehicleList();
        initServiceList();
        initShipmentMenu();
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
    
    public List<ConfigShipment> getAddedShipments() {
        return Shipments;
    }
    
    private void initVehicleTypeBox() {
        VehicleTypeBox.setItems(VrpMaker.getInstance().getGoldenSampleCarModel());
        VehicleTypeBox.getSelectionModel().selectFirst();
    }
    
    private void initVehicleList() {
        for (Car car : VrpMaker.getInstance().getGoldenSampleCar()) Cars.add(car);
    }
    
    private void initServiceList() {
        for(ConfigDepot depot : VrpMaker.getInstance().getConfigDepot()) {
            AllDepots.add(depot);
        }
        
        Depots.clear();
        for (ConfigDepot depot : AllDepots) {
            if (depot.getAreaNumber().equals("220")) Depots.add(depot);
        }
    }
    
    private void initShipmentMenu() { 
        PickAShipMenu.getItems().clear();
        SendAShipMenu.getItems().clear();
        
        for (Depot depot : VrpMaker.getInstance().getGoldenSampleDepot()) {
            ConfigDepot from = new ConfigDepot(depot);
            ConfigDepot to = new ConfigDepot(depot);
            
            PickAShipMenu.getItems().add(from);
            SendAShipMenu.getItems().add(to);
        }
        
        for (ConfigShipment ship : VrpMaker.getInstance().getConfigShipment()) {
            Shipments.add(ship);
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
