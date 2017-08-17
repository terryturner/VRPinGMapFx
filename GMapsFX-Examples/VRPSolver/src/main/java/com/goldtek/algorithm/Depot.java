package com.goldtek.algorithm;

import com.graphhopper.jsprit.core.util.Coordinate;

public class Depot {
	private final String mLocationID; 
	private final String mName;
	private final Coordinate mCoordinate;
	private final boolean mBelongCenter;
	private String mCounty = null;
	private String mArea = null;
	private int mPickupCapacity = 0;
	private int mDeliverCapacity = 0;
	private int mPickupShipment = 0;
	private int mDropoffShipment = 0;
	private String mNick = null;
	
	public Depot(Depot depot) {
	    this(false, depot.getLocationID(), depot.getName(), depot.getLongitude(), depot.getLatitude(), depot.getPickupCapacity(), depot.getDeliverCapacity());
	    mCounty = depot.getCounty();
	    mArea = depot.getArea();
	}
	
	public Depot(String id, String name, double lng, double lat) {
		this(false, id, name, lng, lat, 0, 0);
	}
	
    public Depot(String id, String name, double lng, double lat, int pickupNo, int deliverNo) {
        this(false, id, name, lng, lat, pickupNo, deliverNo);
    }
    
    public Depot(String id, String county, String area, String name, double lng, double lat) {
        this(false, id, name, lng, lat, 0, 0);
        mCounty = county;
        mArea = area;
    }
	
	public Depot(boolean isCenter, String id, String name, double lng, double lat) {
		this(isCenter, id, name, lng, lat, 0, 0);
	}
	
	public Depot(boolean isCenter, String id, String name, double lng, double lat, int pickupNo, int deliverNo) {
		mBelongCenter = isCenter;
		mLocationID = id;
		mName = name;
		mCoordinate = new Coordinate(lng, lat);
		mPickupCapacity = pickupNo;
		mDeliverCapacity = deliverNo;
	}
	
	public String getLocationID() { return mLocationID; }
	
	public String getName() { return (mNick == null) ? mName : mNick; }
	
	public double getLongitude() { return mCoordinate.getX(); }
	
	public double getLatitude() { return mCoordinate.getY(); }
	
	public boolean isCenter() { return mBelongCenter; }
	
	public int getPickupCapacity() { return mPickupCapacity; }
	
	public int getDeliverCapacity() { return mDeliverCapacity; }
	
	public int getPickupShipment() { return mPickupShipment; }
	
	public int getDropoffShipment() { return mDropoffShipment; }

	public String getCounty() { return mCounty; }
	
	public String getArea() { return mArea; }
	
	public void setNickName(String name) {
		mNick = name;
	}

	public void setPickupCapacity(int capacity) {
		mPickupCapacity = capacity;
	}
	
	public void setDeliverCapacity(int capacity) {
		mDeliverCapacity = capacity;
	}
	
	public void setPickupShipment(int amount) { mPickupShipment = amount; }
	
	public void setDropoffShipment(int amount) { mDropoffShipment = amount; }
	
	public String getAreaNumber() {
	    return mLocationID.substring(0, 3);
	}
	
	public boolean isSameLocation(Depot depot) {
	    if (depot == null) return false;
	    if (depot.getLatitude() != getLatitude()) return false;
	    if (depot.getLongitude() != getLongitude()) return false;
	    if (depot.getLocationID() != getLocationID()) return false;
	    if (depot.getName() != getName()) return false;
	    return true;
	}
	
	public String toLatLongString() {
		return String.format("%.8G, %.8G", getLatitude(), getLongitude());
	}
	
	public String toString() {
		return String.format("[%s] [%f, %f] %s - Pickup: %d, Deliver: %d", getLocationID(), getLongitude(), getLatitude(), getName(), getPickupCapacity(), getDeliverCapacity());
	}
}
