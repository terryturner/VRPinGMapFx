package com.goldtek.algorithm;

import java.util.ArrayList;
import java.util.List;

import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.service.directions.DirectionsRenderer;

public class GT_Line {

	public GT_Line() {
	}

	private List<Marker> Markers = new ArrayList<>();
	private DirectionsRenderer directionsRenderer = null;
	private boolean visible = false;

	public boolean getVisible() {
		return visible;
	}

	public void setVisible(boolean vis) {
		visible = vis;
	}

	public void saveLineMarkers(Marker markers) {
		Markers.add(markers);
	}

	public void saveRoute(DirectionsRenderer Render) {
		directionsRenderer = Render;
	}

	public DirectionsRenderer getRoute() {
		return directionsRenderer;
	}

	public List<Marker> getMarker() {
		return Markers;
	}

	public void hideMarker(GoogleMap map) {
		for (Marker marker : Markers) {
			map.removeMarker(marker);
		}
	}

	public void addMarker(GoogleMap map) {
		for (Marker marker : Markers) {
			map.addMarker(marker);
		}
	}

	public void clearMarker(GoogleMap map) {
		map.clearMarkers();

	}

}
