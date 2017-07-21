package com.goldtek.main;

import com.goldtek.jsprit.JspritSolver;
import com.goldtek.main.config.ConfigDialog;
import com.goldtek.main.routeguide.RouteColor;
import com.goldtek.main.routeguide.RouteLabel;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.*;
import javafx.util.Callback;
import com.goldtek.main.routeguide.*;
import com.goldtek.algorithm.*;
import com.goldtek.algorithm.ColorfulDepot.ListViewID;
//---------

public class MainFXMLController
		implements Initializable, MapComponentInitializedListener, DirectionsServiceCallback {
    private final static String TITLE_ALL_ROUTE = "All Routes";

	protected DirectionsService mDirectionsService;
	protected DirectionsPane mDirectionsPane;

	protected Depot mDragDepot = null;
	protected List<GMapLine> mGMapLineList = new ArrayList<>();
	protected List<String> mMenuButtonText = new ArrayList<>();

	protected ObservableList<ColorfulDepot> mCurrentRouteGuide = FXCollections.observableArrayList();
	protected List<Route> mSaveRoutes = new ArrayList<>();
	protected IVrpSolver mSolver = JspritSolver.getInstance();

	@FXML protected BorderPane RootPane;
	@FXML protected GoogleMapView mapView;
	@FXML protected ListView<ColorfulDepot> RouteGuide;
	@FXML protected MenuButton MenuButton;

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
		for (GMapLine lines : mGMapLineList) {
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
		for (GMapLine lines : mGMapLineList) {
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
		if (mGMapLineList.get(0).getVisible() == false) {
			DirectionsRenderer render = mGMapLineList.get(0).getRoute();
			render.setMap(mapView.getMap()); // show lines N0

			List<Marker> marker = mGMapLineList.get(0).getMarker();
			for (Marker markers : marker) {
				mapView.getMap().addMarker(markers); // show markers N0
			}
			mGMapLineList.get(0).setVisible(true);
		}
		if (mGMapLineList.get(1).getVisible() == true) {
			DirectionsRenderer render = mGMapLineList.get(1).getRoute();
			render.clearDirections(); // hide lines N1

			List<Marker> marker = mGMapLineList.get(1).getMarker();
			for (Marker markers : marker) {
				mapView.getMap().removeMarker(markers); // hide marker N1
			}
			mGMapLineList.get(1).setVisible(false);
		}
		if (mGMapLineList.get(2).getVisible() == true) {
			DirectionsRenderer render = mGMapLineList.get(2).getRoute();
			render.clearDirections(); // hide lines N2

			List<Marker> marker = mGMapLineList.get(2).getMarker();
			for (Marker markers : marker) {
				mapView.getMap().removeMarker(markers); // hide marker N2
			}
			mGMapLineList.get(2).setVisible(false);
		}
	}

	@FXML
	private void ShowB(ActionEvent event) {
		if (mGMapLineList.get(1).getVisible() == false) {
			DirectionsRenderer render = mGMapLineList.get(1).getRoute();
			render.setMap(mapView.getMap()); // show lines N1

			List<Marker> marker = mGMapLineList.get(1).getMarker();
			for (Marker markers : marker) {
				mapView.getMap().addMarker(markers); // show markers N1
			}
			mGMapLineList.get(1).setVisible(true);
		}
		if (mGMapLineList.get(0).getVisible() == true) {
			DirectionsRenderer render = mGMapLineList.get(0).getRoute();
			render.clearDirections(); // hide lines N0

			List<Marker> marker = mGMapLineList.get(0).getMarker();
			for (Marker markers : marker) {
				mapView.getMap().removeMarker(markers); // hide marker N0
			}
			mGMapLineList.get(0).setVisible(false);
		}
		if (mGMapLineList.get(2).getVisible() == true) {
			DirectionsRenderer render = mGMapLineList.get(2).getRoute();
			render.clearDirections(); // hide lines N2

			List<Marker> marker = mGMapLineList.get(2).getMarker();
			for (Marker markers : marker) {
				mapView.getMap().removeMarker(markers); // hide marker N2
			}
			mGMapLineList.get(2).setVisible(false);
		}
	}

	@FXML
	private void ShowC(ActionEvent event) {
		if (mGMapLineList.get(2).getVisible() == false) {
			DirectionsRenderer render = mGMapLineList.get(2).getRoute();
			render.setMap(mapView.getMap()); // show lines N2

			List<Marker> marker = mGMapLineList.get(2).getMarker();
			for (Marker markers : marker) {
				mapView.getMap().addMarker(markers); // show markers N2
			}
			mGMapLineList.get(2).setVisible(true);
		}
		if (mGMapLineList.get(0).getVisible() == true) {
			DirectionsRenderer render = mGMapLineList.get(0).getRoute();
			render.clearDirections(); // hide lines N0

			List<Marker> marker = mGMapLineList.get(0).getMarker();
			for (Marker markers : marker) {
				mapView.getMap().removeMarker(markers); // hide marker N0
			}
			mGMapLineList.get(0).setVisible(false);
		}
		if (mGMapLineList.get(1).getVisible() == true) {
			DirectionsRenderer render = mGMapLineList.get(1).getRoute();
			render.clearDirections(); // hide lines N1

			List<Marker> marker = mGMapLineList.get(1).getMarker();
			for (Marker markers : marker) {
				mapView.getMap().removeMarker(markers); // hide marker N1
			}
			mGMapLineList.get(1).setVisible(false);
		}
	}

	private void clearAll() {
		mapView.getMap().clearMarkers();
		for (GMapLine lines : mGMapLineList) {
			DirectionsRenderer render = lines.getRoute();
			render.clearDirections();
		}
		mGMapLineList.clear(); // clear any route markers
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

		// IVrpSolver solver = JspritSolver.getInstance();
		// IVrpSolver solver = GreedySolver.getInstance();
		mSolver.reset();
		mSolver.inputFrom(inputPath);
		List<Route> routes = mSolver.solve(20);
		mSaveRoutes = mSolver.solve(20);
		for (Route line : routes) { // add English route label to array list
			mMenuButtonText.add("Route" + RouteLabel.getInstance().get(routes.indexOf(line)));
		}
		afterSolve(mSolver, routes);
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
				.overviewMapControl(true).streetViewControl(false).doubleClickZoomControl(false)
				.mapType(MapTypeIdEnum.ROADMAP);
		mapView.createMap(options);
		mDirectionsService = new DirectionsService();
		mDirectionsPane = mapView.getDirec();
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
			e.printStackTrace();
		}
	}


	private void afterSolve(IVrpSolver solver, List<Route> routes) {
		if (routes != null) {
			for (Route route : routes) {
			    int index = routes.indexOf(route);
				drawDriections(solver.getCenter(index), solver.getCenter(index), route);
			}

            mCurrentRouteGuide.clear();
            for (Route route : routes) {
                MenuButton_Showitem(mSaveRoutes.indexOf(route), false);

                RouteGuide.setItems(mCurrentRouteGuide);
                RouteGuide.setCellFactory(
                        new Callback<ListView<ColorfulDepot>, ListCell<ColorfulDepot>>() {
                            @Override
                            public ListCell<ColorfulDepot> call(ListView<ColorfulDepot> listView) {
                                return new RouteGuidItem();
                            }
                        });
            }
		}
	}

	private void drawDriections(Depot start, Depot end, Route route) {
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

            mGMapLineList.add(LineMarkers); // save any route markers
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

        DirectionsRenderer directionsRenderer = new DirectionsRenderer(false, mapView.getMap(), mDirectionsPane, RouteColor.getInstance().get(route.getId()));
        mDirectionsService.getRoute(request, this, directionsRenderer);
        LineMarkers.saveRoute(directionsRenderer);

        LineMarkers.setVisible(true);
    }
    
    private void updateMenubutton() {
        MenuButton.getItems().clear(); // initialization menu button item text
        MenuItem allitem = new MenuItem(TITLE_ALL_ROUTE);
        MenuButton.getItems().add(allitem);
        allitem.setOnAction(MenuButtonAction);
        for (String lines : mMenuButtonText) {
            MenuItem item = new MenuItem(lines);
            MenuButton.getItems().addAll(item); // add menu item to menu button
            String idString = String.valueOf(mMenuButtonText.indexOf(lines));
            item.setId(idString); // set item idString
            item.setOnAction(MenuButtonAction);
        }
        mMenuButtonText.clear(); // clear menu button text
        MenuButton.setText("Options");
    }

    private void MenuButton_Showitem(int idint, boolean clear) {
        if (clear) mCurrentRouteGuide.clear();

        ColorfulDepot startdepot = new ColorfulDepot(idint, mSolver.getCenter(idint), ListViewID.START);
        mCurrentRouteGuide.add(startdepot);
        for (Depot depot : mSaveRoutes.get(idint).getDepots()) {
            ColorfulDepot pointdepot = new ColorfulDepot(idint, depot, ListViewID.WAYPOINT);
            pointdepot.setDepotPoint(mSaveRoutes.get(idint).getDepots().indexOf(depot));
            mCurrentRouteGuide.add(pointdepot);
        }
        ColorfulDepot enddepot = new ColorfulDepot(idint, mSolver.getCenter(idint), ListViewID.END);
        mCurrentRouteGuide.add(enddepot);
    }

    private void MenuButton_ShowRoute(int idint) {
        for (int i = 0; i < mGMapLineList.size(); i++) {
            if (i == idint) {
                if (mGMapLineList.get(i).getVisible() == false) {
                    DirectionsRenderer render = mGMapLineList.get(i).getRoute();
                    render.setMap(mapView.getMap()); // show lines

                    List<Marker> marker = mGMapLineList.get(i).getMarker();
                    for (Marker markers : marker) {
                        mapView.getMap().addMarker(markers); // show markers
                    }
                    mGMapLineList.get(i).setVisible(true);
                }
            } else {
                if (mGMapLineList.get(i).getVisible() == true) {
                    DirectionsRenderer render = mGMapLineList.get(i).getRoute();
                    render.clearDirections(); // hide lines

                    List<Marker> marker = mGMapLineList.get(i).getMarker();
                    for (Marker markers : marker) {
                        mapView.getMap().removeMarker(markers); // hide markers
                    }
                    mGMapLineList.get(i).setVisible(false);
                }
            }
        }
    }

    private EventHandler<ActionEvent> MenuButtonAction = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (event.getSource() instanceof MenuItem) {
                if (((MenuItem)event.getSource()).getText().equals(TITLE_ALL_ROUTE)) {
                    for (GMapLine lines : mGMapLineList) { // Show Total Routes
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
                    mCurrentRouteGuide.clear();;
                    for (Route route : mSaveRoutes) {
                        MenuButton_Showitem(mSaveRoutes.indexOf(route), false);
                    }
                } else {
                    int idx = Integer.valueOf(((MenuItem)event.getSource()).getId());
                    MenuButton_ShowRoute(idx); // menu button action event functions
                    MenuButton_Showitem(idx, true);
                }
                MenuButton.setText(((MenuItem)event.getSource()).getText());
                RouteGuide.setItems(mCurrentRouteGuide);
                RouteGuide.setCellFactory(new Callback<ListView<ColorfulDepot>, ListCell<ColorfulDepot>>() {
                    @Override
                    public ListCell<ColorfulDepot> call(ListView<ColorfulDepot> listView) {
                        return new RouteGuidItem();
                    }
                });
            }
        }
    };
}