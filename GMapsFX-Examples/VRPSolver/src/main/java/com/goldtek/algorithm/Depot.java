package com.goldtek.algorithm;

public class Depot {
	private final String mLocationID; 
	private final String mName;
	private final double mLongitude;
	private final double mLatitude;
	private int mPickupCapacity = 0;
	private int mDeliverCapacity = 0;
	
	public Depot(String id, String name, double lot, double lat) {
		this(id, name, lot, lat, 0, 0);
	}
	
	public Depot(String id, String name, double lot, double lat, int pickupNo, int deliverNo) {
		mLocationID = id;
		mName = name;
		mLongitude = lot;
		mLatitude = lat;
		mPickupCapacity = pickupNo;
		mDeliverCapacity = deliverNo;
	}
	
	public String getLocationID() { return mLocationID; }
	
	public int getPickupCapacity() { return mPickupCapacity; }
	
	public int getDeliverCapacity() { return mDeliverCapacity; }

	public void setPickupCapacity(int capacity) {
		mPickupCapacity = capacity;
	}
	
	public void setDeliverCapacity(int capacity) {
		mDeliverCapacity = capacity;
	}
	
	public String toString() {
		return String.format("[%s] [%f, %f] %s - Pickup: %d, Deliver: %d", mLocationID, mLongitude, mLatitude, mName, mPickupCapacity, mDeliverCapacity);
	}
}
