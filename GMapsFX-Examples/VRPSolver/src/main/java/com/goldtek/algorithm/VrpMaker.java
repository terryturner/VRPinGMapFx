package com.goldtek.algorithm;

import java.util.Collection;

import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem.FleetSize;
import com.graphhopper.jsprit.core.problem.job.Job;
import com.graphhopper.jsprit.core.problem.job.Service;
import com.graphhopper.jsprit.core.problem.vehicle.VehicleType;
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
    
    private VrpMaker() {}
    
    private final VehicleRoutingProblem.Builder mGoldenSampleBuilder = VehicleRoutingProblem.Builder.newInstance();
    private final VehicleRoutingProblem.Builder mVrpBuilder = VehicleRoutingProblem.Builder.newInstance();
    
    public void readForGoldenSample(String path) {
        new VrpXMLReader(mGoldenSampleBuilder).read(path);
    }
    
    public void buildFiniteSize() {
        mVrpBuilder.setFleetSize(FleetSize.FINITE);
        new VrpXMLWriter(mVrpBuilder.build()).write("config.xml");
    }
    
    public ObservableList<CarModel> getGoldenSampleCarModel() {
        return CarModel.toCarModelList(mGoldenSampleBuilder.getAddedVehicleTypes());
    }
    
    public ObservableList<Car> getGoldenSampleCar() {
        return Car.toCarList(mGoldenSampleBuilder.getAddedVehicles());
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
    
    public void setCars(Collection<Car> Cars) {
        for (Car car : Cars) {
            for (VehicleType type : mGoldenSampleBuilder.getAddedVehicleTypes()) {
                if (type.getTypeId().equals(car.getModel())) {
                    mVrpBuilder.addVehicle(car.toVehicle(type));
                }
            }
        }
    }
    
}
