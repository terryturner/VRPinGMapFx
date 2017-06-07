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
//peter+++
import java.util.Arrays;
//peter---

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
    
//    Peter+++
//    @FXML
//    protected TextField fromTextField;
//
//    @FXML
//    protected TextField toTextField;
//    Peter---

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
        GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyCBczmGGGZSij3NsT3kACmZc7fbuKJ7yeI");
//        context
//        	.setConnectTimeout(1, TimeUnit.SECONDS)
//        	.setReadTimeout(1, TimeUnit.SECONDS)
//        	.setWriteTimeout(1, TimeUnit.SECONDS);

//        ++++++
//        String[] origins = new String[]{
//                "台中", "Goldtek"
//            };
//            String[] destinations = new String[]{
//            		"台北", "Goldtek", "新北市中和區莒光路91號"
//            };
//            try {
//				DistanceMatrix matrix =
//				   DistanceMatrixApi.getDistanceMatrix(context, origins, destinations).await();
//				
//				for (int i=0; i < matrix.originAddresses.length; i++) {
//					DistanceMatrixElement[] dm = matrix.rows[i].elements;
//					for (int j=0; j < dm.length; j++) {
//						System.out.println(String.format("%s to %s: %s --- %s", matrix.originAddresses[i], matrix.destinationAddresses[j], dm[j].duration.humanReadable, dm[j].distance.humanReadable));
//					}
//				}
//			} catch (ApiException | InterruptedException | IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//            -------
            
//        GeocodingResult[] results;
//		try {
//			results = GeocodingApi.geocode(context,
//			        "1600 Amphitheatre Parkway Mountain View, CA 94043").await();
//			System.out.println(results[0].formattedAddress);  
//		} catch (ApiException | InterruptedException | IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    	
            //Peter ++ DirectionRequest
            /*DirectionsRequest request = new DirectionsRequest("台中", "高雄", TravelModes.DRIVING);
            directionsRenderer = new DirectionsRenderer(true, mapView.getMap(), directionsPane);
            directionsService.getRoute(request, this, directionsRenderer); */
            //-- DirectionRequest
            //++ DirectionsWatpoint
            //DirectionsWaypoint waypoint = new DirectionsWaypoint();
	        String addressOrigin = "23.982196, 121.616099";
	        String addressDestination = "南投";
        	String[] O = new String[]{
        			addressOrigin,
            		"阿里山", 
            		"陽明山", 
            		"積穗國小", 
            		"日月潭國家風景區", 
            		"合歡山",
            		addressDestination
            		};
            DirectionsWaypoint waypoint1 = new DirectionsWaypoint(O[1]);
            DirectionsWaypoint waypoint2 = new DirectionsWaypoint(O[2]);
            DirectionsWaypoint waypoint3 = new DirectionsWaypoint(O[3]);
            DirectionsWaypoint waypoint4 = new DirectionsWaypoint(O[4]);
            DirectionsWaypoint waypoint5 = new DirectionsWaypoint(O[5]);
            DirectionsWaypoint[] test = { waypoint1, waypoint2, waypoint3, waypoint4, waypoint5};
			DirectionsRequest request = new DirectionsRequest(addressOrigin, addressDestination, TravelModes.DRIVING, test);
            directionsRenderer = new DirectionsRenderer(true, mapView.getMap(), directionsPane);
            directionsService.getRoute(request, this, directionsRenderer); 
            System.out.println("=============================");
            try{
            DistanceMatrix matrix =
 				   DistanceMatrixApi.getDistanceMatrix(context, O, O).await();
            
            for (int i=0; i < O.length; i++) {
				DistanceMatrixElement[] dm = matrix.rows[i].elements;
				int j=i+1;
				System.out.println("j="+j);
				if(j>=O.length){
					j=0;
					System.out.println("if="+j);
				};
//				for (int j=0; j < dm.length; j++) {
				System.out.println(String.format("%s to %s: %s --- %s", matrix.originAddresses[i], matrix.destinationAddresses[j], dm[j].duration.humanReadable, dm[j].distance.humanReadable));
//				}			
				}
            } catch (ApiException | InterruptedException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}           
            //Peter-- DirectionsWatpoint
            
    }

    @Override
    public void directionsReceived(DirectionsResult results, DirectionStatus status) {
    	//System.out.println("direction got: " + results.getRoutes().get(0).getLegs().get(0).getDuration().getText());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mapView.addMapInializedListener(this);
        //mapView.setKey("AIzaSyAs71blnhxTVQj72XuGTgzkTIv5AqtDOlE");//terry
//        Peter+++
//        to.bindBidirectional(toTextField.textProperty());
//        from.bindBidirectional(fromTextField.textProperty());
//        Peter---
    }

    @Override
    public void mapInitialized() {
        MapOptions options = new MapOptions();

        options.center(new LatLong(24.997861, 121.486786))
                .zoomControl(false)
                .zoom(16)
                .overviewMapControl(false)
                .defaultUIControl(false)
                .draggableControl(true)
                .mapType(MapTypeIdEnum.ROADMAP);
        GoogleMap map = mapView.createMap(options);
        directionsService = new DirectionsService();
        directionsPane = mapView.getDirec();
    }

}