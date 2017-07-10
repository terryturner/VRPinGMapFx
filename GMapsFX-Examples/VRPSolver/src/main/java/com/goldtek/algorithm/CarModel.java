package com.goldtek.algorithm;

import java.util.Collection;

import com.graphhopper.jsprit.core.problem.vehicle.VehicleType;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CarModel {
    final VehicleType mType;
    
    public CarModel(VehicleType type) {
        mType = type;
    }
    
    public VehicleType getVehicleType() { return mType; }
    
    public String getModel() {
        return mType.getTypeId();
    }
    
    public int getCapacity() {
        return mType.getCapacityDimensions().get(0);
    }
    
    @Override
    public String toString() {
        return mType.getTypeId() + " (" + mType.getCapacityDimensions().get(0) + ")";
    }
    
    public static ObservableList<CarModel> toCarModelList(Collection<VehicleType> list) {
        ObservableList<CarModel> CarModels = FXCollections.observableArrayList();
        for (VehicleType type : list) {
            CarModels.add(new CarModel(type));
        }
        return CarModels;
    }
}
