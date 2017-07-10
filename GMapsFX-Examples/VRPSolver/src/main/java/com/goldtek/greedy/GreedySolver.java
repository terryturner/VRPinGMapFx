package com.goldtek.greedy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.goldtek.algorithm.Depot;
import com.goldtek.algorithm.IVrpSolver;
import com.goldtek.algorithm.Route;
import com.goldtek.jsprit.JspritSolver;
import com.graphhopper.jsprit.core.problem.Location;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.cost.VehicleRoutingTransportCosts;
import com.graphhopper.jsprit.core.problem.job.Delivery;
import com.graphhopper.jsprit.core.problem.job.Job;
import com.graphhopper.jsprit.core.problem.job.Pickup;
import com.graphhopper.jsprit.core.problem.job.Service;
import com.graphhopper.jsprit.core.problem.vehicle.Vehicle;
import com.graphhopper.jsprit.io.problem.VrpXMLReader;

public class GreedySolver extends JspritSolver {

    protected GreedySolver() {
        super();
    }

    public static IVrpSolver getInstance() {
        if (sInstance == null)
            sInstance = new GreedySolver();
        return sInstance;
    }

    @Override
    public List<Route> solve(int iterations) {
        mVRP = mVrpBuilder.build();

        if (mVRP.getFleetSize().equals(VehicleRoutingProblem.FleetSize.FINITE)) {
            return finiteFleet(mVRP.getTransportCosts());
        }
        else {
            return infiniteFleet(mVRP.getTransportCosts());
        }
    }
    
    private List<Route> infiniteFleet(VehicleRoutingTransportCosts costs) {
        List<ConsiderJob> greedyRoute = greedyRoute(costs, mVrpBuilder.getAddedVehicles().iterator().next());
        mShowRoutes.clear();
        
        if (!mVrpBuilder.getAddedVehicles().isEmpty()) {
            Vehicle vehicle = mVrpBuilder.getAddedVehicles().iterator().next();
            final int capacity = vehicle.getType().getCapacityDimensions().get(0);

            boolean isTransportable = true;
            int index = 0;
            while (isTransportable) {
                Route vehicleRoute = new Route(index++);
                int curDeliverAmount = capacity;
                int curPickupAmount = 0;
                
                for (ConsiderJob job : greedyRoute) {
                    if (job.isConsiderable() && job.get() instanceof Service) {
                        Service service = (Service) job.get();
                        int serviceAmount = service.getSize().get(0);

                        Depot depot = null;
                        if (vehicleRoute.getLastDepot() != null
                                && vehicleRoute.getLastDepot().getLocationID().equals(service.getLocation().getId())) {
                            depot = vehicleRoute.getLastDepot();
                        } else {
                            depot = new Depot(service.getLocation().getId(), service.getName(),
                                    service.getLocation().getCoordinate().getX(),
                                    service.getLocation().getCoordinate().getY());
                            vehicleRoute.addDepot(depot);
                        }

                        if (service instanceof Delivery && curDeliverAmount >= serviceAmount) {
                            depot.setDeliverCapacity(serviceAmount);
                            curDeliverAmount -= serviceAmount;
                            job.isConsiderable(false);
                        } else if (service instanceof Pickup && (curPickupAmount + serviceAmount) <= capacity) {
                            depot.setPickupCapacity(serviceAmount);
                            curPickupAmount += serviceAmount;
                            job.isConsiderable(false);
                        } else { //full capacity
                            break;
                        }
                    } else {
                        job.isConsiderable(false);
                    }
                }
                
                if (vehicleRoute.getDepots().size() > 0) mShowRoutes.add(vehicleRoute);
                
                isTransportable = false;
                for (ConsiderJob job : greedyRoute) {
                    if (job.isConsiderable())
                        isTransportable = true;
                }
            }
        }
        updateCenter();
        return mShowRoutes;
    }
    
    private List<Route> finiteFleet(VehicleRoutingTransportCosts costs) {
        List<ConsiderJob> greedyRoute = greedyRoute(costs, mVrpBuilder.getAddedVehicles().iterator().next());
        mShowRoutes.clear();

        mVrpBuilder.getAddedVehicles().forEach(vehicle -> {
            final int capacity = vehicle.getType().getCapacityDimensions().get(0);
            Route vehicleRoute = new Route(vehicle.getIndex());
            int curDeliverAmount = capacity;
            int curPickupAmount = 0;

            for (ConsiderJob job : greedyRoute) {
                if (job.isConsiderable() && job.get() instanceof Service) {
                    Service service = (Service) job.get();
                    int serviceAmount = service.getSize().get(0);

                    Depot depot = null;
                    if (vehicleRoute.getLastDepot() != null
                            && vehicleRoute.getLastDepot().getLocationID().equals(service.getLocation().getId())) {
                        depot = vehicleRoute.getLastDepot();
                    } else {
                        depot = new Depot(service.getLocation().getId(), service.getName(),
                                service.getLocation().getCoordinate().getX(),
                                service.getLocation().getCoordinate().getY());
                        vehicleRoute.addDepot(depot);
                    }

                    if (service instanceof Delivery && curDeliverAmount >= serviceAmount) {
                        depot.setDeliverCapacity(serviceAmount);
                        curDeliverAmount -= serviceAmount;
                        job.isConsiderable(false);
                    } else if (service instanceof Pickup && (curPickupAmount + serviceAmount) <= capacity) {
                        depot.setPickupCapacity(serviceAmount);
                        curPickupAmount += serviceAmount;
                        job.isConsiderable(false);
                    } else { //full capacity
                        break;
                    }
                } else {
                    job.isConsiderable(false);
                }

            }
            
            if (vehicleRoute.getDepots().size() > 0) mShowRoutes.add(vehicleRoute);
        });
        updateCenter();
        return mShowRoutes;
    }

    private List<ConsiderJob> greedyRoute(VehicleRoutingTransportCosts costs, Vehicle vehicle) {
        List<ConsiderJob> greedyJobs = new ArrayList<>();
        List<ConsiderJob> jobs = new ArrayList<>();

        for (Job job : mVrpBuilder.getAddedJobs()) {
            jobs.add(new ConsiderJob(job));
        }

        Location from = vehicle.getStartLocation();
        
        boolean isTrasportable = true;
        while (isTrasportable) {
            double leastCost = Double.MAX_VALUE;
            int which = -1;
            for (int idx = 0; idx < jobs.size(); idx++) {
                ConsiderJob job = jobs.get(idx);
                
                if (jobs.get(idx).isConsiderable() && jobs.get(idx).get() instanceof Service) {
                    double transCost = costs.getTransportCost(from, ((Service) job.get()).getLocation(), 0., null, vehicle);
                    
                    if (transCost < leastCost) {
                        leastCost = transCost;
                        which = idx;
                    }
                } else {
                    job.isConsiderable(false);
                }
            }
            
            if (which != -1) {
                from = ((Service) jobs.get(which).get()).getLocation();
                jobs.get(which).isConsiderable(false);
                
                greedyJobs.add(jobs.get(which));
            }
            
            isTrasportable = false;
            for (ConsiderJob job : jobs) {
                if (job.isConsiderable())
                    isTrasportable = true;
            }
        }
        
        for (ConsiderJob job : greedyJobs) {
            job.isConsiderable(true);
        }

        return greedyJobs;
    }

    private List<Route> finiteFleet3(VehicleRoutingTransportCosts costs) {
        List<ConsiderJob> jobs = new ArrayList<>();
        mShowRoutes.clear();

        for (Job job : mVrpBuilder.getAddedJobs()) {
            jobs.add(new ConsiderJob(job));
        }

        int index = 0;
        for (Vehicle vehicle : mVrpBuilder.getAddedVehicles()) {
            final int capacity = vehicle.getType().getCapacityDimensions().get(0);
            Route vehicleRoute = new Route(index++);
            int curDeliverAmount = capacity;
            int curPickupAmount = 0;
            Location from = vehicle.getStartLocation();

            for (ConsiderJob job : jobs)
                job.isConsiderable(true); // initialize

            boolean isTrasportable = true;
            while (isTrasportable) {
                double leastCost = Double.MAX_VALUE;
                int which = -1;
                for (int idx = 0; idx < jobs.size(); idx++) {
                    ConsiderJob job = jobs.get(idx);

                    if (jobs.get(idx).isConsiderable() && jobs.get(idx).get() instanceof Service) {
                        int jobAmount = job.get().getSize().get(0);
                        double transCost = costs.getTransportCost(from, ((Service) job.get()).getLocation(), 0., null,
                                vehicle);
                        if ((job.get() instanceof Delivery && curDeliverAmount >= jobAmount && transCost < leastCost)
                                || (job.get() instanceof Pickup && (curPickupAmount + jobAmount) <= capacity && transCost < leastCost)) {
                            leastCost = transCost;
                            which = idx;
                        } else if ((job.get() instanceof Delivery && curDeliverAmount < jobAmount)
                                || (job.get() instanceof Pickup && (curPickupAmount + jobAmount) > capacity)) {
                            job.isConsiderable(false);
                        }
                    } else {
                        job.isConsiderable(false);
                    }
                }
                // should get acceptable one
                if (which != -1) {
                    Service service = (Service) jobs.get(which).get();
                    int serviceAmount = service.getSize().get(0);

                    if (service instanceof Delivery)
                        curDeliverAmount -= serviceAmount;
                    else if (service instanceof Pickup)
                        curPickupAmount += serviceAmount;
                    from = service.getLocation();
                    jobs.get(which).isConsiderable(false);

                    Depot depot;
                    if (vehicleRoute.getLastDepot() != null
                            && vehicleRoute.getLastDepot().getLocationID().equals(service.getLocation().getId())) {
                        depot = vehicleRoute.getLastDepot();
                    } else {
                        depot = new Depot(service.getLocation().getId(), service.getName(),
                                service.getLocation().getCoordinate().getX(),
                                service.getLocation().getCoordinate().getY());
                        vehicleRoute.addDepot(depot);
                    }
                    if (service instanceof Delivery)
                        depot.setDeliverCapacity(serviceAmount);
                    else if (service instanceof Pickup)
                        depot.setPickupCapacity(serviceAmount);

                    //System.out.println("go to " + service.getName());
                } else {
                    System.out.println("there's no next");
                }

                isTrasportable = false;
                for (ConsiderJob job : jobs) {
                    if (job.isConsiderable())
                        isTrasportable = true;
                }
            }
            if (vehicleRoute.getDepots().size() > 0)
                mShowRoutes.add(vehicleRoute);
        }

        return mShowRoutes;
    }
}
