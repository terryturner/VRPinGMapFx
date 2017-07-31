package com.goldtek.algorithm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.goldtek.main.FileHandle;
import com.goldtek.main.config.ConfigDepot;
import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem.FleetSize;
import com.graphhopper.jsprit.core.problem.job.Delivery;
import com.graphhopper.jsprit.core.problem.job.Job;
import com.graphhopper.jsprit.core.problem.job.Pickup;
import com.graphhopper.jsprit.core.problem.job.Service;
import com.graphhopper.jsprit.core.problem.vehicle.Vehicle;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleType;
import com.graphhopper.jsprit.core.util.Coordinate;
import com.graphhopper.jsprit.io.problem.VrpXMLReader;
import com.graphhopper.jsprit.io.problem.VrpXMLWriter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class VrpMaker {
    public final static String OUTPUT = "config.xml";
    private static VrpMaker sInatance = null;
    
    public static VrpMaker getInstance() { 
        if (sInatance == null)
            sInatance = new VrpMaker();
        return sInatance;
    }
    
    private VrpMaker() {
        String inputPath = "input/goldtek_golden_sample.xml";
        if (FileHandle.getInstance().isExists(inputPath))
        {
            readForGoldenSample(inputPath);
        }
    }
    
    private final VehicleRoutingProblem.Builder mGoldenSampleBuilder = VehicleRoutingProblem.Builder.newInstance();
    private VehicleRoutingProblem.Builder mOutputBuilder = null;
    private boolean mInitialized = false;
    
    public boolean intialized() { return mInitialized; }
    
    public void readForGoldenSample(String path) {
        if (!intialized()) {
            new VrpXMLReader(mGoldenSampleBuilder).read(path);
            mInitialized = true;
        }
        readFromOutput();
    }
    
    public void readFromOutput() {
        if (FileHandle.getInstance().isExists(OUTPUT)) {
            mOutputBuilder = VehicleRoutingProblem.Builder.newInstance();
            new VrpXMLReader(mOutputBuilder).read(OUTPUT);
        }
    }
    
    public void buildFiniteSize(Collection<Car> Cars, Collection<Depot> Depots) {
        VehicleRoutingProblem.Builder vrpBuilder = VehicleRoutingProblem.Builder.newInstance();
        vrpBuilder.setFleetSize(FleetSize.FINITE);
        
        if (Cars != null) {
            for (Car car : Cars) {
                for (VehicleType type : mGoldenSampleBuilder.getAddedVehicleTypes()) {
                    if (type.getTypeId().equals(car.getModel())) {
                        vrpBuilder.addVehicle(car.toVehicle(type));
                    }
                }
            }
        }
        
        if (Depots != null) {
            int serviceId = 2;
            for (Depot depot : Depots) {
                Location.Builder locBuilder = Location.Builder.newInstance();
                locBuilder.setId(depot.getLocationID());
                locBuilder.setCoordinate(Coordinate.newInstance(depot.getLongitude(), depot.getLatitude()));
                if (depot.getDeliverCapacity() > 0) {
                    vrpBuilder.addJob(Delivery.Builder.newInstance(String.valueOf(serviceId++)).setName(depot.getName()).addSizeDimension(0, depot.getDeliverCapacity()).setLocation(locBuilder.build()).build());
                }
                if (depot.getPickupCapacity() > 0) {
                    vrpBuilder.addJob(Pickup.Builder.newInstance(String.valueOf(serviceId++)).setName(depot.getName()).addSizeDimension(0, depot.getPickupCapacity()).setLocation(locBuilder.build()).build());
                }
            }
        }
        
        new VrpXMLWriter(vrpBuilder.build()).write(OUTPUT);
        
        readFromOutput();
    }
    
    public ObservableList<CarModel> getGoldenSampleCarModel() {
        ObservableList<CarModel> CarModels = FXCollections.observableArrayList();
        for (VehicleType type : mGoldenSampleBuilder.getAddedVehicleTypes()) {
            CarModels.add(new CarModel(type));
        }
        return CarModels;
    }
    
    public ObservableList<Car> getGoldenSampleCar() {
        ObservableList<Car> Cars = FXCollections.observableArrayList();
        Collection<Vehicle> addedVehicles = null;
        
        if (mOutputBuilder != null && mOutputBuilder.getAddedVehicles().size() > 0) addedVehicles = mOutputBuilder.getAddedVehicles();
        else addedVehicles = mGoldenSampleBuilder.getAddedVehicles();
        
        if (addedVehicles != null) {
            for (Vehicle vehicle : addedVehicles) {
                Cars.add(new Car(vehicle));
            }
        }
        return Cars;
    }
    
    public ObservableList<Depot> getGoldenSampleDepot() {
        ObservableList<Depot> Depots = FXCollections.observableArrayList();
        for (Job job : mGoldenSampleBuilder.getAddedJobs()) {
            if (job instanceof Service) {
                Service serv = (Service) job;
                Depots.add(new Depot(serv.getLocation().getId(), serv.getName(), serv.getLocation().getCoordinate().getX(), serv.getLocation().getCoordinate().getY()));
            }
        }
        return Depots;
    }
    
    public ObservableList<ConfigDepot> getConfigDepot() {
        ObservableList<ConfigDepot> Depots = FXCollections.observableArrayList();
        HashMap<Coordinate, ConfigDepot> temp = new HashMap<>();
        
        for (Job job : mGoldenSampleBuilder.getAddedJobs()) {
            if (job instanceof Service) {
                Service serv = (Service) job;
                ConfigDepot depot = new ConfigDepot(serv.getLocation().getId(), null, null, serv.getName(), serv.getLocation().getCoordinate().getX(), serv.getLocation().getCoordinate().getY());
                temp.put(serv.getLocation().getCoordinate(), depot);
            }
        }
        
        if (mOutputBuilder != null) {
            for (Job job : mOutputBuilder.getAddedJobs()) {
                if (job instanceof Service) {
                    Service serv = (Service) job;
                    if (serv instanceof Pickup) {
                        temp.get(serv.getLocation().getCoordinate()).setOn(true);
                        temp.get(serv.getLocation().getCoordinate()).setPickup(serv.getSize().get(0));
                    }
                    if (serv instanceof Delivery) {
                        temp.get(serv.getLocation().getCoordinate()).setOn(true);
                        temp.get(serv.getLocation().getCoordinate()).setDeliver(serv.getSize().get(0));
                    }
                }
            }
        }
        
        for (Map.Entry<Coordinate, ConfigDepot> entry : temp.entrySet()) {
            Depots.add(entry.getValue());
        }
        
        return Depots;
    }
    
}
