package com.goldtek.algorithm;

import com.graphhopper.jsprit.core.problem.vehicle.Vehicle;

public class Car {
	private final String mID;
	private final String mModel;
//	private final int mBeginLocationID;
//	private final double mBeginLongitude;
//	private final double mBeginLatitude;
	private final int mCapacity;
//	private final double mEarliestDeparture;
//    private final double mLatestArrival;

	public Car(Vehicle vehicle) {
	    mID = vehicle.getId();
        mModel = vehicle.getType().getTypeId();
//        mBeginLocationID = Integer.valueOf(vehicle.getStartLocation().getId());
//        mBeginLongitude = vehicle.getStartLocation().getCoordinate().getX();
//        mBeginLatitude = vehicle.getStartLocation().getCoordinate().getY();
        mCapacity = vehicle.getType().getCapacityDimensions().get(0);
//        mEarliestDeparture = vehicle.getEarliestDeparture();
//        mLatestArrival = vehicle.getLatestArrival();
	}
	
	public Car(String id, String model, int capacity) {
	    mID = id;
	    mModel = model;
	    mCapacity = capacity;
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
}
