package com.goldtek.main;

import com.goldtek.algorithm.Depot;
import com.goldtek.algorithm.IVrpSolver;
import com.goldtek.algorithm.Route;
import com.goldtek.greedy.GreedySolver;
import com.goldtek.algorithm.GMapLine;
import com.goldtek.jsprit.JspritSolver;
import com.goldtek.main.config.ConfigDialog;
import com.goldtek.main.routeguide.GuideCell;
import com.goldtek.main.routeguide.IDragGuide;
import com.goldtek.main.routeguide.RouteColor;
import com.goldtek.main.routeguide.RouteLabel;
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

import java.io.File;
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
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainFXMLController
		implements Initializable, MapComponentInitializedListener, DirectionsServiceCallback, IDragGuide {

	protected DirectionsService directionsService;
	protected DirectionsPane directionsPane;

	protected StringProperty from = new SimpleStringProperty();
	protected StringProperty to = new SimpleStringProperty();

	protected ObservableList<Depot> depots;
	protected ObservableList<Image> depotImages;
	protected Depot mDragDepot = null;
	protected List<GMapLine> GMapLineList = new ArrayList<>();
	protected List<String> menubuttontext = new ArrayList<>();

	@FXML protected BorderPane RootPane;
    @FXML protected GoogleMapView mapView;
    @FXML protected ListView<Depot> RouteGuide;
    @FXML protected MenuButton MenuButton;

    private void updateMenubutton() {
    	MenuButton.getItems().clear();	//initialization menu button item text
    	MenuItem allitem = new MenuItem("All Routes");
    	MenuButton.getItems().add(allitem);
    	allitem.setOnAction(event -> {
    		for (GMapLine lines : GMapLineList) {	//Show Total Routes
    			if (lines.getVisible() == false) {
    				DirectionsRenderer render = lines.getRoute();
    				render.setMap(mapView.getMap()); // show lines

    				List<Marker> marker = lines.getMarker();
    				for (Marker markers : marker) {
    					mapView.getMap().addMarker(markers); // show markers
    				}
    				lines.setVisible(true);
    			}
    		}
    	});
    	for(String lines : menubuttontext){
    		MenuItem item = new MenuItem(lines);
    		MenuButton.getItems().addAll(item);	//add menu item to menu button
    		String idString = String.valueOf(menubuttontext.indexOf(lines));	//get loop index return id to String
    		item.setId(idString);	//set item idString
    		item.setOnAction(event -> {
    		    MenuItem itemid = (MenuItem) event.getSource();	//get event source
    		    String id = itemid.getId();	//get event source id
    		    int idint = Integer.parseInt(id);	//return idString to integer
    		    MenuButtonShow(idint);  //menu button action event functions
    		});
    	}
    	menubuttontext.clear();	//clear menu button text
    }
    
    
    
    
	private void MenuButtonShow(int show) {
    	for(int i=0; i < GMapLineList.size(); i++){
    	    DirectionsRenderer render = GMapLineList.get(i).getRoute();
    	    List<Marker> marker = GMapLineList.get(i).getMarker();
    	    
            if (i == show && GMapLineList.get(i).getVisible() == false) {
                render.setMap(mapView.getMap()); // show lines

                for (Marker markers : marker) {
                    mapView.getMap().addMarker(markers); // show markers
                }
                GMapLineList.get(i).setVisible(true);
            } else if (i != show && GMapLineList.get(i).getVisible() == true) {
                render.clearDirections(); // hide lines

                for (Marker markers : marker) {
                    mapView.getMap().removeMarker(markers); // hide markers
                }
                GMapLineList.get(i).setVisible(false);
            }
    	}
	}
    
    
    
    @FXML
	private void handleMenu(ActionEvent event) {
		MenuItem item = ((MenuItem) event.getSource());
		switch (item.getId()) {
		case "MenuExit":
			System.exit(0);
			break;
		case "MenuConfig":
		    ConfigDialog dialog = new ConfigDialog(RootPane.getScene().getWindow());
			dialog.show();
			break;
		default:
			break;
		}
	}

	@FXML
	private void HideTotal(ActionEvent event) {
		for (GMapLine lines : GMapLineList) {
			if (lines.getVisible() == true) {
				DirectionsRenderer render = lines.getRoute();
				render.clearDirections(); // hide lines

				List<Marker> markers = lines.getMarker();
				for (Marker marker : markers) {
					mapView.getMap().removeMarker(marker); // hide markers
				}
				lines.setVisible(false);
			}
		}
	}

	@FXML
	private void ShowTotal(ActionEvent event) {
		for (GMapLine lines : GMapLineList) {
			if (lines.getVisible() == false) {
				DirectionsRenderer render = lines.getRoute();
				render.setMap(mapView.getMap()); // show lines

				List<Marker> marker = lines.getMarker();
				for (Marker markers : marker) {
					mapView.getMap().addMarker(markers); // show markers
				}
				lines.setVisible(true);
			}
		}
	}

	@FXML
	private void ShowA(ActionEvent event) {
		if (GMapLineList.get(0).getVisible() == false) {
			DirectionsRenderer render = GMapLineList.get(0).getRoute();
			render.setMap(mapView.getMap()); // show lines N0

			List<Marker> marker = GMapLineList.get(0).getMarker();
			for (Marker markers : marker) {
				mapView.getMap().addMarker(markers); // show markers N0
			}
			GMapLineList.get(0).setVisible(true);
		}
		if (GMapLineList.get(1).getVisible() == true) {
			DirectionsRenderer render = GMapLineList.get(1).getRoute();
			render.clearDirections(); // hide lines N1

			List<Marker> marker = GMapLineList.get(1).getMarker();
			for (Marker markers : marker) {
				mapView.getMap().removeMarker(markers); // hide marker N1
			}
			GMapLineList.get(1).setVisible(false);
		}
		if (GMapLineList.get(2).getVisible() == true) {
			DirectionsRenderer render = GMapLineList.get(2).getRoute();
			render.clearDirections(); // hide lines N2

			List<Marker> marker = GMapLineList.get(2).getMarker();
			for (Marker markers : marker) {
				mapView.getMap().removeMarker(markers); // hide marker N2
			}
			GMapLineList.get(2).setVisible(false);
		}
	}

	@FXML
	private void ShowB(ActionEvent event) {
		if (GMapLineList.get(1).getVisible() == false) {
			DirectionsRenderer render = GMapLineList.get(1).getRoute();
			render.setMap(mapView.getMap()); // show lines N1

			List<Marker> marker = GMapLineList.get(1).getMarker();
			for (Marker markers : marker) {
				mapView.getMap().addMarker(markers); // show markers N1
			}
			GMapLineList.get(1).setVisible(true);
		}
		if (GMapLineList.get(0).getVisible() == true) {
			DirectionsRenderer render = GMapLineList.get(0).getRoute();
			render.clearDirections(); // hide lines N0

			List<Marker> marker = GMapLineList.get(0).getMarker();
			for (Marker markers : marker) {
				mapView.getMap().removeMarker(markers); // hide marker N0
			}
			GMapLineList.get(0).setVisible(false);
		}
		if (GMapLineList.get(2).getVisible() == true) {
			DirectionsRenderer render = GMapLineList.get(2).getRoute();
			render.clearDirections(); // hide lines N2

			List<Marker> marker = GMapLineList.get(2).getMarker();
			for (Marker markers : marker) {
				mapView.getMap().removeMarker(markers); // hide marker N2
			}
			GMapLineList.get(2).setVisible(false);
		}
	}

	@FXML
	private void ShowC(ActionEvent event) {
		if (GMapLineList.get(2).getVisible() == false) {
			DirectionsRenderer render = GMapLineList.get(2).getRoute();
			render.setMap(mapView.getMap()); // show lines N2

			List<Marker> marker = GMapLineList.get(2).getMarker();
			for (Marker markers : marker) {
				mapView.getMap().addMarker(markers); // show markers N2
			}
			GMapLineList.get(2).setVisible(true);
		}
		if (GMapLineList.get(0).getVisible() == true) {
			DirectionsRenderer render = GMapLineList.get(0).getRoute();
			render.clearDirections(); // hide lines N0

			List<Marker> marker = GMapLineList.get(0).getMarker();
			for (Marker markers : marker) {
				mapView.getMap().removeMarker(markers); // hide marker N0
			}
			GMapLineList.get(0).setVisible(false);
		}
		if (GMapLineList.get(1).getVisible() == true) {
			DirectionsRenderer render = GMapLineList.get(1).getRoute();
			render.clearDirections(); // hide lines N1

			List<Marker> marker = GMapLineList.get(1).getMarker();
			for (Marker markers : marker) {
				mapView.getMap().removeMarker(markers); // hide marker N1
			}
			GMapLineList.get(1).setVisible(false);
		}
	}

	private void clearAll() {
		mapView.getMap().clearMarkers();
		for (GMapLine lines : GMapLineList) {
			DirectionsRenderer render = lines.getRoute();
			render.clearDirections();
		}
		GMapLineList.clear(); // clear any route markers
	}

	@FXML
	private void testAction(ActionEvent event) {
	    String inputPath = "input/zhonghe_test.xml";
		clearAll();
		
		if (!FileHandle.getInstance().isExists(inputPath)) {
		    File file = FileHandle.getInstance().showXMLChooser(RootPane.getScene().getWindow());
		    if (file == null) return;
		    else inputPath = file.getPath();
		}

		IVrpSolver solver = JspritSolver.getInstance();
		//IVrpSolver solver = GreedySolver.getInstance();
		solver.reset();
		solver.inputFrom(inputPath);
		List<Route> routes = solver.solve(20);
		for(Route line : routes){	//add English route label to array list 
			menubuttontext.add("Route "+RouteLabel.getInstance().get(routes.indexOf(line)));
		}
		afterSolve(solver, routes);
		updateMenubutton();
	}

	@Override
	public void directionsReceived(DirectionsResult results, DirectionStatus status) {
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		mapView.addMapInializedListener(this);
	}

	@Override
	public void mapInitialized() {
		MapOptions options = new MapOptions();

		options.center(new LatLong(24.997861, 121.486786)).zoomControl(true).mapTypeControl(false).zoom(14)
				.overviewMapControl(true).streetViewControl(false).doubleClickZoomControl(false).mapType(MapTypeIdEnum.ROADMAP);
		mapView.createMap(options);
		directionsService = new DirectionsService();
		directionsPane = mapView.getDirec();
	}


	public void getduration(String[] waypoints) {
		GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyCBczmGGGZSij3NsT3kACmZc7fbuKJ7yeI");
		try {
			DistanceMatrix matrix = DistanceMatrixApi.getDistanceMatrix(context, waypoints, waypoints).await();

			for (int i = 0; i < matrix.originAddresses.length; i++) {
				DistanceMatrixElement[] dm = matrix.rows[i].elements;
				for (int j = 0; j < dm.length; j++) {
					System.out.println(String.format("%s ===TO=== %s => %s --- %s", matrix.originAddresses[i],
							matrix.destinationAddresses[j], dm[j].duration.humanReadable,
							dm[j].distance.humanReadable));
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
			for (Route route : routes) {
			    int index = routes.indexOf(route);
				drawDriections(solver.getCenter(index), solver.getCenter(index), route);
			}

			if (routes.size() >= 1) {
				depots = FXCollections.observableArrayList();
				depotImages = FXCollections.observableArrayList();

				Depot start = solver.getCenter(0);
				start.setNickName("First Car");
				depots.add(start);
				for (Depot depot : routes.get(0).getDepots()){
					depots.add(depot);
				}
				Depot end = solver.getCenter(0);
				end.setNickName("Center");
				depots.add(end);

				for (Depot depot : depots) {
				    System.out.println("dbg: " + depot.toString());;
				}
				depots.forEach(depot -> depotImages.add(GuideCell.textToImage(depot.getName())));
				RouteGuide.setItems(depots);
				RouteGuide.setCellFactory(param -> new GuideCell(MainFXMLController.this));
			}

		}
	}

    public void drawDriections(Depot start, Depot end, Route route) {
        GMapLine LineMarkers = new GMapLine();
        DirectionsRequest request = null;
        DirectionsWaypoint[] wayPoints = null;
        MarkerOptions markerOptions = new MarkerOptions();

        if (route != null) {
            wayPoints = new DirectionsWaypoint[route.getDepots().size()];
            for (int idx = 0; idx < route.getDepots().size(); idx++) {
                wayPoints[idx] = new DirectionsWaypoint(route.getDepots().get(idx).toLatLongString());
                markerOptions
                        .position(new LatLong(route.getDepots().get(idx).getLatitude(),
                                route.getDepots().get(idx).getLongitude()))
                        .label(RouteLabel.getInstance().getLabel(route.getId(), idx+1));

                Marker marker = new Marker(markerOptions);

                mapView.getMap().addMarker(marker);
                LineMarkers.saveLineMarkers(marker); // add markers one Line
            }

            GMapLineList.add(LineMarkers); // save any route markers
            request = new DirectionsRequest(start.toLatLongString(), end.toLatLongString(), TravelModes.DRIVING,
                    wayPoints);
        } else {
            request = new DirectionsRequest(start.toLatLongString(), end.toLatLongString(), TravelModes.DRIVING);
        }
        // Add are Start & End markers on the map+++
        markerOptions.position(new LatLong(start.getLatitude(), start.getLongitude()))
                .icon("http://maps.google.com/mapfiles/kml/pal3/icon21.png");
        Marker marker = new Marker(markerOptions);
        mapView.getMap().addMarker(marker);

        DirectionsRenderer directionsRenderer = new DirectionsRenderer(false, mapView.getMap(), directionsPane, RouteColor.getInstance().get(route.getId()));
        directionsService.getRoute(request, this, directionsRenderer);
        LineMarkers.saveRoute(directionsRenderer);

        LineMarkers.setVisible(true);
    }
}