package com.goldtek.algorithm;

public class Depot {
	private final String mLocationID; 
	private final String mName;
	private final double mLongitude;
	private final double mLatitude;
	private int mPickupCapacity = 0;
	private int mDeliverCapacity = 0;
	
	public Depot(String id, String name, double lng, double lat) {
		this(id, name, lng, lat, 0, 0);
	}
	
	public Depot(String id, String name, double lng, double lat, int pickupNo, int deliverNo) {
		mLocationID = id;
		mName = name;
		mLongitude = lng;
		mLatitude = lat;
		mPickupCapacity = pickupNo;
		mDeliverCapacity = deliverNo;
	}
	
	public String getLocationID() { return mLocationID; }
	
	public String getName() {return mName; }
	
	public double getLongitude() { return mLongitude; }
	
	public double getLatitude() { return mLatitude; }
	
	public int getPickupCapacity() { return mPickupCapacity; }
	
	public int getDeliverCapacity() { return mDeliverCapacity; }

	public void setPickupCapacity(int capacity) {
		mPickupCapacity = capacity;
	}
	
	public void setDeliverCapacity(int capacity) {
		mDeliverCapacity = capacity;
	}
	
	public String toLatLongString() {
		return String.format("%.8G, %.8G", getLatitude(), getLongitude());
	}
	
	public String toString() {
		return String.format("[%s] [%f, %f] %s - Pickup: %d, Deliver: %d", mLocationID, mLongitude, mLatitude, mName, mPickupCapacity, mDeliverCapacity);
	}
}
