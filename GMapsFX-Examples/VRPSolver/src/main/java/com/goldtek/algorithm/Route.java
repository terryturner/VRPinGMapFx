package com.goldtek.algorithm;

import java.util.ArrayList;
import java.util.List;

public class Route {
    private final int ID;
    private final String Driver;
	private List<Depot> depots = new ArrayList<>();
	
	public Route(int id, String driver) {
	    ID = id;
	    Driver = driver;
	}
	
	public int getId() { return ID; }
	
	public String getDriver() { return Driver; }
	
	public void addDepot(Depot depot) {
		depots.add(depot);
	}
	
	public List<Depot> getDepots() {
		return depots;
	}
	
	public Depot getLastDepot() {
		if (depots.size() <= 0) return null;
		else return depots.get(depots.size()-1);
	}
}
