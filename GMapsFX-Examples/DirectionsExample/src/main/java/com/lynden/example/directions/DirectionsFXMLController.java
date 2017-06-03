package com.lynden.example.directions;


import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.google.maps.model.GeocodingResult;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.*;
import com.lynden.gmapsfx.service.directions.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

public class DirectionsFXMLController implements Initializable, MapComponentInitializedListener, DirectionsServiceCallback {

    protected DirectionsService directionsService;
    protected DirectionsPane directionsPane;

    protected StringProperty from = new SimpleStringProperty();
    protected StringProperty to = new SimpleStringProperty();
    protected DirectionsRenderer directionsRenderer = null;

    @FXML
    protected GoogleMapView mapView;

    @FXML
    protected TextField fromTextField;

    @FXML
    protected TextField toTextField;

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
    	
    	mapView.setCenter(24.997861, 121.486786);
    	GeoAPI api = new GeoAPI();
    	//api.testDistanceMatrix();
    	jsprit algo = new jsprit();
    	//algo.start();
    }

    @Override
    public void directionsReceived(DirectionsResult results, DirectionStatus status) {
    	//System.out.println("direction got: " + results.getRoutes().get(0).getLegs().get(0).getDuration().getText());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mapView.addMapInializedListener(this);
        //mapView.setKey("AIzaSyAs71blnhxTVQj72XuGTgzkTIv5AqtDOlE");//terry
        to.bindBidirectional(toTextField.textProperty());
        from.bindBidirectional(fromTextField.textProperty());
    }

    @Override
    public void mapInitialized() {
        MapOptions options = new MapOptions();

        options.center(new LatLong(24.997861, 121.486786))
                .zoomControl(false)
                .zoom(16)
                .overviewMapControl(false)
                .defaultUIControl(false)
                .draggableControl(false)
                .mapType(MapTypeIdEnum.ROADMAP);
        GoogleMap map = mapView.createMap(options);
        directionsService = new DirectionsService();
        directionsPane = mapView.getDirec();
    }

}