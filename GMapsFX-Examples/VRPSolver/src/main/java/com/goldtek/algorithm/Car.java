package com.goldtek.algorithm;

public class Car {
	private final String mID;
	private final int mBeginLocationID;
	private final double mBeginLongitude;
	private final double mBeginLatitude;
	private final int mCapacity;
	
	private int mEndLocationID;
	private double mEndLongitude;
	private double mEndLatitude;

	public Car(String name, int beginID, double beginLot, double beginLat, int capacity) {
		this(name, beginID, beginLot, beginLat, beginID, beginLot, beginLat, capacity);
	}
	
	public Car(String name, int beginID, double beginLot, double beginLat, int endID, double endLot, double endLat, int capacity) {
		mID = name;
		mBeginLocationID = beginID;
		mBeginLongitude = beginLot;
		mBeginLatitude = beginLat;
		mEndLocationID = endID;
		mEndLongitude = endLot;
		mEndLatitude = endLat;
		mCapacity = capacity;
	}
}
