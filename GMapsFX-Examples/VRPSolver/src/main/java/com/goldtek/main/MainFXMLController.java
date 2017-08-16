package com.goldtek.main;

import com.goldtek.jsprit.JspritSolver;
import com.goldtek.main.config.ConfigDialog;
import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.*;
import com.lynden.gmapsfx.service.directions.*;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import com.goldtek.main.routeguide.*;
import com.goldtek.main.routeguide.ColorfulDepot.ListViewID;
import com.goldtek.algorithm.*;
import com.goldtek.database.DbManager;
import com.goldtek.database.IDbCallback;

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
	protected List<Cost> mCostRoutes = new ArrayList<>();
	protected IVrpSolver mSolver = JspritSolver.getInstance();

	@FXML protected BorderPane RootPane;
	@FXML protected GoogleMapView MapView;
	@FXML protected ListView<ColorfulDepot> RouteGuide;
	@FXML protected MenuButton MenuButton;
	@FXML protected CheckMenuItem MenuCostAssist;
	
	private WorkIndicatorDialog<Boolean> mProgress = null;

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
	     case "MenuStart":
	         String inputPath = VrpMaker.OUTPUT;
	         clearAll();
	         
	         if (!FileHandle.getInstance().isExists(inputPath)) {
	             File file = FileHandle.getInstance().showXMLChooser(RootPane.getScene().getWindow());
	             if (file == null) return;
	             else inputPath = file.getPath();
	         }

             mSolver.reset();
             mSolver.inputFrom(inputPath);

	         mProgress = new WorkIndicatorDialog<>(RootPane.getScene().getWindow(), "Processing VRP");
	         mProgress.exec(Boolean.FALSE, inputParam -> {
	             if (mCostRoutes.size() > 0 && MenuCostAssist.isSelected()) {
	                 mSolver.costFrom(mCostRoutes);
	             }
	             mSaveRoutes = mSolver.solve(2000);
	             return new Integer(1);
	         });
	         mProgress.addTaskEndNotification(result -> {
	             afterSolve(mSolver, mSaveRoutes);
	             updateMenubutton();
	         });

	         break;
		case "MenuCostAssist":
		    if (item instanceof CheckMenuItem && MenuCostAssist.isSelected()) {
                if (mCostRoutes.size() == 0) {
                    mProgress = new WorkIndicatorDialog<>(RootPane.getScene().getWindow(), "Enable the Assistant...");
                    mProgress.addTaskEndNotification(result -> {
                        if (result == 0) {
                            ((CheckMenuItem) item).setSelected(false);
                            Alert alert = new Alert(AlertType.WARNING);
                            alert.setTitle("Warning");
                            alert.setHeaderText(null);
                            alert.setContentText("Cannot enable the cost assistant, please try again later !");

                            alert.showAndWait();
                        }
                    });
                    mProgress.exec(Boolean.FALSE, inputParam -> {
                        getCost();
                        while (mProgress.isVisible()) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        return new Integer(mProgress.isException() ? 0 : 1);
                    });
                }
		    }
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
					MapView.getMap().removeMarker(marker); // hide markers
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
				render.setMap(MapView.getMap()); // show lines

				List<Marker> marker = lines.getMarker();
				for (Marker markers : marker) {
					MapView.getMap().addMarker(markers); // show markers
				}
				lines.setVisible(true);
			}
		}
	}

	@Override
	public void directionsReceived(DirectionsResult results, DirectionStatus status) {
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		MapView.addMapInializedListener(this);
		DbManager.getInstance();		
	}

	@Override
	public void mapInitialized() {
		MapOptions options = new MapOptions();

		options.center(new LatLong(24.997861, 121.486786)).zoomControl(true).mapTypeControl(false).zoom(14)
				.overviewMapControl(true).streetViewControl(false).doubleClickZoomControl(false)
				.mapType(MapTypeIdEnum.ROADMAP);
		MapView.createMap(options);
		mDirectionsService = new DirectionsService();
		mDirectionsPane = MapView.getDirec();
	}

	private void afterSolve(IVrpSolver solver, List<Route> routes) {
		if (routes != null) {
			for (Route route : routes) {
			    int index = routes.indexOf(route);
				drawDriections(solver.getCenter(index), solver.getCenter(index), route);
				RouteLabel.getInstance().setDriver(index, route.getDriver());
				mMenuButtonText.add(String.format("%s - %s", RouteLabel.getInstance().get(index), RouteLabel.getInstance().getDriver(index)));
			}

            mCurrentRouteGuide.clear();
            for (Route route : routes) {
                MenuButton_Showitem(routes.indexOf(route), false);

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

    private void clearAll() {
        MapView.getMap().clearMarkers();
        for (GMapLine lines : mGMapLineList) {
            DirectionsRenderer render = lines.getRoute();
            render.clearDirections();
        }
        mGMapLineList.clear(); // clear any route markers
        mCurrentRouteGuide.clear();
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

                MapView.getMap().addMarker(marker);
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
        MapView.getMap().addMarker(marker);

        DirectionsRenderer directionsRenderer = new DirectionsRenderer(false, MapView.getMap(), mDirectionsPane, RouteColor.getInstance().get(route.getId()));
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

    private void MenuButton_Showitem(int index, boolean clear) {
        if (clear) mCurrentRouteGuide.clear();

        ColorfulDepot startdepot = new ColorfulDepot(index, mSolver.getCenter(index), ListViewID.START);
        mCurrentRouteGuide.add(startdepot);
        for (Depot depot : mSaveRoutes.get(index).getDepots()) {
            ColorfulDepot pointdepot = new ColorfulDepot(index, depot, ListViewID.WAYPOINT);
            pointdepot.setDepotPoint(mSaveRoutes.get(index).getDepots().indexOf(depot));
            mCurrentRouteGuide.add(pointdepot);
        }
        ColorfulDepot enddepot = new ColorfulDepot(index, mSolver.getCenter(index), ListViewID.END);
        mCurrentRouteGuide.add(enddepot);
    }

    private void MenuButton_ShowRoute(int index) {
        for (int i = 0; i < mGMapLineList.size(); i++) {
            if (i == index) {
                if (mGMapLineList.get(i).getVisible() == false) {
                    DirectionsRenderer render = mGMapLineList.get(i).getRoute();
                    render.setMap(MapView.getMap()); // show lines

                    List<Marker> marker = mGMapLineList.get(i).getMarker();
                    for (Marker markers : marker) {
                        MapView.getMap().addMarker(markers); // show markers
                    }
                    mGMapLineList.get(i).setVisible(true);
                }
            } else {
                if (mGMapLineList.get(i).getVisible() == true) {
                    DirectionsRenderer render = mGMapLineList.get(i).getRoute();
                    render.clearDirections(); // hide lines

                    List<Marker> marker = mGMapLineList.get(i).getMarker();
                    for (Marker markers : marker) {
                        MapView.getMap().removeMarker(markers); // hide markers
                    }
                    mGMapLineList.get(i).setVisible(false);
                }
            }
        }
    }
    
    private void getPalmBox() {
        DbManager.getInstance().getPalmBox(new IDbCallback<Depot>() {
            @Override
            public void onQuery(List<Depot> list) {
                for (Depot depot : list)
                    System.out.println(depot.getName());
            }
        });
    }
    
    private void getCost() {
        DbManager.getInstance().getGmapCost(new IDbCallback<Cost>() {
            @Override
            public void onQuery(List<Cost> list) {
                if (mProgress != null) {
                    if (list != null) {
                        mProgress.setResult(true);
                        mCostRoutes = list;
                    }
                    else mProgress.setResult(false);
                }
            }
        });
    }

    private EventHandler<ActionEvent> MenuButtonAction = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (event.getSource() instanceof MenuItem) {
                if (((MenuItem)event.getSource()).getText().equals(TITLE_ALL_ROUTE)) {
                    for (GMapLine lines : mGMapLineList) { // Show Total Routes
                        if (lines.getVisible() == false) {
                            DirectionsRenderer render = lines.getRoute();
                            render.setMap(MapView.getMap()); // show lines

                            List<Marker> marker = lines.getMarker();
                            for (Marker markers : marker) {
                                MapView.getMap().addMarker(markers); // show markers
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