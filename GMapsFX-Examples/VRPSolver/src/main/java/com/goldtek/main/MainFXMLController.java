package com.goldtek.main;


import com.goldtek.algorithm.Depot;
import com.goldtek.algorithm.IVrpSolver;
import com.goldtek.algorithm.Route;
import com.goldtek.jsprit.JspritSolver;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.GeocodedWaypointStatus;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.*;
import com.lynden.gmapsfx.service.directions.*;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

public class MainFXMLController implements Initializable, MapComponentInitializedListener, DirectionsServiceCallback {

    	protected DirectionsService directionsService;
    protected DirectionsPane directionsPane;

    protected StringProperty from = new SimpleStringProperty();
    protected StringProperty to = new SimpleStringProperty();
    protected DirectionsRenderer directionsRenderer = null;

    @FXML
    protected GoogleMapView mapView;
    
    @FXML
    private void toTextFieldAction(ActionEvent event) {
        DirectionsRequest request = new DirectionsRequest(from.get(), to.get(), TravelModes.DRIVING);
        directionsRenderer = new DirectionsRenderer(true, mapView.getMap(), directionsPane);
        directionsService.getRoute(request, this, directionsRenderer);
    }
    
    @FXML
    private void clearDirections(ActionEvent event) {
        directionsRenderer.clearDirections();
    }
    
    @FXML
    private void testAction(ActionEvent event) {
    	IVrpSolver solver = JspritSolver.getInstance();
    	solver.reset();
    	solver.inputFrom("input/zhonghe_test.xml");
    	List<Route> routes = solver.solve(20);
    	
    	for (Route route : routes) {
    		System.out.println("=== ROUTE ===");
    		System.out.print("[ ");
    		for (Depot depot : route.getDepots()) {
    			System.out.print(depot.getLocationID() + " ");
    		}
    		System.out.println("]");
    		drawDriections(solver.getCenter(), solver.getCenter(), route);
    	}
    	
        //getduration(O); //getduration(String[] waypoints)
    }

    @Override
    public void directionsReceived(DirectionsResult results, DirectionStatus status) {}

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mapView.addMapInializedListener(this);
    }

    @Override
    public void mapInitialized() {
        MapOptions options = new MapOptions();

        options.center(new LatLong(24.997861, 121.486786))
                .zoomControl(true)
                .zoom(16)
                .overviewMapControl(true)
                .streetViewControl(true)
                .draggableControl(true)
                .mapType(MapTypeIdEnum.ROADMAP);
        GoogleMap map = mapView.createMap(options);
        directionsService = new DirectionsService();
        directionsPane = mapView.getDirec();
    }
    
    public void drawDriections(Depot start, Depot end, Route route) {
    	DirectionsRequest request = null;
    	DirectionsWaypoint[] wayPoints = null;
		if (route != null) {
			wayPoints = new DirectionsWaypoint[route.getDepots().size()];
			for (int idx = 0; idx < route.getDepots().size(); idx++) {
				wayPoints[idx] = new DirectionsWaypoint(route.getDepots().get(idx).toLatLongString());
			}
			
			request = new DirectionsRequest(start.toLatLongString(), end.toLatLongString(), TravelModes.DRIVING, wayPoints);
		} else {
			request = new DirectionsRequest(start.toLatLongString(), end.toLatLongString(), TravelModes.DRIVING);
		}

		directionsRenderer = new DirectionsRenderer(true, mapView.getMap(), directionsPane);
        directionsService.getRoute(request, this, directionsRenderer);
    }
    public void getduration(String[] waypoints){
    	GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyCBczmGGGZSij3NsT3kACmZc7fbuKJ7yeI");
    	try {
			DistanceMatrix matrix =
			   DistanceMatrixApi.getDistanceMatrix(context, waypoints, waypoints).await();
			
			for (int i=0; i < matrix.originAddresses.length; i++) {
				DistanceMatrixElement[] dm = matrix.rows[i].elements;
				for (int j=0; j < dm.length; j++) {
					System.out.println(String.format("%s ===TO=== %s => %s --- %s", matrix.originAddresses[i], matrix.destinationAddresses[j], dm[j].duration.humanReadable, dm[j].distance.humanReadable));
				}
			}
		} catch (ApiException | InterruptedException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }
    
    
}