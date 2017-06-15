package com.lynden.example.directions;


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
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
//peter+++
import java.util.Arrays;
//+++
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import com.google.maps.DirectionsApi.RouteRestriction;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElementStatus;
import com.google.maps.model.TrafficModel;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;

//---
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
    	
//    	mapView.setCenter(24.997861, 121.486786);
//        GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyCBczmGGGZSij3NsT3kACmZc7fbuKJ7yeI"); //Peter key
//        GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyAs71blnhxTVQj72XuGTgzkTIv5AqtDOlE"); //Terry Key
//        context
//        	.setConnectTimeout(1, TimeUnit.SECONDS)
//        	.setReadTimeout(1, TimeUnit.SECONDS)
//        	.setWriteTimeout(1, TimeUnit.SECONDS)
//        	.setRetryTimeout(1, TimeUnit.SECONDS)
//        	.setQueryRateLimit(3);

//        Peter++++++
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
//            Peter-------
            
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
//	        String addressOrigin = "臺北小巨蛋";
//	        String addressDestination = "老梅綠石槽";
        	String[] O = new String[]{
            		"臺北松山機場", 
            		"實踐大學 台北校區", 
            		"V_K 美麗華店", 
            		"士林官邸", 
            		"國立故宮博物院",
//            		"台北市立天文科學教育館", 
//            		"芝山巖", 
//            		"臺北榮民總醫院",
//            		"天母棒球場",
//            		"台灣國立陽明大學", 
//            		"北投公園", 
//            		"國防大學管理學院", 
//            		"臺北市立復興高級中學", 
//            		"地熱谷",
//            		"皇池溫泉御膳館", 
//            		"中國文化大學", 
//            		"擎天崗大草原",
//            		"陽明山國家公園遊客中心",
//            		"金山財神廟",
//            		"法鼓山世界佛教教育園區", 
//            		"朱銘美術館", 
//            		"北海高爾夫鄉村俱樂部",
//            		"濱海高爾夫球場",
            		};
        drawdirections("臺北小巨蛋", "老梅綠石槽", O); //drawdirections(String start, String end, String[] waypoints)
        getduration(O); //getduration(String[] waypoints)


//            DirectionsWaypoint[] waypointarray = new DirectionsWaypoint[O.length];
//            for(int i=0; i < O.length; i++){
//            	waypointarray[i] =  new DirectionsWaypoint(O[i]);
//            }
            
//            DirectionsWaypoint[] test = { waypoint1, waypoint2, waypoint3, waypoint4, waypoint5, 
//            		 waypoint6, waypoint7, waypoint8
//            		};

//			DirectionsRequest request = new DirectionsRequest(addressOrigin, addressDestination, TravelModes.DRIVING, waypointarray);
//            directionsRenderer = new DirectionsRenderer(true, mapView.getMap(), directionsPane);
//            directionsService.getRoute(request, this, directionsRenderer); 
//            System.out.println("=============================");
//        	try {
//				DistanceMatrix matrix =
//				   DistanceMatrixApi.getDistanceMatrix(context, O, O).await();
//				
//				for (int i=0; i < matrix.originAddresses.length; i++) {
//					DistanceMatrixElement[] dm = matrix.rows[i].elements;
//					for (int j=0; j < dm.length; j++) {
//						System.out.println(String.format("%s ===TO=== %s => %s --- %s", matrix.originAddresses[i], matrix.destinationAddresses[j], dm[j].duration.humanReadable, dm[j].distance.humanReadable));
//					}
//				}
//			} catch (ApiException | InterruptedException | IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} 
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
    

    public void drawdirections(String start, String end, String[] waypoints){
    	
        DirectionsWaypoint[] waypointarray = new DirectionsWaypoint[waypoints.length];
        
        for(int i=0; i < waypoints.length; i++){
        	waypointarray[i] =  new DirectionsWaypoint(waypoints[i]);
        }        

		DirectionsRequest request = new DirectionsRequest(start, end, TravelModes.DRIVING, waypointarray);
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