package com.goldtek.algorithm;

import java.util.ArrayList;

import javafx.collections.ObservableList;

public class ColorfulDepot {
	private Depot mDepot;
	private int mColorIndex;
	private int mDepotPoint;

	public enum ListViewID {
		START, WAYPOINT, END
	};

	private ListViewID mListViewID;

	public ColorfulDepot(int colorIndex, Depot depot, ListViewID id) {
		this.mDepot = depot;
		this.mColorIndex = colorIndex;
		this.mListViewID = id;

	}

	public Depot getDepot() {
		return mDepot;
	}

	public int getIndex() {
		return mColorIndex;
	}

	public void setDepotPoint(int depotpoint) {
		this.mDepotPoint = depotpoint + 1;
	}

	public int getDepotPoint() {
		return mDepotPoint;
	}

	public ListViewID getListViewID() {
		return mListViewID;
	}

}
