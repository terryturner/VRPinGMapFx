package com.goldtek.main;


import com.goldtek.algorithm.Depot;
import com.goldtek.algorithm.IVrpSolver;
import com.goldtek.algorithm.Route;
import com.goldtek.greedy.GreedySolver;
import com.goldtek.jsprit.JspritSolver;
import com.goldtek.main.routeguide.GuideCell;
import com.goldtek.main.routeguide.IDragGuide;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;


import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainFXMLController implements Initializable, MapComponentInitializedListener, DirectionsServiceCallback, IDragGuide {

    protected DirectionsService directionsService;
    protected DirectionsPane directionsPane;

    protected StringProperty from = new SimpleStringProperty();
    protected StringProperty to = new SimpleStringProperty();
    protected List<DirectionsRenderer> directionsRenderers = new ArrayList<>();

    protected ObservableList<Depot> depots;
    protected ObservableList<Image> depotImages;
    protected Depot mDragDepot = null;
    
    @FXML protected BorderPane RootPane;
    @FXML protected GoogleMapView mapView;
    @FXML protected ListView<Depot> RouteGuide;
    
    @FXML
    private void handleMenu(ActionEvent event) {
    	MenuItem item = ((MenuItem)event.getSource());
    	switch (item.getId()) {
		case "MenuExit":
			System.exit(0);
			break;
	     case "MenuConfig":
	            final Stage dialog = new Stage();
	            dialog.initModality(Modality.APPLICATION_MODAL);
	            dialog.initOwner(mapView.getScene().getWindow());
	            VBox dialogVbox = new VBox(20);
	            dialogVbox.getChildren().add(new Text("This is a Dialog"));
	            Scene dialogScene = new Scene(dialogVbox, 300, 200);
	            dialog.setScene(dialogScene);
	            dialog.show();
	            break;
		default:
			break;
		}
    }
    
    @FXML
    private void clearDirections(ActionEvent event) {
    	for (DirectionsRenderer render : directionsRenderers)
    		render.clearDirections();
    	directionsRenderers.clear();
    }
    
    @FXML
    private void testAction(ActionEvent event) {
        //IVrpSolver solver = JspritSolver.getInstance();
        IVrpSolver solver = GreedySolver.getInstance();
        solver.reset();
        solver.inputFrom("input/zhonghe_test.xml");
        //solver.inputFrom("input/goldtek_zhonghe.xml");
        List<Route> routes = solver.solve(200);
        
        afterSolve(solver, routes);
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
                .mapTypeControl(false)
                .zoom(16)
                .overviewMapControl(true)
                .streetViewControl(true)
                .mapType(MapTypeIdEnum.ROADMAP);
        mapView.createMap(options);
        directionsService = new DirectionsService();
        directionsPane = mapView.getDirec();
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

	@Override
	public Image getImage(int index) {
		return depotImages.get(index);
	}

	@Override
	public void setImage(int index, Image image) {
		depotImages.set(index, image);
	}

	@Override
	public void OnDragDetected(Depot item) {
		mDragDepot = item;
	}

	@Override
	public Depot getDragItem() {
		return mDragDepot;
	}
    
	private void afterSolve(IVrpSolver solver, List<Route> routes) {
        if (routes != null) {

            String[] colors= { "#438391", "#62a3cf", "#333366", "#770f5d", "#ffe246", "#9e379f", "#01aebf" };
            
            int index = 0;
            for (Route route : routes) {
                System.out.println("=== ROUTE === ");
                System.out.print("[ ");
                for (Depot depot : route.getDepots()) {
                    System.out.print(depot.getLocationID() + " ");
                }
                System.out.println("]");
                
                drawDriections(solver.getCenter(), solver.getCenter(), route, colors[index]);
                index++;
                if (index > colors.length) index = 0;
            }
            
            if (routes.size() >= 1) {
                depots = FXCollections.observableArrayList();
                depotImages = FXCollections.observableArrayList();

                Depot start = solver.getCenter();
                start.setNickName("First Car");
                depots.add(start);
                for (Depot depot : routes.get(0).getDepots()) depots.add(depot);
                Depot end = solver.getCenter();
                end.setNickName("Center");
                depots.add(end);    
                
                depots.forEach(depot -> depotImages.add(GuideCell.textToImage(depot.getName())));
                RouteGuide.setItems(depots);
                RouteGuide.setCellFactory(param -> new GuideCell(MainFXMLController.this));
            }

        }
    }
	
	public void drawDriections(Depot start, Depot end, Route route, String color) {
        DirectionsRequest request = null;
        DirectionsWaypoint[] wayPoints = null; 
        MarkerOptions markerOptions = new MarkerOptions();
        String label[] = {"A11", "A22", "A33", "A44", "A55", "A66", "A77", "A88", "A99"};
        int labelIndex = 0;
        if (route != null) {
            wayPoints = new DirectionsWaypoint[route.getDepots().size()];
            for (int idx = 0; idx < route.getDepots().size(); idx++) {
                wayPoints[idx] = new DirectionsWaypoint(route.getDepots().get(idx).toLatLongString());

                 //Add are waypoint markers on the map+++
                    markerOptions.position( new LatLong(route.getDepots().get(idx).getLatitude(), route.getDepots().get(idx).getLongitude()) )
                                .label(label[labelIndex++ % label.length]);
                    Marker marker = new Marker( markerOptions );

                    mapView.getMap().addMarker(marker); 
                  //Add are waypoint markers on the map---
            }           
            request = new DirectionsRequest(start.toLatLongString(), end.toLatLongString(), TravelModes.DRIVING, wayPoints);
        } else {
            request = new DirectionsRequest(start.toLatLongString(), end.toLatLongString(), TravelModes.DRIVING);
        }
        
         //Add are Start & End markers on the map+++
        markerOptions.position( new LatLong(start.getLatitude(), start.getLongitude()) )
                    .label("中")
                    .icon("http://maps.google.com/mapfiles/kml/pal3/icon21.png");
        Marker markerS = new Marker( markerOptions );
        mapView.getMap().addMarker(markerS);
        ////Add are Start & End markers on the map---
        
        DirectionsRenderer directionsRenderer = new DirectionsRenderer(false, mapView.getMap(), directionsPane, color);
        directionsService.getRoute(request, this, directionsRenderer);
        directionsRenderers.add(directionsRenderer);

    }
}