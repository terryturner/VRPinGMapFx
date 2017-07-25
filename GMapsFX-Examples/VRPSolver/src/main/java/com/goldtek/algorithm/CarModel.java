package com.goldtek.algorithm;

import com.graphhopper.jsprit.core.problem.vehicle.VehicleType;

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
    
}
