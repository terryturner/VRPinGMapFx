package com.goldtek.algorithm;

import java.util.Collection;

import com.goldtek.main.FileHandle;
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
    private boolean mInitialized = false;
    
    public boolean intialized() { return mInitialized; }
    
    public void readForGoldenSample(String path) {
        if (!intialized()) {
            new VrpXMLReader(mGoldenSampleBuilder).read(path);
            mInitialized = true;
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
        
        new VrpXMLWriter(vrpBuilder.build()).write("config.xml");
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
        for (Vehicle vehicle : mGoldenSampleBuilder.getAddedVehicles()) {
            Cars.add(new Car(vehicle));
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
    
}
