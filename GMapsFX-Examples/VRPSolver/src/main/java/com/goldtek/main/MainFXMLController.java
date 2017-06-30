package com.goldtek.main;

import com.goldtek.algorithm.Depot;
import com.goldtek.algorithm.IVrpSolver;
import com.goldtek.algorithm.Route;
import com.goldtek.greedy.GreedySolver;
import com.goldtek.algorithm.GT_Line;
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

public class MainFXMLController
		implements Initializable, MapComponentInitializedListener, DirectionsServiceCallback, IDragGuide {

	protected DirectionsService directionsService;
	protected DirectionsPane directionsPane;

	protected StringProperty from = new SimpleStringProperty();
	protected StringProperty to = new SimpleStringProperty();
	// protected List<DirectionsRenderer> directionsRenderers = new
	// ArrayList<>();
	// protected List<Marker> TotalMarkers = new ArrayList<>();
	protected ObservableList<Depot> depots;
	protected ObservableList<Image> depotImages;
	protected Depot mDragDepot = null;
	protected List<GT_Line> Any_Route_Marker = new ArrayList<>();
	@FXML
	protected ListView<Depot> RouteGuide;

	@FXML
	protected GoogleMapView mapView;

	@FXML
	private void handleMenu(ActionEvent event) {
		MenuItem item = ((MenuItem) event.getSource());
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
	private void HideTotal(ActionEvent event) {
		for (GT_Line lines : Any_Route_Marker) {
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
		for (GT_Line lines : Any_Route_Marker) {
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
		if (Any_Route_Marker.get(0).getVisible() == false) {
			DirectionsRenderer render = Any_Route_Marker.get(0).getRoute();
			render.setMap(mapView.getMap()); // show lines N0

			List<Marker> marker = Any_Route_Marker.get(0).getMarker();
			for (Marker markers : marker) {
				mapView.getMap().addMarker(markers); // show markers N0
			}
			Any_Route_Marker.get(0).setVisible(true);
		}
		if (Any_Route_Marker.get(1).getVisible() == true) {
			DirectionsRenderer render = Any_Route_Marker.get(1).getRoute();
			render.clearDirections(); // hide lines N1

			List<Marker> marker = Any_Route_Marker.get(1).getMarker();
			for (Marker markers : marker) {
				mapView.getMap().removeMarker(markers); // hide marker N1
			}
			Any_Route_Marker.get(1).setVisible(false);
		}
		if (Any_Route_Marker.get(2).getVisible() == true) {
			DirectionsRenderer render = Any_Route_Marker.get(2).getRoute();
			render.clearDirections(); // hide lines N2

			List<Marker> marker = Any_Route_Marker.get(2).getMarker();
			for (Marker markers : marker) {
				mapView.getMap().removeMarker(markers); // hide marker N2
			}
			Any_Route_Marker.get(2).setVisible(false);
		}
	}

	@FXML
	private void ShowB(ActionEvent event) {
		if (Any_Route_Marker.get(1).getVisible() == false) {
			DirectionsRenderer render = Any_Route_Marker.get(1).getRoute();
			render.setMap(mapView.getMap()); // show lines N1

			List<Marker> marker = Any_Route_Marker.get(1).getMarker();
			for (Marker markers : marker) {
				mapView.getMap().addMarker(markers); // show markers N1
			}
			Any_Route_Marker.get(1).setVisible(true);
		}
		if (Any_Route_Marker.get(0).getVisible() == true) {
			DirectionsRenderer render = Any_Route_Marker.get(0).getRoute();
			render.clearDirections(); // hide lines N0

			List<Marker> marker = Any_Route_Marker.get(0).getMarker();
			for (Marker markers : marker) {
				mapView.getMap().removeMarker(markers); // hide marker N0
			}
			Any_Route_Marker.get(0).setVisible(false);
		}
		if (Any_Route_Marker.get(2).getVisible() == true) {
			DirectionsRenderer render = Any_Route_Marker.get(2).getRoute();
			render.clearDirections(); // hide lines N2

			List<Marker> marker = Any_Route_Marker.get(2).getMarker();
			for (Marker markers : marker) {
				mapView.getMap().removeMarker(markers); // hide marker N2
			}
			Any_Route_Marker.get(2).setVisible(false);
		}
	}

	@FXML
	private void ShowC(ActionEvent event) {
		if (Any_Route_Marker.get(2).getVisible() == false) {
			DirectionsRenderer render = Any_Route_Marker.get(2).getRoute();
			render.setMap(mapView.getMap()); // show lines N2

			List<Marker> marker = Any_Route_Marker.get(2).getMarker();
			for (Marker markers : marker) {
				mapView.getMap().addMarker(markers); // show markers N2
			}
			Any_Route_Marker.get(2).setVisible(true);
		}
		if (Any_Route_Marker.get(0).getVisible() == true) {
			DirectionsRenderer render = Any_Route_Marker.get(0).getRoute();
			render.clearDirections(); // hide lines N0

			List<Marker> marker = Any_Route_Marker.get(0).getMarker();
			for (Marker markers : marker) {
				mapView.getMap().removeMarker(markers); // hide marker N0
			}
			Any_Route_Marker.get(0).setVisible(false);
		}
		if (Any_Route_Marker.get(1).getVisible() == true) {
			DirectionsRenderer render = Any_Route_Marker.get(1).getRoute();
			render.clearDirections(); // hide lines N1

			List<Marker> marker = Any_Route_Marker.get(1).getMarker();
			for (Marker markers : marker) {
				mapView.getMap().removeMarker(markers); // hide marker N1
			}
			Any_Route_Marker.get(1).setVisible(false);
		}
	}

	private void clearALL() {
		mapView.getMap().clearMarkers();
		for (GT_Line lines : Any_Route_Marker) {
			DirectionsRenderer render = lines.getRoute();
			render.clearDirections();
		}
		Any_Route_Marker.clear(); // clear any route markers
	}

	@FXML
	private void testAction(ActionEvent event) {
		clearALL();

		IVrpSolver solver = JspritSolver.getInstance();
		solver.reset();
		solver.inputFrom("input/zhonghe_test.xml");
		List<Route> routes = solver.solve(20);

		String[] colors = { "#000000", "#FF0000", "#00FF00", "#0000FF", "#808000", "#FF00FF", "#008000" };
		int colorsindex = 0;
		for (Route route : routes) {
			drawDriections(solver.getCenter(), solver.getCenter(), route, colors[colorsindex], routes.indexOf(route));
			colorsindex++;
			if (colorsindex > colors.length)
				colorsindex = 0;
		}

		if (routes.size() == 1) {
			depots = FXCollections.observableArrayList();
			depotImages = FXCollections.observableArrayList();

			depots.add(solver.getCenter());
			for (Depot depot : routes.get(0).getDepots())
				depots.add(depot);
			depots.add(solver.getCenter());

			depots.forEach(depot -> depotImages.add(GuideCell.textToImage(depot.getName())));
			// if (depots == null) System.out.println("null depots");
			RouteGuide.setItems(depots);
			RouteGuide.setCellFactory(param -> new GuideCell(MainFXMLController.this));
		}
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
				.overviewMapControl(true).streetViewControl(true).mapType(MapTypeIdEnum.ROADMAP);
		mapView.createMap(options);
		directionsService = new DirectionsService();
		directionsPane = mapView.getDirec();
	}

	public void drawDriections(Depot start, Depot end, Route route, String color, int labelindex) {
		GT_Line LineMarkers = new GT_Line();
		DirectionsRequest request = null;
		DirectionsWaypoint[] wayPoints = null;
		MarkerOptions markerOptions = new MarkerOptions();
		int Markindex = 0;
		// +++ add English labels on markers
		if (labelindex == 25)
			labelindex = 0;
		List<String> labelNum = new ArrayList<>();
		String[] label = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
				"S", "T", "U", "V", "W", "X", "Y", "Z" };
		for (int i = 1; i <= route.getDepots().size(); i++) {
			labelNum.add(label[labelindex] + i);
		}
		// --- add English labels on markers
		if (route != null) {
			wayPoints = new DirectionsWaypoint[route.getDepots().size()];
			for (int idx = 0; idx < route.getDepots().size(); idx++) {
				wayPoints[idx] = new DirectionsWaypoint(route.getDepots().get(idx).toLatLongString());
				markerOptions
						.position(new LatLong(route.getDepots().get(idx).getLatitude(),
								route.getDepots().get(idx).getLongitude()))
						.label(labelNum.get(Markindex++ % labelNum.size()));
				Marker marker = new Marker(markerOptions);

				mapView.getMap().addMarker(marker);
				System.out.println("marker=" + marker);
				LineMarkers.saveLineMarkers(marker); // add markers one Line
			}

			Any_Route_Marker.add(LineMarkers); // save any route markers
			request = new DirectionsRequest(start.toLatLongString(), end.toLatLongString(), TravelModes.DRIVING,
					wayPoints);
		} else {
			request = new DirectionsRequest(start.toLatLongString(), end.toLatLongString(), TravelModes.DRIVING);
		}
		// Add are Start & End markers on the map+++
		markerOptions.position(new LatLong(start.getLatitude(), start.getLongitude())).label("M")
				.icon("http://maps.google.com/mapfiles/kml/pal3/icon21.png");
		Marker marker = new Marker(markerOptions);
		mapView.getMap().addMarker(marker);

		DirectionsRenderer directionsRenderer = new DirectionsRenderer(false, mapView.getMap(), directionsPane, color);
		directionsService.getRoute(request, this, directionsRenderer);
		LineMarkers.saveRoute(directionsRenderer);

		LineMarkers.setVisible(true);
		// System.out.println("LineMarkers.getVisible()="+LineMarkers.getVisible());
		// System.out.println("Any_Route_Marker.get(0).getVisible()="+Any_Route_Marker.get(labelindex).getVisible());
		// System.out.println("Any_Route_Marker.get(1).getVisible()="+Any_Route_Marker.get(labelindex).getVisible());
		// System.out.println("Any_Route_Marker.get(2).getVisible()="+Any_Route_Marker.get(labelindex).getVisible());

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

			String[] colors = { "#438391", "#62a3cf", "#333366", "#770f5d", "#ffe246", "#9e379f", "#01aebf" };

			int index = 0;
			for (Route route : routes) {
				System.out.println("=== ROUTE === ");
				System.out.print("[ ");
				for (Depot depot : route.getDepots()) {
					System.out.print(depot.getLocationID() + " ");
				}
				System.out.println("]");

				drawDriections(solver.getCenter(), solver.getCenter(), route, colors[index], index);
				index++;
				if (index > colors.length)
					index = 0;
			}

			if (routes.size() >= 1) {
				depots = FXCollections.observableArrayList();
				depotImages = FXCollections.observableArrayList();

				Depot start = solver.getCenter();
				start.setNickName("First Car");
				depots.add(start);
				for (Depot depot : routes.get(0).getDepots())
					depots.add(depot);
				Depot end = solver.getCenter();
				end.setNickName("Center");
				depots.add(end);

				depots.forEach(depot -> depotImages.add(GuideCell.textToImage(depot.getName())));
				RouteGuide.setItems(depots);
				RouteGuide.setCellFactory(param -> new GuideCell(MainFXMLController.this));
			}

		}
	}
}