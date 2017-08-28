package com.goldtek.jsprit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.goldtek.algorithm.Cost;
import com.goldtek.algorithm.Depot;
import com.goldtek.algorithm.IVrpSolver;
import com.goldtek.algorithm.RelationKey;
import com.goldtek.algorithm.Route;
import com.graphhopper.jsprit.core.algorithm.VehicleRoutingAlgorithm;
import com.graphhopper.jsprit.core.algorithm.box.Jsprit;
import com.graphhopper.jsprit.core.problem.VehicleRoutingProblem;
import com.graphhopper.jsprit.core.problem.job.Delivery;
import com.graphhopper.jsprit.core.problem.job.Job;
import com.graphhopper.jsprit.core.problem.job.Pickup;
import com.graphhopper.jsprit.core.problem.job.Service;
import com.graphhopper.jsprit.core.problem.job.Shipment;
import com.graphhopper.jsprit.core.problem.solution.VehicleRoutingProblemSolution;
import com.graphhopper.jsprit.core.problem.solution.route.VehicleRoute;
import com.graphhopper.jsprit.core.problem.solution.route.activity.DeliverShipment;
import com.graphhopper.jsprit.core.problem.solution.route.activity.PickupShipment;
import com.graphhopper.jsprit.core.problem.solution.route.activity.TourActivity;
import com.graphhopper.jsprit.core.problem.vehicle.Vehicle;
import com.graphhopper.jsprit.core.util.Solutions;
import com.graphhopper.jsprit.core.util.VehicleRoutingTransportCostsMatrix;
import com.graphhopper.jsprit.io.problem.VrpXMLReader;

public class JspritSolver implements IVrpSolver {
    protected final static String SHIPMENT_HINT = "包含快遞";
	protected static IVrpSolver sInstance = null;
	protected VehicleRoutingProblem.Builder mVrpBuilder = VehicleRoutingProblem.Builder.newInstance();
	protected VehicleRoutingTransportCostsMatrix mCostMatrix = null;
	protected VehicleRoutingProblem mVRP = null;
	protected List<Route> mShowRoutes = new ArrayList<>();
	protected List<Depot> mCenterList = new ArrayList<>();
	
	protected JspritSolver() {}
	
	public static IVrpSolver getInstance() {
		if (sInstance == null) sInstance = new JspritSolver();
		return sInstance;
	}
	
	public String debug() {
		return "debug for ";
	}
	
	@Override
	public void reset() {
		mVrpBuilder = VehicleRoutingProblem.Builder.newInstance();
		mVRP = null;
		mCostMatrix = null;
	}

	@Override
	public void inputFrom(String path) {
		new VrpXMLReader(mVrpBuilder).read(path);
	}

	@Override
	public void costFrom(String path) {
    	VehicleRoutingTransportCostsMatrix.Builder matrixBuilder = VehicleRoutingTransportCostsMatrix.Builder.newInstance(false);

    	try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(path)));
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                String[] lineTokens = line.split(",");
                matrixBuilder.addTransportDistance(lineTokens[0], lineTokens[1], Integer.parseInt(lineTokens[2]));
            }
            reader.close();
    	} catch (IOException ex) {
    		ex.printStackTrace();
    	}
    	
    	mCostMatrix = matrixBuilder.build();
	}
	
	@Override
	public void costFrom(List<Cost> list) {
	    if (list == null) {
	        mCostMatrix = null;
	        return;
	    }
	    
	    VehicleRoutingTransportCostsMatrix.Builder matrixBuilder = VehicleRoutingTransportCostsMatrix.Builder.newInstance(false);
	    //HashMap<RelationKey, Double> distances = new HashMap<>();
	    for (Cost cost : list) {
	        //System.out.println(cost.getKey().from + " to " + cost.getKey().to + " => " + cost.getCost());
	        matrixBuilder.addTransportDistance(cost.getKey().from, cost.getKey().to, cost.getCost());
	    }
	    mCostMatrix = matrixBuilder.build();
	}

	
	@Override
	public List<Depot> getAllService() {
		List<Depot> list = new ArrayList<>();

		for (Job job : mVrpBuilder.getAddedJobs()) {
			if (job instanceof Service) {
				Service serv = (Service) job;
				
				boolean isNewDepot = true;
				for (Depot depot : list) {
					if (serv.getLocation().getId().equalsIgnoreCase(depot.getLocationID())) {
						isNewDepot = false;
						
						if (serv instanceof Pickup) depot.setPickupCapacity(serv.getSize().get(0));
						else if (serv instanceof Delivery) depot.setDeliverCapacity(serv.getSize().get(0));

						break;
					}
				}
				if (isNewDepot) {
					Depot depot = new Depot(serv.getLocation().getId(), serv.getName(),
							serv.getLocation().getCoordinate().getX(), serv.getLocation().getCoordinate().getY());
					
					if (serv instanceof Pickup) depot.setPickupCapacity(serv.getSize().get(0));
					else if (serv instanceof Delivery) depot.setDeliverCapacity(serv.getSize().get(0));
						
					list.add(depot);
				}				
			}
		}
		return list;
	}

	@Override
	public Depot getCenter(int route) {
	    if (route < 0 || route >= mShowRoutes.size()) return null;
	    else return mCenterList.get(route);
	}

	@Override
	public List<Route> solve(int iterations) {
	    // TODO: mapping all matrix with mVrpBuilder service stop
	    if (mCostMatrix != null) mVrpBuilder.setRoutingCost(mCostMatrix);
	    
		mVRP = mVrpBuilder.build();
		VehicleRoutingAlgorithm algorithm = Jsprit.createAlgorithm(mVRP);
    	algorithm.setMaxIterations(iterations);

    	Collection<VehicleRoutingProblemSolution> solutions = algorithm.searchSolutions();
    	VehicleRoutingProblemSolution bestSolution = Solutions.bestOf(solutions);

    	int index = 0;
    	mShowRoutes.clear();

    	for (VehicleRoute vehicleRoute : bestSolution.getRoutes()) {
    		Route showRoute = new Route(index++, vehicleRoute.getVehicle().getId());

    		for (TourActivity act : vehicleRoute.getActivities()) {
    			Depot depot = null;
    			
    			if (showRoute.getLastDepot() != null && showRoute.getLastDepot().getLocationID().equals(act.getLocation().getId())) {
    				depot = showRoute.getLastDepot();
    			} else {
    				if (act instanceof TourActivity.JobActivity) {
                    	Job job = ((TourActivity.JobActivity) act).getJob();
                    	String name = "-";

                    	if (job instanceof Pickup || job instanceof Delivery)
                    	    name = job.getName();
                    	else if (job instanceof Shipment) {
                    	    if (act instanceof PickupShipment)
                    	        name = job.getName().substring(0, job.getName().indexOf("-"));
                    	    else if (act instanceof DeliverShipment)
                    	        name = job.getName().substring(job.getName().indexOf("-")+1);
                    	}
                    	    
                        depot = new Depot(act.getLocation().getId(), name,
                                act.getLocation().getCoordinate().getX(), act.getLocation().getCoordinate().getY());

                    } else {
    					depot = new Depot(act.getLocation().getId(), "-", act.getLocation().getCoordinate().getX(),
    							act.getLocation().getCoordinate().getY());
                    }
                    showRoute.addDepot(depot);
    			}
    			
    			if (act instanceof TourActivity.JobActivity) {
    				Job job = ((TourActivity.JobActivity) act).getJob();
    				if (job instanceof Pickup) depot.setPickupCapacity(job.getSize().get(0));
					else if (job instanceof Delivery) depot.setDeliverCapacity(job.getSize().get(0));
					else if (job instanceof Shipment) {
					    Shipment ship = (Shipment) job;
					    if (act.getLocation().getId().equals(ship.getPickupLocation().getId()))
					        depot.setPickupShipment(depot.getPickupShipment() + job.getSize().get(0));
					    else if (act.getLocation().getId().equals(ship.getDeliveryLocation().getId()))
					        depot.setDropoffShipment(depot.getDropoffShipment() + job.getSize().get(0));
					}
    				
                    if (job instanceof Shipment && !depot.getName().contains(SHIPMENT_HINT)) {
                        depot.setNickName(String.format("%s (%s)", depot.getName(), SHIPMENT_HINT));
                    }
    			}
    		}

    		mShowRoutes.add(showRoute);
    	}
    	
    	boolean debug = false;
        if (debug) {
            for (VehicleRoute route : bestSolution.getRoutes()) {
                System.out.println("=== route ===");

                System.out.print(route.getVehicle().getId() + ": [ ");
                for (TourActivity act : route.getActivities()) {
                    String jobId;
                    if (act instanceof TourActivity.JobActivity) {
                        jobId = ((TourActivity.JobActivity) act).getJob().getId();
                    } else {
                        jobId = "-";
                    }
                    System.out.print(jobId + " ");
                }
                System.out.println("]");
            }

            if (bestSolution.getUnassignedJobs().size() > 0) {
                System.out.println("=== Unassigned Service ===");
                System.out.print("[ ");
                for (Job job : bestSolution.getUnassignedJobs()) {
                    if (job instanceof Service) {
                        System.out.print(job.getId() + " ");
                    }
                }
                System.out.println("]");
            }
        }
    	
    	updateCenter();
    	return mShowRoutes;
	}
	
	protected void updateCenter() {
	    mCenterList.clear();
	    for (Route route : mShowRoutes) {
	        int pickupAmount = 0;
	        int deliverAmount = 0;
	        for (Depot depot : route.getDepots()) {
	            if (!depot.getName().contains("-")) {
	                pickupAmount += depot.getDeliverCapacity();
	                deliverAmount += depot.getPickupCapacity();
	            }
	        }
	        
	        Depot center = null;
	        if (mVrpBuilder.getAddedVehicles().size() > 0) {
	            Vehicle car = mVrpBuilder.getAddedVehicles().iterator().next();
	            center = new Depot(true, car.getStartLocation().getId(), car.getId(),
	                    car.getStartLocation().getCoordinate().getX(), car.getStartLocation().getCoordinate().getY(),
	                    pickupAmount, deliverAmount);
	        }
	        if (center != null) mCenterList.add(center);
	    }
	}
}
