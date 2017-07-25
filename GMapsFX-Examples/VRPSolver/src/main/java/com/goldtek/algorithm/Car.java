package com.goldtek.algorithm;

import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.problem.vehicle.Vehicle;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleImpl;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleType;
import com.graphhopper.jsprit.core.util.Coordinate;

import com.graphhopper.jsprit.core.problem.vehicle.VehicleImpl.Builder;

public class Car {
	private final String mID;
	private final String mModel;
	private final int mBeginLocationID;
	private final double mBeginLongitude;
	private final double mBeginLatitude;
	private final int mCapacity;
	private final double mEarliestDeparture;
    private final double mLatestArrival;

	public Car(Vehicle vehicle) {
	    mID = vehicle.getId();
        mModel = vehicle.getType().getTypeId();
        mBeginLocationID = Integer.valueOf(vehicle.getStartLocation().getId());
        mBeginLongitude = vehicle.getStartLocation().getCoordinate().getX();
        mBeginLatitude = vehicle.getStartLocation().getCoordinate().getY();
        mCapacity = vehicle.getType().getCapacityDimensions().get(0);
        mEarliestDeparture = vehicle.getEarliestDeparture();
        mLatestArrival = vehicle.getLatestArrival();
	}
	
	public Car(String id, String model, int capacity) {
	    mID = id;
	    mModel = model;
	    mCapacity = capacity;
	    mBeginLocationID = 1;
	    mBeginLongitude = 121.487799;
        mBeginLatitude = 24.997798;
        mEarliestDeparture = 0;
        mLatestArrival = 28800.0;
	}
	
	public String getName() {
	    return mID;
	}
	
	public String getModel() {
	    return mModel;
	}
	
	public int getCapacity() {
	    return mCapacity;
	}
	
	public Vehicle toVehicle(VehicleType type) {
	    Builder vehicleBuilder = VehicleImpl.Builder.newInstance(mID);
	    Location.Builder locBuilder = Location.Builder.newInstance();
	    locBuilder.setId(String.valueOf(mBeginLocationID));
	    locBuilder.setCoordinate(Coordinate.newInstance(mBeginLongitude, mBeginLatitude));
	    
        vehicleBuilder.setStartLocation(locBuilder.build());
        vehicleBuilder.setEarliestStart(mEarliestDeparture);
        vehicleBuilder.setLatestArrival(mLatestArrival);
        vehicleBuilder.setType(type);
        VehicleImpl vehicle = vehicleBuilder.build();
        return vehicle;
	}

}
